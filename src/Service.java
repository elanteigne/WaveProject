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
	public void sendMessage(String packetType, int messageID, String fromGroup, String toGroup, String data){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(fromGroup);
			
			int hopCount = 0;
			
			String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+waveManager.speed+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude+"/"+data;
			
			/**Testing**/
			//Test shows traffic ahead being slower and car ahead braking
			//String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+60+"/"+45.3476235+"/"+-73.6597858+"/"+data;
			
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			sendingProcess.send(packet);
			
			String output = "-> Sent "+packetType+" message to "+fromGroup+": "+message;
			System.out.println(output);
		}catch(Exception e){ }
	}
	
	public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
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
	
	public boolean checkIfAhead(String direction){
		if(direction.equals(waveManager.direction)){		
			return true;
		}else{		
			return false;
		}
	}
	
	public boolean checkIfBehind(String direction){
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
}