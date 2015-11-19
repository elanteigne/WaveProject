import java.util.concurrent.TimeUnit;

public class WaveManager {
	//Class Variables
	private static WaveManager waveManager;
	private BreakService breakService;
	private Receiver receiver;
	
	//MyInfo
	public String CarID;
	private int speed;
	private int breakAmount;
	private String direction;
	
	//Resources
	private String controlGroup = "230.0.0.1";
	private String breakServiceGroup = "230.0.0.2";
	
	//Constructor
	public WaveManager(){
		CarID = checkVinNumber();
		breakAmount = 100;
		speed = 20;
		direction = checkDirection();
		
		breakService = new BreakService(this, CarID,controlGroup);
		receiver = new Receiver(CarID,controlGroup);
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
		waveManager.run();
	}
	
	public void run(){
		if(speed>10){
			if(breakService.checkBreak(breakAmount)){
				breakService.sendControlMessage(breakServiceGroup);
				receiver.getPacket();
				
				while(breakService.checkBreak(breakAmount)){
					breakService.sendServiceMessage(breakAmount,direction);
					receiver.getPacket();
					
					try{
						TimeUnit.SECONDS.sleep(2);
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