import java.net.DatagramPacket;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Service {
	//Object
	public WaveManager waveManager;
	private MulticastSocket sendingProcess;
	private int[] serviceVariables = new int[]{5,4,2,2,1,1,3,3,8,136}; 
	//private int[] brakeVariables = new int[]{0,1,2,3,4,5,6,7,8,9,10}; 
	//private int[] serviceVariables = new int[]{0,1,2,3,4,5,6,7,8,9,10}; 
	
	//Constructor
	public Service(WaveManager waveManager){
		this.waveManager=waveManager;
		try{
			sendingProcess = new MulticastSocket();
		}catch(Exception e){ }
	}
	
	//Class Methods
<<<<<<< HEAD
	
	//SENDER
	
	public void sendMessage(String type, String serviceGroup, String message){
		try{ //waveManager.CarID+"/"+serviceGroup+"/"+direction+"/"+breakAmount
			
			String[] msg = message.split("/");
			
			List<Boolean> packetInfo =new ArrayList<Boolean>();
			
			System.out.println("Msg: "+msg[0]+" "+msg[1]+" "+msg[2]);
			System.out.println("L1: " + msg[0].length());
			System.out.println("Serv: " + serviceGroup);
			for(int i =0; i<msg.length + 10; i++){
				
					if(i>=10){
						if(serviceGroup.equals("230.0.0.2")){
							//Integer.parseInt("1234");
								boolean[] info = intToBitsAndCut(Integer.parseInt(msg[i]), Integer.parseInt(mLength[i]));
								System.out.println("Test"+info);
								packetInfo = appendPacket(packetInfo, info, Integer.parseInt(mLength[i]));
						}
					}
						
					else if(i<10){
						
							if(i==0){
								boolean[] info = intToBitsAndCut(waveManager.channel, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
								System.out.println("Test"+info+"Test");
							};
							if(i==1){
								boolean[] info = intToBitsAndCut(waveManager.service, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==2){
								int d;
								if(waveManager.direction.equals("N")){d = 0;}else{d=0;};
								boolean[] info = intToBitsAndCut(d, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==3){
								boolean[] info = intToBitsAndCut(waveManager.lights, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==4){
								boolean[] info = intToBitsAndCut(waveManager.turnSignals, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==5){
								boolean[] info = intToBitsAndCut(waveManager.breaks, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							
							if(i==6){
								boolean[] info = intToBitsAndCut(waveManager.eBreak, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==7){
								boolean[] info = intToBitsAndCut(waveManager.emergency, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==8){
								boolean[] info = intToBitsAndCut(waveManager.vType, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
							if(i==9){
								boolean[] info = intToBitsAndCut(waveManager.speed, serviceVariables[i]);
								packetInfo = appendPacket(packetInfo, info, serviceVariables[i]);
							};
					}else{
						
					}
			};
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(serviceGroup);
			//DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			byte[] pInfo = toByteArray(packetInfo);
			DatagramPacket packet = new DatagramPacket(pInfo, pInfo.length, InetDestination, waveManager.port);
			//Send packet
			sendingProcess.send(packet);
			
			//String output = "Sent "+type+" message to "+serviceGroup+": "+message;
			String output = "Sent "+type+" message to "+serviceGroup+": "+pInfo;
			System.out.println("Original msg:" + message);
			System.out.println(output);
			
		}catch(Exception e){
			
		}
	}
	
	public static byte[] toByteArray(List<Boolean> packetAL) {
		boolean[] packet = new boolean[packetAL.size()];
		//Must use loop because were using boolean, not Boolean for 'packet'
		//normally, would use x = y.toArray();
		for (int i = 0; i < packetAL.size(); i++) {
			packet[i] = packetAL.get(i);
			}
		
	    byte[] WSApacket = new byte[packet.length / 8];
	    	for (int entry = 0; entry < WSApacket.length; entry++) {
	    		for (int bit = 0; bit < 8; bit++) {
	    			if (packet[entry * 8 + bit]) {
	    				WSApacket[entry] |= (128 >> bit);
	    			}
	    		}
	    	}
	    return WSApacket;
	} 
	
	public static boolean[] intToBitsAndCut(int n, int size){
		final boolean[] b = new boolean[size];
		    for (int i = 0; i < size; i++) {
		        b[size - 1 - i] = (1 << i & n) != 0;
		    	}
		    return b;
		}
	
	public static List<Boolean> appendPacket(List<Boolean> packet, boolean[] added, int size){
			for(int i = 0; i<size; i++){
				packet.add(added[i]);
				}
		return packet;
		}
}
=======
	public void sendMessage(String packetType, int messageID, String fromGroup, String toGroup, String data){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(fromGroup);
			
			int hopCount = 0;
			
			String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+waveManager.speed+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude+"/"+data;
			
			/**Testing**/
			//General & Braking 
			//String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+60+"/"+45.3476235+"/"+-73.6597858+"/"+data;
			
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			sendingProcess.send(packet);
			
			String output = "-> Sent "+packetType+" message to "+fromGroup+": "+message;
			waveManager.userInterface.output(output);
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
>>>>>>> refs/remotes/origin/master
