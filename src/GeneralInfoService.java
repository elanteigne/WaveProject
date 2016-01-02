import java.util.concurrent.TimeUnit;

public class GeneralInfoService extends Service implements Runnable{
	//Objects
	private Thread generalInfoServiceThread;
	
	//Resources
	public int delay = 500;
	public String serviceGroup = "230.0.0.2";
	public int messageID = 0;
	
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
		sendMessage("Control", messageID, waveManager.controlGroup, serviceGroup, "0/0");
		 messageID++;
	}
	
	public void sendServiceMessage(){
		//Test shows traffic ahead being slower
		//sendMessage("Service", messageID, serviceGroup, serviceGroup, ""+0+"/"+45.3476235+"/"+-73.7597858);
		sendMessage("Service", messageID, serviceGroup, serviceGroup, ""+waveManager.speed+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude);
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
					
					System.out.println("Calculated: TrafficAheadSlower x"+warningLevel);
				}
			}else if(checkIfBehind(direction)){
				if(vehicleSpeed>waveManager.speed){
					int speedDifference = vehicleSpeed - waveManager.speed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficBehindFasterWarningLight = warningLevel;
					
					System.out.println("Calculated: TrafficBehindFaster x"+warningLevel);
				}
			}
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

