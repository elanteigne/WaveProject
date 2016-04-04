import java.util.*;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class WaveManager {
	//Objects
	private static WaveManager waveManager;
	public ArrayList<Service> services;
	private Receiver receiver;
	public UserInterface userInterface;
	
	//MyInfo
	public String CarID;
	public double GPSlattitude;
	public double GPSlongitude;
	public int[] speed = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public int brakeAmount;
	public int heading;
	public String vehicleType;
	public boolean sirensOn;
	public int headlights;
	
	//Calculated values
	public int suggestedBrakeAmount; //Percentage of brake that should be applied
	public int additionalBrakeAmount; //Percentage of additional brake that should be applied due to speed difference
	public int suggestedBrakeSpeed; //Speed at which brake should be applied, dependent on distance between vehicles
	public int trafficAheadSlowerWarningLight; 
	public int trafficBehindFasterWarningLight;
	public boolean inTraffic;
	public int trafficLevel;
	public ArrayList<ArrayList<Object>> vehiclesAccountedFor = new ArrayList<ArrayList<Object>>(); 
	
	//Resources
	public int port = 2222;
	public int delay = 350;
	public String controlGroup = "230.0.0.1";
	
	//Constructor
	public WaveManager(){
		CarID = "222-222-222-227";
		setVehicleType("Civilian");
		setSpeed(speed[0]);
		setBrake(100);
		setHeading(330);
		headlights = 0; //0 is off, 1 is low-beams, 2 is high-beams
		setGPS(45.38375,-75.68896);	
		
		//Add dummy vehicles
		ArrayList<Object> vehicle; 
		for(int i=0; i < 7; i++){
			vehicle = new ArrayList<Object>();
			vehicle.add("222-222-222-20" + i);
			vehicle.add(330);
			vehicle.add(this.speed[0]);
			vehicle.add(GPSlattitude);
			vehicle.add(GPSlongitude);
			vehiclesAccountedFor.add(vehicle);
		}

		inTraffic = true;
		userInterface = new UserInterface(this);
		userInterface.start();
		
		try{ TimeUnit.SECONDS.sleep(1); } catch(Exception e){ }
		
		services = new ArrayList<>();
		services.add(new GeneralInfoService(this));
		services.add(new BrakeService(this));
		services.add(new EmergencyService(this));
		services.add(new TrafficService(this));
		
		//Emergency Service
		if(vehicleType.equals("Emergency")){
			sirensOn = false;
		}
		
		receiver = new Receiver(this,services.get(0), services.get(1), services.get(2), services.get(3));
		
		receiver.start();
		for(Service service:services){
			service.start();
			}
	}
	
	//Class Methods
	public static void main(String[] args){
		waveManager = new WaveManager();
	}
	
	public void setGPS(double lat, double lng){
		this.GPSlattitude = lat;
		this.GPSlongitude = lng;
	}

	public void setHeading(int heading){
		this.heading = heading; 
	}
	
	public void setSpeed(int speed){
		this.speed[0] = speed;
	}
	
	public synchronized void addSpeed(int speed){
		for(int i=9; i>0; i--){
			this.speed[i]=this.speed[i-1];
		}
		this.speed[0]=speed;
	}
	
	public void setBrake(int bAmount){
		this.brakeAmount = bAmount;
	}
	
	public void setVehicleType(String vType){
		this.vehicleType = vType;
	}

	public synchronized int getSpeed(){
		return speed[0];
	}
	
	public synchronized boolean getSiren(){
		return sirensOn;
	}
	

	public synchronized boolean getTrafficValue(){
		return inTraffic;
	}
}