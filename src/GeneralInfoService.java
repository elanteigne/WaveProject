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
	public void computeData(String direction, String speed, String lattitude, String longitude){
		int otherVehicleSpeed = Integer.parseInt(speed);
		double otherVehicleLattitude = Double.parseDouble(lattitude);
		double otherVehicleLongitude = Double.parseDouble(longitude);
		
		double distanceBetweenVehicles = twoPointDistance(otherVehicleLattitude, otherVehicleLongitude, waveManager.GPSlattitude, waveManager.GPSlongitude);
		if(distanceBetweenVehicles<150){
			//Only way to check ahead so far is checking the direction
			if(checkIfAhead(direction)){
				if(otherVehicleSpeed<waveManager.speed){
					int speedDifference = waveManager.speed - otherVehicleSpeed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficAheadSlowerWarningLight = warningLevel;
					
					System.out.println("Calculated: TrafficAheadSlower x"+warningLevel);
				}
			}else if(checkIfBehind(direction)){
				if(otherVehicleSpeed>waveManager.speed){
					int speedDifference = otherVehicleSpeed - waveManager.speed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficBehindFasterWarningLight = warningLevel;
					
					System.out.println("Calculated: TrafficBehindFaster x"+warningLevel);
				}
			}
		}
	}
	
	private boolean checkIfAhead(String direction){
		if(direction.equals(waveManager.direction)){		
			return true;
		}else{		
			return false;
		}
	}
	
	private boolean checkIfBehind(String direction){
		String oppositeDirection = "";
		if(direction.equals("N")){
			oppositeDirection = "S";
		}else if(direction.equals("NE")){
			oppositeDirection = "SW";
		}else if(direction.equals("E")){
			oppositeDirection = "W";
		}else if(direction.equals("SE")){
			oppositeDirection = "NW";
		}else if(direction.equals("S")){
			oppositeDirection = "N";
		}else if(direction.equals("SW")){
			oppositeDirection = "NE";
		}else if(direction.equals("W")){
			oppositeDirection = "E";
		}else if(direction.equals("NW")){
			oppositeDirection = "SE";
		}
		
		if(oppositeDirection.equals(waveManager.direction)){		
			return true;
		}else{
			return false;
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

