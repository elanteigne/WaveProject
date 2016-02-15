import java.util.concurrent.TimeUnit;

public class EmergencyService extends Service implements Runnable {
	//Class Variables
	private Thread emergencyServiceThread;
	
	//Resources
	public int delay;
	public int messageID = 0;
	public String serviceGroup = "230.0.0.4";
	private String output;
	
	//Constructor
	public EmergencyService(WaveManager waveManager){
		super(waveManager);
		delay = waveManager.delay;
	}
	
	//Class Methods
	public void start(){
		if(emergencyServiceThread==null){
			emergencyServiceThread = new Thread(this, "EmergencyService");
			emergencyServiceThread.start();
		}
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		 messageID++;
		 waveManager.userInterface.updateEmergencyServicePacketsSent(messageID);
	}
	
	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, "");
		 messageID++;
		 waveManager.userInterface.updateEmergencyServicePacketsSent(messageID);
	}
		
	public void run(){
		while(true){
			delay = waveManager.delay;
			System.out.println(""+waveManager.sirensOn);
			if(waveManager.sirensOn){
				sendControlMessage();
				//Wait 
				try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
				
				int count = 0;
				while(count<5){
					sendServiceMessage();

					delay = waveManager.delay;
					//Wait
					try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
					count++;
				}
			}
		}
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(int heading, double vehicleLattitude, double vehicleLongitude){
		if(checkIfBehind(heading, vehicleLattitude, vehicleLongitude)){
			double distance = calculateDistance(vehicleLattitude, vehicleLongitude);
			
			output = "o Calculated: Emergency Vehicle approaching ("+(int)distance+"m). Please be aware.";
			System.out.println(output);
			waveManager.userInterface.computedEmergencyInfo(output);
		}else{
			output = "o Calculated: Emergency Vehicle nearby but not behind you. Please be aware.";
			System.out.println(output);
			waveManager.userInterface.computedEmergencyInfo(output);
		}
	}
}
