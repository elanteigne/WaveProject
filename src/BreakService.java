
public class BreakService extends Service{
	//Class Variables
	private WaveManager waveManager;
	
	//Resources
	private String controlGroup;
	private String serviceGroup;
	
	//Constructor
	public BreakService(WaveManager waveManager){
		super(waveManager);
		this.waveManager = waveManager;
		this.controlGroup = waveManager.controlGroup;
	}

	//Class Methods
	public void sendControlMessage(String serviceGroup){
		sendMessage("Control", controlGroup, serviceGroup, "0/0");
		this.serviceGroup = serviceGroup;
	}
	
	public void sendServiceMessage(int breakAmount, String direction){
		sendMessage("Service", serviceGroup, serviceGroup, ""+breakAmount);
	}
	
	//The check to see if I send
	public boolean checkBreak(int breakAmount){
		if(breakAmount>5){			
			return true;
		}
		return false;
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(int breakAmount){
		if(breakAmount<25){
			waveManager.speedAdjustment = 5;
		}else if(breakAmount<50){
			waveManager.speedAdjustment = 20;				
		}else if(breakAmount<75){
			waveManager.speedAdjustment = 40;				
		}else{
			waveManager.speedAdjustment = 60;				
		}
		System.out.println("Calculated: SpeedAdjustment = '"+waveManager.speedAdjustment+"'");
	}
}
