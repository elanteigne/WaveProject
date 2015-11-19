
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
		sendMessage("Control", controlGroup, waveManager.CarID+"/"+serviceGroup+"/0/0");
		this.serviceGroup = serviceGroup;
	}
	
	public void sendServiceMessage(int breakAmount, String direction){
		sendMessage("Service", serviceGroup, waveManager.CarID+"/"+serviceGroup+"/"+direction+"/"+breakAmount);
	}
	
	public boolean checkBreak(int breakAmount){
		if(breakAmount>5){
			waveManager.speedAdjustment = computeData(breakAmount);				
			return true;
		}
		
		return false;
	}
	
	private int computeData(int breakAmount){
		int speedAdjustment;
		if(breakAmount<25){
			speedAdjustment = 5;
		}else if(breakAmount<50){
			speedAdjustment = 20;				
		}else if(breakAmount<75){
			speedAdjustment = 40;				
		}else{
			speedAdjustment = 60;				
		}
		System.out.println("SpeedAdjustment '"+speedAdjustment+"'");
		return speedAdjustment;
	}
}
