
public class BreakService extends Service{
	//Class Variables
	public String serviceGroup = "230.0.0.2";
	
	//Constructor
	public BreakService(WaveManager waveManager){
		super(waveManager);
	}

	//Class Methods
	public void sendControlMessage(){
		sendMessage("Control", waveManager.controlGroup, serviceGroup, "0/0");
	}
	
	public void sendServiceMessage(){
		sendMessage("Service", serviceGroup, serviceGroup, ""+waveManager.breakAmount);
	}
	
	//The check to see if I send
	public boolean checkBreak(){
		if(waveManager.breakAmount>5){			
			return true;
		}
		return false;
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(String data){
		int breakAmount = Integer.parseInt(data);
		
		if(breakAmount<25){
			waveManager.speedAdjustment = 5;
		}else if(breakAmount<50){
			waveManager.speedAdjustment = 20;				
		}else if(breakAmount<75){
			waveManager.speedAdjustment = 40;				
		}else{
			waveManager.speedAdjustment = 60;				
		}
		//System.out.println("Calculated: SpeedAdjustment = '"+waveManager.speedAdjustment+"'");
	}
}
