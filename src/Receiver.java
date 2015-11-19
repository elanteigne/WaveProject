import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver {
	//Class Variables
	private MulticastSocket listener;
	
	//MyInfo
	private String carID;
	private int port = 2222;
	
	//Resources
	private String currentGroup;
	
	//Constructor
	public Receiver(String carID, String controlGroup){
		this.carID=carID;
		try{
			listener = new MulticastSocket(port);			
			listener.joinGroup(InetAddress.getByName(controlGroup));
			currentGroup = controlGroup;
		}catch(Exception e){
			
		}
	}
	
	//Class Methods
	public void switchGroups(String group){
		try{		
			listener.joinGroup(InetAddress.getByName(group));
			currentGroup = group;
			
		}catch(Exception e){
			
		}
	}
	
	public void getPacket(){
		try{
			
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			listener.receive(packet);
			
			byte[] message = packet.getData();
			int length = packet.getLength();
			
			String str = new String(message, 0, length);
			String[] strings = str.split("/");
			
			System.out.println("Received message from CarID: "+strings[1]+" saying '"+strings[3]+"' from "+currentGroup);
			
			switchGroups(strings[0]);
		}catch(Exception e){
			
		}
	}
}
