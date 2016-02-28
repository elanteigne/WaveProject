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
			if(waveManager.getSiren()){
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
		double distance = calculateDistance(vehicleLattitude, vehicleLongitude);
		if(distance<150){
			if(checkIfBehind(heading, vehicleLattitude, vehicleLongitude)){
				
				output = "o Calculated: Emergency Vehicle approaching behind ("+(int)distance+"m). Please be aware.";
				waveManager.userInterface.computedEmergencyInfo(output);
				waveManager.userInterface.turnOnEmergencySiren((int)distance);
			}else{
				output = "o Calculated: Emergency Vehicle nearby ("+(int)distance+"m). Please be aware.";
				waveManager.userInterface.computedEmergencyInfo(output);
				waveManager.userInterface.turnOnEmergencySiren((int)distance);
			}
		}
	}
}
