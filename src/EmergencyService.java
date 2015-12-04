import java.util.concurrent.TimeUnit;

public class EmergencyService extends Service implements Runnable {
	//Class Variables
	private Thread emergencyServiceThread;
	
	//Resources
	public int delay = 5;
	public int messageID = 0;
	public String serviceGroup = "230.0.0.2";
	
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
		sendMessage("Control", messageID, waveManager.controlGroup, serviceGroup, "0/0");
		 messageID++;
	}
	
	public void sendServiceMessage(){
		sendMessage("Service", messageID, serviceGroup, serviceGroup, "");
		 messageID++;
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
	public void computeData(){
		System.out.println("There is an Emergency Vehicle approaching. Please be aware.");
	}
}
