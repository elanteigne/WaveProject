import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver {
	//Class Variables
	private WaveManager waveManager;
	private BreakService breakService;
	private MulticastSocket listener;
	
	//Resources
	private String currentGroup;
	
	//Constructor
	public Receiver(WaveManager waveManager, BreakService breakService){
		this.waveManager=waveManager;
		this.breakService=breakService;
		try{
			listener = new MulticastSocket(waveManager.port);			
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
			System.out.println("Switched to group "+currentGroup);
			
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
			
			String fromCarID = strings[0];
			String hopCount = strings[1];
			String messageGroup = strings[2];
			
			
			//Commented for testing purposes
			//if(!(strings[0].equals(waveManager.CarID))){
			if(fromCarID.equals(waveManager.CarID)){
				if(messageGroup.equals(waveManager.breakServiceGroup)&&currentGroup.equals(waveManager.breakServiceGroup)){
					System.out.println("Received message from CarID: "+fromCarID+" saying '"+strings[3]+"' from "+currentGroup+" with hopCount = "+hopCount);
					breakService.computeData(strings[3]);
				}else{
					System.out.println("Received message from CarID: "+fromCarID+" advertising '"+messageGroup+"'");
					
					//Once we have multiple cars, add group to a list of group to listen to
				}
			}else{
				System.out.println("Omitted own message");
			}
			
			//Will move to line 73 once we have multiple cars
			switchGroups(messageGroup);
			
		}catch(Exception e){
			
		}
	}
}
