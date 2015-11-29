import java.util.concurrent.TimeUnit;

public class WaveManager {
	//Class Variables
	private static WaveManager waveManager;
	private BreakService breakService;
	private Receiver receiver;
	
	//MyInfo
	public String CarID;
	public int speed;
	public int breakAmount;
	public String direction;
	
	//Calculated values
	public int speedAdjustment;
	
	//Resources
	public int port = 2222;
	public String controlGroup = "230.0.0.1";
	public int messageIDglobal = 0;
	
	//Constructor
	public WaveManager(){
		CarID = checkVinNumber();
		breakAmount = 100;
		speed = 20;
		direction = checkDirection();
		
		breakService = new BreakService(this);
		receiver = new Receiver(this,breakService);
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
		waveManager.run();
	}
	
	public void run(){
		if(speed>10){
			if(breakService.checkBreak()){
				breakService.sendControlMessage();
				receiver.getPacket();
				
				while(breakService.checkBreak()){
					breakService.sendServiceMessage();
					receiver.getPacket();
					
					//Wait 1 second
					try{
						TimeUnit.SECONDS.sleep(1);
					}catch(Exception e){
						
					}
				}
			}
		}
	}
	
	public String checkVinNumber(){
		//Check Vin Number
		return "000-000-000-001";
	}
	
	public String checkDirection(){
		//Check compass
		return "N";
	}
}