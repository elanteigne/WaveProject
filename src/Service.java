import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Service {
	//Object
	public WaveManager waveManager;
	private MulticastSocket sendingProcess;
	
	//Constructor
	public Service(WaveManager waveManager){
		this.waveManager=waveManager;
		try{
			sendingProcess = new MulticastSocket();
		}catch(Exception e){ }
	}
	
	//Class Methods
	public void sendMessage(String fromGroup, String toGroup, int messageID, String data){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(fromGroup);
			
			int hopCount = 0;
			
			//String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.bearing+"/"+waveManager.speed+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude+"/"+data;
			
			/**Testing**/
			//General & Braking 
			String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.bearing+"/"+60+"/"+45.3476235+"/"+-73.6597858+"/"+data;
			
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
						
			//Send packet
			sendingProcess.send(packet);
			
			String packetType = "";
			if(fromGroup.equals(toGroup)){
				packetType = "Service";
			}else{
				packetType = "Control";
			}
			
			String output = "-> Sent "+packetType+" message to "+fromGroup+": "+message;
			waveManager.userInterface.output(output);
			System.out.println(output);
		}catch(Exception e){ }
	}
	
	public double calculateDistance(double lat1, double lon1) {
		double theta = lon1 - waveManager.GPSlongitude;
		double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(waveManager.GPSlattitude)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(waveManager.GPSlattitude)) * Math.cos(deg2rad(theta));
		distance = Math.acos(distance);
		distance = rad2deg(distance);
		distance = distance * 1609.344;

		return distance;
	}
	
	public double compareBearing(double lat1, double lon1) {
		double	dlonr = deg2rad(waveManager.GPSlongitude) - deg2rad(lon1);
		double	y = Math.sin(dlonr) * Math.cos(deg2rad(waveManager.GPSlattitude));
		double	x = Math.cos(deg2rad(lat1))*Math.sin(deg2rad(waveManager.GPSlattitude)) - Math.sin(deg2rad(lat1)) * Math.cos(deg2rad(waveManager.GPSlattitude))*Math.cos(dlonr);

		double	bearing = rad2deg(Math.atan2(y, x));

		return bearing;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	public boolean checkIfAhead(double lat1, double lon1) {
		//Should ideally check if on the same road but can't without a maps API
		double bearing = compareBearing(lat1, lon1);
		if(bearing<10 && bearing>150){		
			return true;
		}else{		
			return false;
		}
	}
	
	public boolean checkIfBehind(double lat1, double lon1) {
		//Should ideally check if on the same road but can't without a maps API
		double bearing = compareBearing(lat1, lon1);
		if(bearing>170 && bearing<190){		
			return true;
		}else{		
			return false;
		}
	}
}