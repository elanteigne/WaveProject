import java.util.concurrent.TimeUnit;

public class EmergencyService extends Service implements Runnable {
	//Class Variables
	private Thread emergencyServiceThread;
	
	//Resources
	public int delay = 500;
	public int messageID = 0;
	public String serviceGroup = "230.0.0.4";
	private String output;
	
	//Constructor
	public EmergencyService(WaveManager waveManager){
		super(waveManager);
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
	
	//The check to see if I send
	public boolean checkSiren(){
		if(waveManager.sirensOn){
			return true;
		}else{
			return false;
		}
	}
	
	public void run(){
		while(checkSiren()){
			sendControlMessage();
			//Wait 
			try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
			
			int count = 0;
			while(count<5){
				sendServiceMessage();
				
				//Wait
				try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
				count++;
			}
		}
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(double vehicleLattitude, double vehicleLongitude){
		double distance = calculateDistance(vehicleLattitude, vehicleLongitude);
		
		System.out.println("o Calculated: Emergency Vehicle approaching ("+distance+"m). Please be aware.");
		output = "o Calculated: Emergency Vehicle approaching ("+distance+"m). Please be aware.";
		waveManager.userInterface.output(output);
	}
}
