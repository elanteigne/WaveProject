import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class WaveManager {
	//Class Variables
	private static WaveManager waveManager;
	private BreakService breakService;
	private EmergencyService emergencyService;
	private Receiver receiver;
	
	//MyInfo
	private String vehicleType;
	public boolean sirensOn;
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
		vehicleType = checkVehicleType();
		breakAmount = 100;
		speed = 20;
		direction = checkDirection();
		
		breakService = new BreakService(this);
		
		if(vehicleType.equals("Emergency")){
			emergencyService = new EmergencyService(this);
			sirensOn = true;
		}
		
		receiver = new Receiver(this,breakService, emergencyService);
		
		receiver.start();
		breakService.start();
		if(vehicleType.equals("Emergency")){
			emergencyService.start();
		}
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
	}
	
	public String checkVinNumber(){
		return "000-000-000-001";
	}
	
	public String checkVehicleType(){
		return "Emergency";
		//return "Civilian";
	}
	
	public String checkDirection(){
		return "N";
	}
}