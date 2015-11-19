
public class BreakService extends Service{
	//Class Variables
	private WaveManager waveManager;
	
	//MyInfo
	public int speedAdjustment;
	
	//Resources
	private String controlGroup;
	private String serviceGroup;
	
	
	//Constructor
	public BreakService(WaveManager waveManager, String carID, String controlGroup){
		super(waveManager);
		this.waveManager = waveManager;
		this.controlGroup = controlGroup;
	}

	//Class Methods
	public void sendControlMessage(String serviceGroup){
		sendMessage("Control", controlGroup, serviceGroup+"/"+waveManager.CarID+"/0/0");
		this.serviceGroup = serviceGroup;
	}
	
	public void sendServiceMessage(int breakAmount, String direction){
		sendMessage("Service", serviceGroup, serviceGroup+"/"+waveManager.CarID+"/"+direction+"/"+breakAmount);
	}
	
	public boolean checkBreak(int breakAmount){
		if(breakAmount>5){
			speedAdjustment = computeData(breakAmount);				
			return true;
		}
		
		return false;
	}
	
	private int computeData(int breakAmount){
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
