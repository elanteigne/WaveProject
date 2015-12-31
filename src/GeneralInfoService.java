import java.util.concurrent.TimeUnit;

public class GeneralInfoService extends Service implements Runnable{
	//Objects
	private Thread generalInfoServiceThread;
	
	//Resources
	public int delay = 500;
	public String serviceGroup = "230.0.0.4";
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
		sendMessage("Service", messageID, serviceGroup, serviceGroup, ""+waveManager.speed+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude);
		 messageID++;
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(String direction, String speed, String lattitude, String longitude){
		int otherVehicleSpeed = Integer.parseInt(speed);
		double otherVehicleLattitude = Double.parseDouble(lattitude);
		double otherVehicleLongitude = Double.parseDouble(longitude);
		
		if(checkIfAhead(direction, otherVehicleLattitude, otherVehicleLongitude)){
			if(otherVehicleSpeed<waveManager.speed){
				int speedDifference = waveManager.speed - otherVehicleSpeed;
				outputWarningLights(speedDifference);
				
				waveManager.trafficAheadSlower = true;
				System.out.println("Calculated: TrafficAheadSlower");
			}else{
				waveManager.trafficAheadSlower = false;		
			}
		}else if(checkIfBehind(direction, otherVehicleLattitude, otherVehicleLongitude)){
			if(otherVehicleSpeed>waveManager.speed){
				int speedDifference = otherVehicleSpeed - waveManager.speed;
				outputWarningLights(speedDifference);
				
				waveManager.trafficBehindFaster = true;
				System.out.println("Calculated: TrafficBehindFaster");
			}else{
				waveManager.trafficBehindFaster = false;		
			}
		}
	}
	
	private boolean checkIfAhead(String direction, double otherVehicleLattitude, double otherVehicleLongitude){
		double distanceBetweenVehicles = twoPointDistance(otherVehicleLattitude, otherVehicleLongitude, waveManager.GPSlattitude, waveManager.GPSlongitude);
		return true;
	}
	
	private boolean checkIfBehind(String direction, double otherVehicleLattitude, double otherVehicleLongitude){
		
		return true;
	}
	
	private void outputWarningLights(int speedDifference){
		if(speedDifference<10){
			//Turn on first warning light
		}else if(speedDifference<20){
			//Turn on second warning light
		}else{
			//Turn on third warning light
		}		
	}

	private double twoPointDistance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 1609.344;

		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
}

