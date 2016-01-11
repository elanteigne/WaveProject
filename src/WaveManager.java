import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class WaveManager {
	//Objects
	private static WaveManager waveManager;
<<<<<<< HEAD
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
=======
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
>>>>>>> refs/remotes/origin/master
	
	//end of defaults

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
<<<<<<< HEAD
		//TrafficService = new TrafficService(this);
		//rec2 = new Receiver(this,TrafficService);
		receiver = new Receiver(this,breakService);
		breakService = new BreakService(this);
		/*receiver = new Receiver(this,breakService);*/
=======
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
		
>>>>>>> refs/remotes/origin/master
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
	}
	
<<<<<<< HEAD
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
=======
	public String checkVinNumber(){
		String vinNum = "000-000-000-001";
		userInterface.writeCarID(vinNum);
		return vinNum;
>>>>>>> refs/remotes/origin/master
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