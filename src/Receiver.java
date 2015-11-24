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
			
			if(!(strings[0].equals(waveManager.CarID))){
				if(strings[1].equals(waveManager.breakServiceGroup)&&currentGroup.equals(waveManager.breakServiceGroup)){
					System.out.println("Received message from CarID: "+strings[0]+" saying '"+strings[3]+"' from "+currentGroup);
					breakService.computeData(Integer.parseInt(strings[3]));
				}else{
					System.out.println("Received message from CarID: "+strings[0]+" advertising '"+strings[1]+"'");
				}
			}else{
				System.out.println("Omitted own message");
			}
			switchGroups(strings[1]);
			
		}catch(Exception e){
			
		}
	}
}
