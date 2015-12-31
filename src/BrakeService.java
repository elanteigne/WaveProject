import java.util.concurrent.TimeUnit;

public class BrakeService extends Service implements Runnable{
	//Objects
	private Thread brakeServiceThread;
	
	//Resources
	public int delay = 500;
	public String serviceGroup = "230.0.0.3";
	public int messageID = 0;
	
	//Constructor
	public BrakeService(WaveManager waveManager){
		super(waveManager);
	}

	//Class Methods
	public void start(){
		if(brakeServiceThread==null){
			brakeServiceThread = new Thread(this, "BreakService");
			brakeServiceThread.start();
		}
	}
	
	public void run(){
		while(waveManager.speed>10){
			if(checkBreak()){
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
	}
	
	public void sendControlMessage(){
		sendMessage("Control", messageID, waveManager.controlGroup, serviceGroup, "0/0");
		 messageID++;
	}
	
	public void sendServiceMessage(){
		sendMessage("Service", messageID, serviceGroup, serviceGroup, ""+waveManager.brakeAmount);
		 messageID++;
	}
	
	//The check to see if I send
	public boolean checkBreak(){
		if(waveManager.brakeAmount>5){			
			return true;
		}
		return false;
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(String data){
		int breakAmount = Integer.parseInt(data);
		
		if(breakAmount<25){
			waveManager.speedAdjustment = 5;
		}else if(breakAmount>25 && breakAmount<50){
			waveManager.speedAdjustment = 20;
		}else if(breakAmount>50 && breakAmount<75){
			waveManager.speedAdjustment = 40;		
		}else{
			waveManager.speedAdjustment = 60;				
		}
		
		System.out.println("Calculated: SpeedAdjustment = '"+waveManager.speedAdjustment+"'");
	}
}
