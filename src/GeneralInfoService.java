import java.util.concurrent.TimeUnit;

public class GeneralInfoService extends Service implements Runnable{
	//Objects
	private Thread generalInfoServiceThread;
	
	//Resources
	public int delay;
	public String serviceGroup = "230.0.0.2";
	public int messageID = 0;
	public int numClosebyVehicles;
	//private double closebyVehiclesTimestamp;
	private String output;
	
	//Constructor
	public GeneralInfoService(WaveManager waveManager){
		super(waveManager);
		delay = waveManager.delay*2;
	}

	//Class Methods
	public void start(){
		if(generalInfoServiceThread==null){
			generalInfoServiceThread = new Thread(this, "GeneralInfoService");
			generalInfoServiceThread.start();
		}
	}
	
	public void run(){
		while(true){
			delay = waveManager.delay*2;
			
			sendControlMessage();
			//Wait
			try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
			
			int count = 0;
			while(count<5){
				sendServiceMessage();

				delay = waveManager.delay;
				//Wait
				try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }

				count++;
			}
		}
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		 messageID++;
		 waveManager.userInterface.updateGeneralInfoServicePacketsSent(messageID);
	}
	
	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, "");
		messageID++;
		waveManager.userInterface.updateGeneralInfoServicePacketsSent(messageID);
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(String direction,  int vehicleSpeed, double vehicleLattitude, double vehicleLongitude){
		double distanceBetweenVehicles = calculateDistance(vehicleLattitude, vehicleLongitude);
		
		if(distanceBetweenVehicles<150){
			//Only way to check ahead so far is checking the direction
			if(checkIfAhead(vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed<waveManager.speed){
					int speedDifference = waveManager.speed - vehicleSpeed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficAheadSlowerWarningLight = warningLevel;
					
					System.out.println("o Calculated: TrafficAheadSlower x"+warningLevel);
					output = "o Calculated: TrafficAheadSlower x"+warningLevel;
					waveManager.userInterface.computedGeneralInfo(output);
					
					output = "TrafficAheadSlower x"+warningLevel;
					waveManager.userInterface.writeGeneralInfo(output);
				}else{
					System.out.println("o Calculated: Vehicle is ahead but is faster so is not considered");
					output = "o Calculated: Vehicle is ahead but is faster so is not considered";
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else if(checkIfBehind(vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed>waveManager.speed){
					int speedDifference = vehicleSpeed - waveManager.speed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficBehindFasterWarningLight = warningLevel;
					
					System.out.println("o Calculated: TrafficBehindFaster x"+warningLevel);
					output = "o Calculated: TrafficBehindFaster x"+warningLevel;
					waveManager.userInterface.computedGeneralInfo(output);
					
					output = "TrafficBehindFaster x"+warningLevel;
					waveManager.userInterface.writeGeneralInfo(output);
				}else{
					System.out.println("o Calculated: Vehicle is behind but is slower so is not considered");
					output = "o Calculated: Vehicle is behind but is slower so is not considered";
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else{
				System.out.println("o Calculated: Vehicle is not in critical area, therefore not considered");
				output = "o Calculated: Vehicle is not in critical area, therefore not considered";
				waveManager.userInterface.computedGeneralInfo(output);
			}
			
			//Decide if trafficService should start sending
			//Other solution. Vehicle would know how many vehicles around using sensors, can choose to send then, put code directly in service thread.
			//if(distanceBetweenVehicles<50){
				////Here is where we will decide if TrafficService should send a message
				//if(numClosebyVehicles>10){
					//closebyVehiclesTimestamp = System.currentTimeMillis();
					////advertiseTrafficInfo = true;
					
					//if(vehicleSpeed<40){
						//numClosebyVehicles++;
						////Very Slow traffic
					//}else if(vehicleSpeed<65){
						//numClosebyVehicles++;
						////Slow traffic
					//}else{
						////Traffic moving well
					//}
					////sendTrafficServiceMessage with calculated info for limited time?
				//}
				
				//if(System.currentTimeMillis()>closebyVehiclesTimestamp+5000){
					//numClosebyVehicles = 0;
					////advertiseTrafficInfo = false;
				//}
			//}
		}else{
			System.out.println("o Calculated: Vehicle is too far ahead to be considered");
			output = "o Calculated: Vehicle is too far ahead to be considered";
			waveManager.userInterface.computedGeneralInfo(output);
		}
	}
	
	private int outputWarningLights(int speedDifference){
		if(speedDifference<10){
			return 1; //Turn on first warning light
		}else if(speedDifference<20){
			return 2; //Turn on second warning light
		}else{
			return 3; //Turn on third warning light
		}		
	}
	
}

