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

					output = "o Calculated: Traffic Ahead Slower by "+speedDifference+" Km/h";
					System.out.println(output);
					waveManager.userInterface.computedGeneralInfo(output);
					
					waveManager.userInterface.writeGeneralInfoCarAhead(speedDifference, vehicleSpeed);
				}else{
					output = "o Calculated: Vehicle is ahead but is not slower so it is not considered";
					System.out.println(output);
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else if(checkIfBehind(heading, vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed>waveManager.speed[0]){
					int speedDifference = vehicleSpeed - waveManager.speed[0];

					output = "o Calculated: Traffic Behind Faster by "+speedDifference+" Km/h";
					System.out.println(output);
					waveManager.userInterface.computedGeneralInfo(output);
					
					waveManager.userInterface.writeGeneralInfoCarBehind(speedDifference, vehicleSpeed);
				}else{
					output = "o Calculated: Vehicle is behind but not faster so it is not considered";
					System.out.println(output);
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else if(checkIfOncoming(heading, vehicleLattitude, vehicleLongitude)){
				if(waveManager.headlights == 2){
					output = "o Calculated: Oncoming vehicles, please lower your high-beams";
					System.out.println(output);
					waveManager.userInterface.computedGeneralInfo(output);
				}
			}else{
				output = "o Calculated: Vehicle is not in critical area, therefore not considered";
				System.out.println(output);
				waveManager.userInterface.computedGeneralInfo(output);
			}
			
			//Decide if trafficService should start sending
			if(distanceBetweenVehicles<25){
				
				if(waveManager.vehiclesAccountedFor.size()==0){
					closebyVehiclesTimestamp = System.currentTimeMillis();
				}
				
				listVehicle(fromCarID, heading, vehicleSpeed, vehicleLattitude, vehicleLongitude);
								
				if(System.currentTimeMillis()<closebyVehiclesTimestamp+5000){
					//if(waveManager.vehiclesAccountedFor.size()>5){
					if(waveManager.vehiclesAccountedFor.size()>0){
						if(waveManager.speed[4]>(waveManager.speed[0]*1.5)){
							waveManager.inTraffic = true;
							waveManager.userInterface.computedGeneralInfo("o Calculated: In traffic");
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
			output = "o Calculated: Vehicle is too far ahead to be considered";
			System.out.println(output);
			waveManager.userInterface.computedGeneralInfo(output);
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