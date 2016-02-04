import java.util.*;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class WaveManager {
	//Objects
	private static WaveManager waveManager;
	private GeneralInfoService generalInfoService;
	private BrakeService brakeService;
	private EmergencyService emergencyService;
	private TrafficService trafficService;
	private Receiver receiver;
	public UserInterface userInterface;
	public AerialDisplay aerialDisplay;
	
	//MyInfo
	public String CarID;
	public double GPSlattitude;
	public double GPSlongitude;
	public int speed;
	public int brakeAmount;
	public int heading;
	public String vehicleType;
	public boolean sirensOn;
	
	//Calculated values
	public int suggestedBrakeAmount; //Percentage of brake that should be applied
	public int additionalBrakeAmount; //Percentage of additional brake that should be applied due to speed difference
	public int suggestedBrakeSpeed; //Speed at which brake should be applied, dependent on distance between vehicles
	public int trafficAheadSlowerWarningLight; 
	public int trafficBehindFasterWarningLight;
	public boolean inTraffic;
	public int trafficLevel;
	public ArrayList<ArrayList<Object>> vehiclesAccountedFor = new ArrayList<ArrayList<Object>>(); //static???
	
	//Resources
	public int port = 2222;
	public int delay;
	public String controlGroup = "230.0.0.1";
	
	//Constructor
	public WaveManager(){
		CarID = checkVinNumber();
		vehicleType = checkVehicleType();
		speed = checkSpeed();
		speed = 0;
		brakeAmount = checkBrake();
		heading = checkHeading();
		checkGPS();	
		delay=500;
		
		userInterface = new UserInterface(this);
		userInterface.start();
		aerialDisplay = new AerialDisplay(this);
		aerialDisplay.start();
		
		try{ TimeUnit.SECONDS.sleep(1); } catch(Exception e){ }
		
		generalInfoService = new GeneralInfoService(this);
		brakeService = new BrakeService(this);
		emergencyService = new EmergencyService(this);
		trafficService = new TrafficService(this);
		
		//Emergency Service
		if(vehicleType.equals("Emergency")){
			sirensOn = checkSirens();
		}
	
		//Traffic Service
		//
		
		receiver = new Receiver(this,generalInfoService, brakeService, emergencyService, trafficService);
		
		receiver.start();
		generalInfoService.start();
		brakeService.start();
		emergencyService.start();
		trafficService.start();
		
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
	}
	
	public String checkVinNumber(){
		String vinNum = "000-000-000-001";
		return vinNum;
	}
	
	//Make this recurring and figure out GPS format
	public void checkGPS(){
		GPSlattitude = 50.05;//45.3496235;
		GPSlongitude = 50.05;//-73.7597858;
	}

	public int checkHeading(){
		int heading = 0; //N
		return heading;
	}
	
	public int checkSpeed(){
		return speed;
	}
	
	public int checkBrake(){
		int brakeAmount = 100;
		return brakeAmount;
	}
	
	public String checkVehicleType(){
		String vehicleType = "Emergency";
		//String vehicleType = "Civilian";
		return vehicleType;
	}
	
	public boolean checkSirens(){
		return false;
	}
	
}