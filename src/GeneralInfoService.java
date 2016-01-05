import java.util.concurrent.TimeUnit;

public class GeneralInfoService extends Service implements Runnable{
	//Objects
	private Thread generalInfoServiceThread;
	
	//Resources
	public int delay = 1000;
	public String serviceGroup = "230.0.0.2";
	public int messageID = 0;
	private String output;
	
	//Constructor
	public GeneralInfoService(WaveManager waveManager){
		super(waveManager);
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
			sendControlMessage();
			//Wait
			try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
			
			int count = 0;
			while(count<5){
				sendServiceMessage();
				
				//Wait
				try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }

				count++;
			}
		}
	}
	
	public void sendControlMessage(){
		sendMessage("Control", messageID, waveManager.controlGroup, serviceGroup, "");
		 messageID++;
	}
	
	public void sendServiceMessage(){
		sendMessage("Service", messageID, serviceGroup, serviceGroup, "");
		messageID++;
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(String direction,  int vehicleSpeed, double vehicleLattitude, double vehicleLongitude){
		double distanceBetweenVehicles = calculateDistance(vehicleLattitude, vehicleLongitude, waveManager.GPSlattitude, waveManager.GPSlongitude);

		if(distanceBetweenVehicles<150){
			//Only way to check ahead so far is checking the direction
			if(checkIfAhead(direction)){
				if(vehicleSpeed<waveManager.speed){
					int speedDifference = waveManager.speed - vehicleSpeed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficAheadSlowerWarningLight = warningLevel;
					
					System.out.println("o Calculated: TrafficAheadSlower x"+warningLevel);
					output = "o Calculated: TrafficAheadSlower x"+warningLevel;
					waveManager.userInterface.output(output);
				}else{
					System.out.println("o Calculated: Vehicle is ahead but is faster so is not considered");
					output = "o Calculated: Vehicle is ahead but is faster so is not considered";
					waveManager.userInterface.output(output);
				}
			}else if(checkIfBehind(direction)){
				if(vehicleSpeed>waveManager.speed){
					int speedDifference = vehicleSpeed - waveManager.speed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficBehindFasterWarningLight = warningLevel;
					
					System.out.println("o Calculated: TrafficBehindFaster x"+warningLevel);
					output = "o Calculated: TrafficBehindFaster x"+warningLevel;
					waveManager.userInterface.output(output);
				}else{
					System.out.println("o Calculated: Vehicle is behind but is slower so is not considered");
					output = "o Calculated: Vehicle is behind but is slower so is not considered";
					waveManager.userInterface.output(output);
				}
			}else{
				System.out.println("o Calculated: Vehicle is not in critical area, therefore not considered");
				output = "o Calculated: Vehicle is not in critical area, therefore not considered";
				waveManager.userInterface.output(output);
			}
		}else{
			System.out.println("o Calculated: Vehicle is too far ahead to be considered");
			output = "o Calculated: Vehicle is too far ahead to be considered";
			waveManager.userInterface.output(output);
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

