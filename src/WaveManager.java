import java.util.Timer;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class WaveManager {
	//Objects
	private static WaveManager waveManager;
	private GeneralInfoService generalInfoService;
	private BrakeService brakeService;
	private EmergencyService emergencyService;
	private Receiver receiver;
	
	//MyInfo
	private String vehicleType;
	public boolean sirensOn;
	public String CarID;
	public double GPSlattitude;
	public double GPSlongitude;
	public int speed;
	public int brakeAmount;
	public String direction;
	
	//Calculated values
	public int speedAdjustment; //Percentage of brake that should be applied
	public int trafficAheadSlowerWarningLight; 
	public int trafficBehindFasterWarningLight;
	
	//Resources
	public int port = 2222;
	public String controlGroup = "230.0.0.1";
	
	//Constructor
	public WaveManager(){
		CarID = checkVinNumber();
		vehicleType = checkVehicleType();
		brakeAmount = 100;
		speed = 20;
		direction = checkDirection();
		checkGPS();
		
		generalInfoService = new GeneralInfoService(this);
		brakeService = new BrakeService(this);
		
		if(vehicleType.equals("Emergency")){
			emergencyService = new EmergencyService(this);
			sirensOn = false;
		}
		
		receiver = new Receiver(this,generalInfoService, brakeService, emergencyService);
		
		receiver.start();
		generalInfoService.start();
		brakeService.start();
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
	
	//Make this recurring and figure out GPS format
	public void checkGPS(){
		GPSlattitude = 45.3496235;
		GPSlongitude = -73.7597858;
	}
	
	public String checkVehicleType(){
		return "Emergency";
		//return "Civilian";
	}
	
	public String checkDirection(){
		return "N";
	}
}