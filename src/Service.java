import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Service {
	//Class Variables
	public WaveManager waveManager;
	private MulticastSocket sendingProcess;
	
	//Constructor
	public Service(WaveManager waveManager){
		this.waveManager=waveManager;
		try{
			sendingProcess = new MulticastSocket();
		}catch(Exception e){
			
		}
	}
	
	//Class Methods
	public void sendMessage(String packetType, String fromGroup, String toGroup, String data){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(fromGroup);
			
			int hopCount = 0;
			
			String message = waveManager.CarID+"/"+waveManager.messageIDglobal+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+data;
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			sendingProcess.send(packet);
			waveManager.messageIDglobal++;
			
			String output = "Sent "+packetType+" message to "+fromGroup+": "+message;
			System.out.println(output);
		}catch(Exception e){
			
		}
	}
}