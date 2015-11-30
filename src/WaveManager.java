import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

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
		
		receiver.start();
		breakService.start();
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
	}
	
	public String checkVinNumber(){
		return "000-000-000-001";
	}
	
	public String checkDirection(){
		return "N";
	}
}