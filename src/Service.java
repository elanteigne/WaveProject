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
			
			String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.heading+"/"+waveManager.speed[0]+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude+"/"+data;
			
			/**Testing**/
			//CarBehind
			//String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.heading+"/"+60+"/"+45.38214+"/"+-75.68781+"/"+data;
			//CarAhead
			//String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.heading+"/"+60+"/"+45.38286+"/"+-75.68836+"/"+data;
			
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			System.out.println(message.getBytes().length);
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
		}catch(Exception e){ }
	}
	
	public double calculateDistance(double lat1, double lon1) {
		double theta = deg2rad(lon1) - deg2rad(waveManager.GPSlongitude);
		double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(waveManager.GPSlattitude)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(waveManager.GPSlattitude)) * Math.cos(theta);
		distance = Math.acos(distance)* 6372795.477598;
		
		return distance;
	}
	
	public double compareBearing(int heading, double lat2, double lon2) {
		double	dlonr = deg2rad(lon2) - deg2rad(waveManager.GPSlongitude);
		double	y = Math.sin(dlonr) * Math.cos(deg2rad(lat2));
		double	x = Math.cos(deg2rad(waveManager.GPSlattitude))*Math.sin(deg2rad(lat2)) - Math.sin(deg2rad(waveManager.GPSlattitude)) * Math.cos(deg2rad(lat2))*Math.cos(dlonr);

		double	bearing = rad2deg(Math.atan2(y, x));
		
		if(waveManager.heading>180){
			bearing += (360-waveManager.heading);
		}else{
			bearing += waveManager.heading;
		}
		
		if(bearing<0){
			bearing+=360;
		}else if(bearing>360){
			bearing-=360;
		}
		return bearing;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	public boolean checkIfAhead(int heading, double lat1, double lon1) {
		//Should ideally check if on the same road but can't without a maps API
		double bearing = compareBearing(heading, lat1, lon1);
		if(bearing<15 || bearing>345){		
			if(heading<waveManager.heading+10 && heading>waveManager.heading-10){
				return true;
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
	public boolean checkIfBehind(int heading, double lat1, double lon1) {
		//Should ideally check if on the same road but can't without a maps API
		double bearing = compareBearing(heading, lat1, lon1);
		if(bearing>170 && bearing<190){		
			if(heading<waveManager.heading+10 && heading>waveManager.heading-10){
				return true;
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
	public boolean checkIfOncoming(int heading, double lat1, double lon1) {
		double bearing = compareBearing(heading, lat1, lon1);
		if(bearing<10 && bearing>350){		
			if(heading<waveManager.heading+170 && heading>waveManager.heading-170){
				return true;
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
}
