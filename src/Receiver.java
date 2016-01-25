import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver implements Runnable{
	//Object
	private Thread receiverThread;
	private GeneralInfoService generalInfoService;
	private BrakeService brakeService;
	private EmergencyService emergencyService;
	private TrafficService trafficService;
	
	private MulticastSocket listener;
	private MulticastSocket passAlongProcess;
	public WaveManager waveManager;
	
	//Resources
	public int numPacketsReceived;
	public int numPacketsPassed;
	public int numPacketsOmitted;
	public int timeout = 1;
	private String currentGroup;
	private String output;
	private int maxHopCount = 5;
	private int numGroupsToListenTo = 0;
	private String[] groupsToListenTo={"230.0.0.1", "", "", "", ""};
	private String[][] recentlyReceivedMessages={{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""},
												{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""},
												{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""},
												{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}};
	
	//Constructor
	public Receiver(WaveManager waveManager, GeneralInfoService generalInfoService, BrakeService brakeService, EmergencyService emergencyService, TrafficService trafficService){
		this.waveManager=waveManager;
		this.generalInfoService=generalInfoService;
		this.brakeService=brakeService;
		this.emergencyService=emergencyService;
		this.trafficService=trafficService;
		
		numPacketsReceived = 0;
		numPacketsPassed = 0;
		numPacketsOmitted = 0;
		
		try{
			listener = new MulticastSocket(waveManager.port);			
			listener.joinGroup(InetAddress.getByName(waveManager.controlGroup));
			currentGroup = waveManager.controlGroup;
		}catch(Exception e){ }
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
				numPacketsReceived++;
				waveManager.userInterface.updateNumPacketsReceived(numPacketsReceived);
			}catch(Exception e){ }
			
			byte[] message = packet.getData();
			int length = packet.getLength();
			
			String str = new String(message, 0, length);
			String[] strings = str.split("/");
			
			String fromCarID = strings[0];
			String messageID = strings[1];
			String fromGroup = strings[2];
			int hopCount = Integer.parseInt(strings[3]);
			String messageGroup = strings[4];
			int heading = Integer.parseInt(strings[5]);
			int vehicleSpeed = Integer.parseInt(strings[6]);
			double vehicleLattitude = Double.parseDouble(strings[7]);
			double vehicleLongitude = Double.parseDouble(strings[8]);
			
			//Commented for testing purposes
			//if(!(strings[0].equals(waveManager.CarID))){
			if(fromCarID.equals(waveManager.CarID)){
				if(receivedMessagePreviously(fromCarID, messageID, messageGroup)){
					
					//The order of these is where PRIORITIES take place
					if(fromGroup.equals(emergencyService.serviceGroup)){
						System.out.println("+ Received *EmergencyService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Sirens 'On', Heading:'"+heading+"', Speed: "+vehicleSpeed+" km/h, HopCount = "+hopCount);
						output = "+ Received *EmergencyService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Sirens 'On', Heading:'"+heading+"', Speed: "+vehicleSpeed+" km/h, HopCount = "+hopCount;
						waveManager.userInterface.output(output);
								
						emergencyService.computeData(vehicleLattitude, vehicleLongitude);
					}else if(fromGroup.equals(brakeService.serviceGroup)){
						int brakeAmount = Integer.parseInt(strings[9]);
						
						System.out.println("+ Received *BrakeService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Speed: "+vehicleSpeed+" km/h, BrakeAmount: "+brakeAmount+"%, Lattitude:'"+vehicleLattitude+"' Longitude:'"+vehicleLongitude+"', Heading:'"+heading+"', HopCount = "+hopCount);
						output = "+ Received *BrakeService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Speed: "+vehicleSpeed+" km/h, BrakeAmount: "+brakeAmount+"%, Lattitude:'"+vehicleLattitude+"' Longitude:'"+vehicleLongitude+"', Heading:'"+heading+"', HopCount = "+hopCount;
						waveManager.userInterface.output(output);
						
						brakeService.computeData(heading, vehicleSpeed, vehicleLattitude, vehicleLongitude, brakeAmount);
					}else if(fromGroup.equals(generalInfoService.serviceGroup)){
						System.out.println("+ Received *GeneralInfoService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Speed:'"+vehicleSpeed+" km/h, Lattitude:'"+vehicleLattitude+"' Longitude:'"+vehicleLongitude+"', Heading:'"+heading+"', HopCount = "+hopCount);
						output = "+ Received *GeneralInfoService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': Speed:'"+vehicleSpeed+" km/h, Lattitude:'"+vehicleLattitude+"' Longitude:'"+vehicleLongitude+"', Heading:'"+heading+"', HopCount = "+hopCount;
						waveManager.userInterface.output(output);
											
						generalInfoService.computeData(fromCarID, heading, vehicleSpeed, vehicleLattitude, vehicleLongitude);
					}else if(fromGroup.equals(trafficService.serviceGroup)){
						int trafficLevel = Integer.parseInt(strings[9]);
						
						System.out.println("+ Received *TrafficService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': TrafficLevel:'"+trafficLevel+"': Speed:'"+vehicleSpeed+" km/h, Lattitude:'"+vehicleLattitude+"' Longitude:'"+vehicleLongitude+"', Heading:'"+heading+"', HopCount = "+hopCount);
						output = "+ Received *TrafficService* messageID '"+messageID+"' from CarID:'"+fromCarID+"': TrafficLevel:'"+trafficLevel+"': Speed:'"+vehicleSpeed+" km/h, Lattitude:'"+vehicleLattitude+"' Longitude:'"+vehicleLongitude+"', Heading:'"+heading+"', HopCount = "+hopCount;
						waveManager.userInterface.output(output);
											
						//trafficService.computeData(trafficLevel);
						//Need: carID, bearing, speed, may be extended to gps coor for distance to traffic calculation
						
					}else{
						System.out.println("+ Received *Control* message advertising '"+messageGroup+"' from CarID '"+fromCarID+"'");
						output = "+ Received *Control* message advertising '"+messageGroup+"' from CarID '"+fromCarID+"'";
						waveManager.userInterface.output(output);
						
						boolean alreadyListening = false;
						for(int i=0; i<groupsToListenTo.length; i++){
							if(groupsToListenTo[i].equals(messageGroup)){
								System.out.println("Group '"+messageGroup+"' is already in groupsToListenTo");
								output = "Group '"+messageGroup+"' is already in groupsToListenTo";
								waveManager.userInterface.output(output);
								alreadyListening = true;
							}
						}
						if(!alreadyListening){
							numGroupsToListenTo++;
							groupsToListenTo[numGroupsToListenTo] = messageGroup;
							
							System.out.println("Added '"+messageGroup+"' to groupsToListenTo");
							output = "Added '"+messageGroup+"' to groupsToListenTo";
							waveManager.userInterface.output(output);
						}
					}

					//DECIDE IF CONTROL MESSAGES SHOULD BE PASSED HERE
					if(hopCount < maxHopCount){
						if(fromGroup.equals(brakeService.serviceGroup)){
							passAlongMessage(fromCarID, fromGroup, messageID, hopCount, messageGroup, heading, vehicleSpeed, vehicleLattitude, vehicleLongitude, strings[9]);
						}else{
							passAlongMessage(fromCarID, fromGroup, messageID, hopCount, messageGroup, heading, vehicleSpeed, vehicleLattitude, vehicleLongitude, "");
						}
					}
				}
			}else{
				numPacketsOmitted++;
				waveManager.userInterface.updateNumPacketsOmitted(numPacketsOmitted);
				
				System.out.println("X Omitted own message");
				output = "X Omitted own message";
				waveManager.userInterface.output(output);
			}
		}catch(Exception e){ }		
	}
	
	public void switchGroups(String group){
		try{
			listener.joinGroup(InetAddress.getByName(group));
		}catch(Exception e){
			
		}
		currentGroup = group;
	}

	private void passAlongMessage(String fromCarID, String fromGroup, String messageID, int hopCount, String messageGroup, int heading,  int vehicleSpeed, double vehicleLattitude, double vehicleLongitudeString, String data){
		try{
			passAlongProcess = new MulticastSocket();
			
			hopCount++;
			
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(currentGroup);
			
			String message = fromCarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+messageGroup+"/"+heading+"/"+vehicleSpeed+"/"+vehicleLattitude+"/"+vehicleLongitudeString+"/"+data;
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			passAlongProcess.send(packet);

			numPacketsPassed++;
			waveManager.userInterface.updateNumPacketsPassed(numPacketsPassed);
			
			System.out.println("->-> Passed messageID '"+messageID+"' along on "+fromGroup+" with hopCount '"+hopCount+"': "+message);
			output = "->-> Passed messageID '"+messageID+"' along on "+fromGroup+" with hopCount '"+hopCount+"': "+message;
			waveManager.userInterface.output(output);
		}catch(Exception e){ }
	}
	
	private boolean receivedMessagePreviously(String fromCarID, String messageID, String fromGroup){
		for(int i=0; i<8; i++){
			if(recentlyReceivedMessages[i][0].equals(fromCarID)&&recentlyReceivedMessages[i][1].equals(messageID)&&recentlyReceivedMessages[i][2].equals(fromGroup)){
				numPacketsOmitted++;
				waveManager.userInterface.updateNumPacketsOmitted(numPacketsOmitted);
				
				System.out.println("X Recently received message '"+messageID+"' from carID '"+fromCarID+"' on service channel '"+fromGroup+"'. Omit message");
				output = "X Recently received message '"+messageID+"' from carID '"+fromCarID+"' on service channel '"+fromGroup+"'. Omit message";
				waveManager.userInterface.output(output);
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
