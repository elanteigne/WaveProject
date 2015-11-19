import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver {
	//Class Variables
	private WaveManager waveManager;
	private MulticastSocket listener;
	
	//MyInfo
	private int port = 2222;
	
	//Resources
	private String currentGroup;
	
	//Constructor
	public Receiver(WaveManager waveManager){
		this.waveManager=waveManager;
		try{
			listener = new MulticastSocket(port);			
			listener.joinGroup(InetAddress.getByName(waveManager.controlGroup));
			currentGroup = waveManager.controlGroup;
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
			
			System.out.println("Received message from CarID: "+strings[0]+" saying '"+strings[3]+"' from "+currentGroup);
			
			switchGroups(strings[1]);
		}catch(Exception e){
			
		}
	}
}
