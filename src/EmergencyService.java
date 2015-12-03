import java.util.concurrent.TimeUnit;

public class EmergencyService extends Service implements Runnable {
	//Class Variables
	private Thread emergencyServiceThread;
	
	//Resources
	public int delay = 500;
	public String serviceGroup = "230.0.0.3";
	
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
		sendMessage("Control", waveManager.controlGroup, serviceGroup, "0/0");
	}
	
	public void sendServiceMessage(){
		sendMessage("Service", serviceGroup, serviceGroup, "");
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
