import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public abstract class Service implements Runnable{
	//Object
	protected WaveManager waveManager;
	private MulticastSocket sendingProcess;
	protected Thread serviceThread;
	protected String threadName;
	protected int delay;
	protected int controlMsgDelay,serviceMsgDelay;
	protected int messageID;
	protected String serviceGroup;
	protected String output;
	protected Runnable runAlgorithm;
	protected boolean runCondition1, runCondition2;
	abstract void sendControlMessage();
	abstract void sendServiceMessage();
	abstract void updateRunConditions();
	abstract void updateMsgDelays();
    abstract void computeData(ArrayList<Object> params);

	//Constructor
	public Service(WaveManager waveManager){
		this.waveManager=waveManager;
		try{
			sendingProcess = new MulticastSocket();
		}catch(Exception e){ }
		messageID = 0;
	}
	

	public void run(){
		while(true){
			updateRunConditions();
			updateMsgDelays();
			if(this.runCondition1){
				sendControlMessage(); 
				//Delay before switching to Service interval 
				try{ TimeUnit.MILLISECONDS.sleep(controlMsgDelay); } catch(Exception e){ }
				for(int count=0;count<5;count++){
					if(this.runCondition2){
						sendServiceMessage();
						//Delay before switching to Control interval
						try{ TimeUnit.MILLISECONDS.sleep(serviceMsgDelay); } catch(Exception e){ }
					}
				}
			}
		}
	}

	public void start(){
		if(serviceThread==null){
			serviceThread = new Thread(runAlgorithm, threadName);
			serviceThread.start();
		}
	}

	//Class Methods
	public final void sendMessage(String fromGroup, String toGroup, int messageID, String data){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(fromGroup);

			int hopCount = 0;

			String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.heading+"/"+waveManager.speed[0]+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude+"/"+data;

			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			//Send packet
			sendingProcess.send(packet);

			String packetType = "";
			if(fromGroup.equals(toGroup)){
				packetType = "Service";
			}else{
				packetType = "Control";
			}

			String output = "-> Sent "+packetType+" message to "+fromGroup+": "+message;
			waveManager.userInterface.output(output);
		}catch(Exception e){ }
	}

	public final double calculateDistance(double lat1, double lon1) {
		double theta = deg2rad(lon1) - deg2rad(waveManager.GPSlongitude);
		double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(waveManager.GPSlattitude)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(waveManager.GPSlattitude)) * Math.cos(theta);
		distance = Math.acos(distance)* 6372795.477598;

		return distance;
	}

	public final double compareBearing(int heading, double lat2, double lon2) {
		double	dlonr = deg2rad(lon2) - deg2rad(waveManager.GPSlongitude);
		double	y = Math.sin(dlonr) * Math.cos(deg2rad(lat2));
		double	x = Math.cos(deg2rad(waveManager.GPSlattitude))*Math.sin(deg2rad(lat2)) - Math.sin(deg2rad(waveManager.GPSlattitude)) * Math.cos(deg2rad(lat2))*Math.cos(dlonr);

		double	bearing = rad2deg(Math.atan2(y, x));

		if(waveManager.heading>180){
			bearing += (360-waveManager.heading);
		}else{
			bearing += waveManager.heading;
		}

		if(bearing<0){
			bearing+=360;
		}else if(bearing>360){
			bearing-=360;
		}
		return bearing;
	}

	private final double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private final double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public final boolean checkIfAhead(int heading, double lat1, double lon1) {
		//Should ideally check if on the same road but can't without a maps API
		
		double bearing = compareBearing(heading, lat1, lon1);
		if(bearing<15 || bearing>345){		
			if(heading<waveManager.heading+10 && heading>waveManager.heading-10){
				return true;
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}

	public final boolean checkIfBehind(int heading, double lat1, double lon1) {
		//Should ideally check if on the same road but can't without a maps API
		double bearing = compareBearing(heading, lat1, lon1);
		if(bearing>170 && bearing<190){
			if(heading<waveManager.heading+10 && heading>waveManager.heading-10){
				return true;
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}

	public final boolean checkIfOncoming(int heading, double lat1, double lon1) {
		double bearing = compareBearing(heading, lat1, lon1);
		if(bearing<10 && bearing>350){		
			if(heading<waveManager.heading+170 && heading>waveManager.heading-170){
				return true;
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
}
