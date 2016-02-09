import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.*;
@SuppressWarnings("unused")

public class GeneralInfoService extends Service implements Runnable{
	//Objects
	private Thread generalInfoServiceThread;
	
	//Resources
	public int delay;
	public String serviceGroup = "230.0.0.2";
	public int messageID = 0;
	public int numClosebyVehicles;
	//private double closebyVehiclesTimestamp;
	private String output;
	
	//Exterior info
	
	//public static ArrayList<ArrayList<Object>> vehiclesAccountedFor = new ArrayList<ArrayList<Object>>();
	
	private long closebyVehiclesTimestamp;

	private int numVehiclesAccountedFor;

	//Constructor
	public GeneralInfoService(WaveManager waveManager){
		super(waveManager);
		delay = waveManager.delay*2;
	}

	//Class Methods
	public void start(){
		if(generalInfoServiceThread==null){
			generalInfoServiceThread = new Thread(this, "GeneralInfoService");
			generalInfoServiceThread.start();
		}
	}
	
	public void run(){
		while(true){
			delay = waveManager.delay*2;
			
			sendControlMessage();
			//Wait
			try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
			
			int count = 0;
			while(count<5){
				sendServiceMessage();

				delay = waveManager.delay;
				//Wait
				try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }

				count++;
			}
		}
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		 messageID++;
		 waveManager.userInterface.updateGeneralInfoServicePacketsSent(messageID);
	}
	
	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, ""+waveManager.headlights);
		messageID++;
		waveManager.userInterface.updateGeneralInfoServicePacketsSent(messageID);
	}
	
	//Method to calculate speed adjustment based on received packets
	public void computeData(String fromCarID, int heading,  int vehicleSpeed, double vehicleLattitude, double vehicleLongitude){
		double distanceBetweenVehicles = calculateDistance(vehicleLattitude, vehicleLongitude);
		
		if(distanceBetweenVehicles<150){
			//Only way to check ahead so far is checking the direction
			if(checkIfAhead(heading, vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed<waveManager.speed[0]){
					int speedDifference = waveManager.speed[0] - vehicleSpeed;
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficAheadSlowerWarningLight = warningLevel;
					
					System.out.println("o Calculated: TrafficAheadSlower x"+warningLevel);
					output = "o Calculated: TrafficAheadSlower x"+warningLevel;
					waveManager.userInterface.computedGeneralInfo(output);
					
					output = "TrafficAheadSlower x"+warningLevel;
					waveManager.userInterface.writeGeneralInfo(output);
				}else{
					System.out.println("o Calculated: Vehicle is ahead but is faster so is not considered");
					output = "o Calculated: Vehicle is ahead but is faster so is not considered";
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else if(checkIfBehind(heading, vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed>waveManager.speed[0]){
					int speedDifference = vehicleSpeed - waveManager.speed[0];
					int warningLevel = outputWarningLights(speedDifference);
					waveManager.trafficBehindFasterWarningLight = warningLevel;
					
					System.out.println("o Calculated: TrafficBehindFaster x"+warningLevel);
					output = "o Calculated: TrafficBehindFaster x"+warningLevel;
					waveManager.userInterface.computedGeneralInfo(output);
					
					output = "TrafficBehindFaster x"+warningLevel;
					waveManager.userInterface.writeGeneralInfo(output);
				}else{
					System.out.println("o Calculated: Vehicle is behind but is slower so is not considered");
					output = "o Calculated: Vehicle is behind but is slower so is not considered";
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else if(checkIfOncoming(heading, vehicleLattitude, vehicleLongitude)){
				if(waveManager.headlights == 2){
					System.out.println("o Calculated: Oncoming vehicles, please lower you high-beams");
					output = "o Calculated: Oncoming vehicles, please lower you high-beams";
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else{
				System.out.println("o Calculated: Vehicle is not in critical area, therefore not considered");
				output = "o Calculated: Vehicle is not in critical area, therefore not considered";
				waveManager.userInterface.computedGeneralInfo(output);
			}
			
			//Decide if trafficService should start sending
			if(distanceBetweenVehicles<25){
				
				if(waveManager.vehiclesAccountedFor.size()==0){
					closebyVehiclesTimestamp = System.currentTimeMillis();
				}
				
				listVehicle(fromCarID, heading, vehicleSpeed, vehicleLattitude, vehicleLongitude);
				
				output = "xxx Num Vehicles Accounted For: "+waveManager.vehiclesAccountedFor.size();
				waveManager.userInterface.computedGeneralInfo(output);
				
				
				if(System.currentTimeMillis()<closebyVehiclesTimestamp+5000){
					//if(waveManager.vehiclesAccountedFor.size()>5){
					if(waveManager.vehiclesAccountedFor.size()>0){
						if(waveManager.speed[5]>(waveManager.speed[0]*0.5)){
							waveManager.inTraffic = true;
							waveManager.userInterface.computedGeneralInfo("In traffic: " + waveManager.inTraffic);
						}
					}
				}else{
					for(int i=0; i<waveManager.vehiclesAccountedFor.size(); i++){
						waveManager.vehiclesAccountedFor.remove(0);
						//only remove 0 since removing element(0) will make the second element element(0) on next iteration
					}
					waveManager.inTraffic = false;
					//waveManager.userInterface.computedGeneralInfo(">>>Emptied vehicles list");
				}
				
				//numVehiclesAccountedFor=0;
			}
		}else{
			System.out.println("o Calculated: Vehicle is too far ahead to be considered");
			output = "o Calculated: Vehicle is too far ahead to be considered";
			waveManager.userInterface.computedGeneralInfo(output);
		}
	}
	
	private int outputWarningLights(int speedDifference){
		if(speedDifference<10){
			return 1; //Turn on first warning light
		}else if(speedDifference<20){
			return 2; //Turn on second warning light
		}else{
			return 3; //Turn on third warning light
		}		
	}
	
	private void listVehicle(String fromCarID, int heading,  int vehicleSpeed, double vehicleLattitude, double vehicleLongitude){
		
		boolean isDuplicate = false;
		
		for(int i = 0; i < waveManager.vehiclesAccountedFor.size(); i++){
			if(waveManager.vehiclesAccountedFor.get(i).get(0).equals(fromCarID)){
				isDuplicate = true;
			}	
		}
		
		if(isDuplicate == false){
			
		ArrayList<Object> vehicle = new ArrayList<Object>();

		if(waveManager.vehiclesAccountedFor.size()> 9){
			waveManager.vehiclesAccountedFor.remove(0);
		}
		
		vehicle.add(fromCarID);
		vehicle.add(heading);
		vehicle.add(vehicleSpeed);
		vehicle.add(vehicleLattitude);
		vehicle.add(vehicleLongitude);
		
		waveManager.vehiclesAccountedFor.add(vehicle);
		
		}
	}
}

	/*public void addVehicle(String CarID, double GPSlattitude, double GPSlongitude, int speed, int bearing){
		
		VehicleInfo v = new VehicleInfo(CarID, GPSlattitude, GPSlongitude, speed, bearing);
		boolean isDuplicate = false;
		
		//check for duplicate ID (is this fully handled by the receiver?)
		for(int i = 0; i<vehicles.size(); i++){
			if(vehicles.get(i).CarID != CarID){
				isDuplicate = true;
			}
		}
		//remove the oldest vehicle in the list if there are ten in the list
		if(isDuplicate == false){
			if(vehicles.size() > 9){ vehicles.remove(0);};
			vehicles.add(v);
		}
		
	}
	
*/

/*for(int i=0; i<10; i++){
if(!vehiclesAccountedFor[i].equals("")){
	if(!vehiclesAccountedFor[i].equals(fromCarID)){
		updateList = true;
	}
}
}
if(vehiclesAccountedFor[0].equals("")){
emptyList=true;
}

if(updateList||emptyList){
for(int i=9; i>0; i--){
	vehiclesAccountedFor[i] = vehiclesAccountedFor[i-1];
}
vehiclesAccountedFor[0] = fromCarID;	
}*/

//addVehicle(CarID, GPSlattitude, GPSlongitude, speed, bearing);
//addVehicles(direction, vehicleSpeed);
/*
numVehiclesAccountedFor = vehiclesAccountedFor.size();
for(int i=0; i<9; i++){
	if(!vehiclesAccountedFor[i].equals("")){
		numVehiclesAccountedFor++;
	}
}
*/