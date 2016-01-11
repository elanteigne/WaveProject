import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class WaveManager {
	//Objects
	private static WaveManager waveManager;
	private GeneralInfoService generalInfoService;
	private BrakeService brakeService;
	private EmergencyService emergencyService;
	private Receiver receiver;
	public UserInterface userInterface;
	
	//MyInfo
	public String CarID;
	public double GPSlattitude;
	public double GPSlongitude;
	public int speed;
	public int brakeAmount;
	public String direction;
	public String vehicleType;
	public boolean sirensOn;
	
	//Calculated values
	public int suggestedBrakeAmount; //Percentage of brake that should be applied
	public int additionalBrakeAmount; //Percentage of additional brake that should be applied due to speed difference
	public int suggestedBrakeSpeed; //Speed at which brake should be applied, dependent on distance between vehicles
	public int trafficAheadSlowerWarningLight; 
	public int trafficBehindFasterWarningLight;

	//Resources
	public int port = 2222;
	public String controlGroup = "230.0.0.1";
	
	//Constructor
	public WaveManager(){
		userInterface = new UserInterface();
		try{ TimeUnit.SECONDS.sleep(1); } catch(Exception e){ }
		
		CarID = checkVinNumber();
		vehicleType = checkVehicleType();
		speed = checkSpeed();
		brakeAmount = checkBrake();
		direction = checkDirection();
		checkGPS();	
		
		generalInfoService = new GeneralInfoService(this);
		brakeService = new BrakeService(this);

		emergencyService = new EmergencyService(this);
		if(vehicleType.equals("Emergency")){
			sirensOn = false;
		}
		
		receiver = new Receiver(this,generalInfoService, brakeService, emergencyService);
		
		receiver.start();
		generalInfoService.start();
		brakeService.start();
		emergencyService.start();
		
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
	}
	
	public String checkVinNumber(){
		String vinNum = "000-000-000-001";
		userInterface.writeCarID(vinNum);
		return vinNum;
	}
	
	//Make this recurring and figure out GPS format
	public void checkGPS(){
		GPSlattitude = 45.3496235;
		GPSlongitude = -73.7597858;
		userInterface.writeGPS(GPSlattitude, GPSlongitude);
	}
	
	public String checkDirection(){
		String direction = "N";
		userInterface.writeDirection(direction);
		return direction;
	}
	
	public int checkSpeed(){
		int speed = 20;
		userInterface.writeSpeed(speed);
		return speed;
	}
	public int checkBrake(){
		int brakeAmount = 100;
		userInterface.writeBrakeAmount(brakeAmount);
		return brakeAmount;
	}
	public String checkVehicleType(){
		//String vehicleType = "Emergency";
		String vehicleType = "Civillian";
		userInterface.writeVehicleType(vehicleType);
		return vehicleType;
	}
	
	
}