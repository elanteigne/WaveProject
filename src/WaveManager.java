import java.util.concurrent.TimeUnit;

public class WaveManager {
	//Class Variables
	private static WaveManager waveManager;
	private BreakService breakService;
	private Receiver receiver;
	private EmergencyService emergencyService;
	

	//MyInfo
	public String CarID;
	private int speed;
	private int breakAmount;
	private String direction;
	
	public int speedAdjustment;
	
	//Resources
	public int port = 2222;
	public String controlGroup = "230.0.0.1";
	public String breakServiceGroup = "230.0.0.2";
	
	//Constructor
	public WaveManager(){
		CarID = checkVinNumber();
		breakAmount = 100;
		speed = 20;
		direction = checkDirection();
		
		breakService = new BreakService(this);
		receiver = new Receiver(this,breakService);
		emergencyService = new EmergencyService(this);
		
		//How can we make the "main" listen to all services?
		//receiver = new Receiver(this,emergencyService)
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