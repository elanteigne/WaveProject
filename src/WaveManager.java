import java.util.concurrent.TimeUnit;

public class WaveManager {
	//Class Variables
	private static WaveManager waveManager;
	private BreakService breakService;
	//private TrafficService TrafficService;
	private Receiver receiver;
	//private Receiver rec2;
	
	//MyInfo
	public String CarID;
	private int breakAmount;
	
	// Default variables
	
	public int speed;
	public String direction;
	public int speedAdjustment;
	public int channel = 4;
	public int service = 3;		
	public int lights = 3;		
	public int turnSignals = 0;	
	public int breaks = 0;		
	public int eBreak = 0;	
	public int emergency = 1;		
	public int vType = 0;	
	
	//end of defaults

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
		//TrafficService = new TrafficService(this);
		//rec2 = new Receiver(this,TrafficService);
		receiver = new Receiver(this,breakService);
		breakService = new BreakService(this);
		/*receiver = new Receiver(this,breakService);*/
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
		waveManager.run();
	}
	
	public void run(){
		
		//routeTraffic();
		
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