import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Service {
	//Class Variables
	private WaveManager waveManager;
	private MulticastSocket sendingProcess;
	private String controlGroup;
	private String serviceGroup;
	//Constructor
	public Service(WaveManager waveManager){
		this.waveManager=waveManager;
		this.controlGroup = waveManager.controlGroup;

		try{
			sendingProcess = new MulticastSocket();
		}catch(Exception e){
			
		}
	}
	
	//Class Methods
	public void sendMessage(String type, String serviceGroup, String message){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(serviceGroup);
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			sendingProcess.send(packet);
			
			String output = "Sent "+type+" message to "+serviceGroup+": "+message;
			System.out.println(output);
		}catch(Exception e){
			
		}
	}
}
