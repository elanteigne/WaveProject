import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Service {
	//Class Variables7
	private WaveManager waveManager;
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
