import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver implements Runnable{
	//Object
	private Thread receiverThread;
	private WaveManager waveManager;
	private GeneralInfoService generalInfoService;
	private BrakeService brakeService;
	private EmergencyService emergencyService;
	private MulticastSocket listener;
	private MulticastSocket passAlongProcess;
	
	//Resources
	public int timeout = 1;
	private String currentGroup;
	private int maxHopCount = 5;
	private String[] groupsToListenTo={"230.0.0.1", "", "", "", ""};
	private String[][] recentlyReceivedMessages={{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""},
												{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""},
												{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""},
												{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}};
	
	//Constructor
	public Receiver(WaveManager waveManager, GeneralInfoService generalInfoService, BrakeService brakeService, EmergencyService emergencyService){
		this.waveManager=waveManager;
		this.generalInfoService=generalInfoService;
		this.brakeService=brakeService;
		this.emergencyService=emergencyService;
		try{
			listener = new MulticastSocket(waveManager.port);			
			listener.joinGroup(InetAddress.getByName(waveManager.controlGroup));
			currentGroup = waveManager.controlGroup;
		}catch(Exception e){
			
		}
	}
	
	//Class Methods
	public void start(){
		if(receiverThread==null){
			receiverThread = new Thread(this, "Receiver");
			receiverThread.start();
		}
	}
	
	public void run(){
		while(true){
			for(int i=0; i<groupsToListenTo.length; i++){
				if(!(groupsToListenTo[i].equals(""))){
					switchGroups(groupsToListenTo[i]);
					int gotPacket = 0;
					while(gotPacket<2){
						getPacket();
						gotPacket++;
					}
				}
			}
		}
	}
	
	public void getPacket(){
		try{
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			try{
				listener.setSoTimeout(timeout);
				listener.receive(packet);
			}catch(Exception e){
				
			}
			
			byte[] message = packet.getData();
			int length = packet.getLength();
			
			String str = new String(message, 0, length);
			String[] strings = str.split("/");
			
			String fromCarID = strings[0];
			String messageID = strings[1];
			String fromGroup = strings[2];
			int hopCount = Integer.parseInt(strings[3]);
			String messageGroup = strings[4];
			String direction = strings[5];
			
			//Commented for testing purposes
			//if(!(strings[0].equals(waveManager.CarID))){
			if(fromCarID.equals(waveManager.CarID)){
				if(receivedMessagePreviously(fromCarID, messageID, messageGroup)){

					//The order of these is where PRIORITIES take place
					 if(fromGroup.equals(emergencyService.serviceGroup)){
						System.out.println("+ Received *EmergencyService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Sirens 'On', Direction:'"+direction+"', Speed:'"+strings[6]+"', HopCount = "+hopCount);
						emergencyService.computeData();
					}else if(fromGroup.equals(brakeService.serviceGroup)){
						System.out.println("+ Received *BrakeService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Speed:'"+strings[6]+"', BrakeAmount:'"+strings[7]+"', Direction:'"+direction+"', HopCount = "+hopCount);
						brakeService.computeData(strings[6]);
					}else if(fromGroup.equals(generalInfoService.serviceGroup)){
						System.out.println("+ Received *GeneralInfoService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Speed:'"+strings[6]+"', Lattitude:'"+strings[7]+"' Longitude:'"+strings[8]+"', Direction:'"+direction+"', HopCount = "+hopCount);
						
						//Either check if car is ahead here or check within compute data
						//Likely requires GPS and some math using GPS and direction
						//Check if car ahead is going slower or if car behind is going faster
						
						generalInfoService.computeData(direction, strings[6], strings[7], strings[8]);
					}else{

						System.out.println("+ Received *Control* message advertising '"+messageGroup+"' from CarID '"+fromCarID+"'");
						
						boolean alreadyListening = false;
						for(int i=0; i<groupsToListenTo.length; i++){
							if(groupsToListenTo[i].equals(messageGroup)){
								System.out.println("Group '"+messageGroup+"' is already in groupsToListenTo");
								alreadyListening = true;
							}
						}
						if(!alreadyListening){
							groupsToListenTo[1] = messageGroup;
							System.out.println("Added '"+messageGroup+"' to groupsToListenTo");
						}
					}
					
					//DECIDE IF CONTROL MESSAGES SHOULD BE PASSED
					if(hopCount < maxHopCount){
						passAlongMessage(fromCarID, fromGroup, messageID, hopCount, messageGroup, strings[5]);
					}
				}
			}else{
				System.out.println("X Omitted own message");
			}
			
		}catch(Exception e){
			
		}		
	}
	
	public void switchGroups(String group){
		try{
			listener.joinGroup(InetAddress.getByName(group));
		}catch(Exception e){
			
		}
		currentGroup = group;
	}

	private void passAlongMessage(String fromCarID, String fromGroup, String messageID, int hopCount, String messageGroup, String data){
		try{
			passAlongProcess = new MulticastSocket();
			
			hopCount++;
			
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(currentGroup);
			
			String message = fromCarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+messageGroup+"/"+waveManager.direction+"/"+data;
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			passAlongProcess.send(packet);
		
			System.out.println("->-> Passed messageID '"+messageID+"' along on "+fromGroup+" with hopCount '"+hopCount+"': "+message);
		}catch(Exception e){
			
		}
	}
	
	private boolean receivedMessagePreviously(String fromCarID, String messageID, String fromGroup){
		for(int i=0; i<8; i++){
			if(recentlyReceivedMessages[i][0].equals(fromCarID)&&recentlyReceivedMessages[i][1].equals(messageID)&&recentlyReceivedMessages[i][2].equals(fromGroup)){
				System.out.println("X Recently received message '"+messageID+"' from carID '"+fromCarID+"' on service channel '"+fromGroup+"'. Omit message");
				return false;
			}
		}
		
		for(int i=7; i>0; i--){
			recentlyReceivedMessages[i][0] = recentlyReceivedMessages[i-1][0];
			recentlyReceivedMessages[i][1] = recentlyReceivedMessages[i-1][1];
			recentlyReceivedMessages[i][2] = recentlyReceivedMessages[i-1][2];
		}
		recentlyReceivedMessages[0][0] = fromCarID;	
		recentlyReceivedMessages[0][1] = messageID;	
		recentlyReceivedMessages[0][2] = fromGroup;	
		return true;
	}
}
