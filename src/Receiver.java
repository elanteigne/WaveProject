import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver {
	//Class Variables
	private WaveManager waveManager;
	private BreakService breakService;
	private MulticastSocket listener;
	
	//Resources
	private MulticastSocket passAlongProcess;
	private String currentGroup;
	private int maxHopCount = 5;
	private String[] recentlyReceivedMessages= {"", "", "", "", "", "", "", "", "", ""};
	
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
			String messageID = strings[1];
			int hopCount = Integer.parseInt(strings[2]);
			String messageGroup = strings[3];
			String direction = strings[4];
			
			//Commented for testing purposes
			//if(!(strings[0].equals(waveManager.CarID))){
			if(fromCarID.equals(waveManager.CarID)){
				
				if(receivedMessagePreviously(messageID)){
					
					if(messageGroup.equals(breakService.serviceGroup)&&currentGroup.equals(breakService.serviceGroup)){
						System.out.println("Received messageID '"+messageID+"' from CarID '"+fromCarID+"' going direction '"+direction+"' saying '"+strings[5]+"' from "+currentGroup+" with hopCount = "+hopCount);
						breakService.computeData(strings[5]);

					}else{
						System.out.println("Received messageID '"+messageID+"' from CarID '"+fromCarID+"' advertising '"+messageGroup+"'");
						//Once we have multiple cars, add group to a list of group to listen to and switch groups there
					}
					
					if(hopCount < maxHopCount){
						passAlongMessage(fromCarID, messageID, hopCount, strings[5]);
					}
				}
			}else{
				System.out.println("Omitted own message");
			}
			
			//Once we have more services the switch group sound be done cyclicly
			switchGroups(messageGroup);
			
		}catch(Exception e){
			
		}		
	}

	private void passAlongMessage(String fromCarID, String messageID, int hopCount, String data){
		try{
			passAlongProcess = new MulticastSocket();
			
			hopCount++;
			
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(currentGroup);
			
			String message = fromCarID+"/"+messageID+"/"+hopCount+"/"+currentGroup+"/"+waveManager.direction+"/"+data;
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			passAlongProcess.send(packet);
		
			System.out.println("Passed messageID '"+messageID+"' along on "+currentGroup+" with hopCount '"+hopCount+"': "+message);
		}catch(Exception e){
			
		}
	}
	
	public void switchGroups(String group){
		try{		
			listener.joinGroup(InetAddress.getByName(group));
			currentGroup = group;
			System.out.println("Switched to group "+currentGroup);
		}catch(Exception e){
			
		}
	}
	
	private boolean receivedMessagePreviously(String messageID){
		for(int i=0; i<10; i++){
			if(recentlyReceivedMessages[i].equals(messageID)){
				System.out.println("Message '"+messageID+"' has recently been received. Omit message");
				return false;
			}
		}
		
		for(int i=9; i>0; i--){
			recentlyReceivedMessages[i] = recentlyReceivedMessages[i-1];
		}
		recentlyReceivedMessages[0] = messageID;	
		return true;
	}
}
