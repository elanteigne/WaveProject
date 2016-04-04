import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.*;
@SuppressWarnings("unused")

public class GeneralInfoService extends Service implements Runnable{
	//Objects

	//Resources
	public int numClosebyVehicles;
	private String[] vehicleAheadInfo = {"","",""};
	private double vehicleAheadTimestamp = 0;
	private String[] vehicleBehindInfo = {"","",""};
	private double vehicleBehindTimestamp = 0;

	//Exterior info
	private long closebyVehiclesTimestamp;
	private int numVehiclesAccountedFor;

	//Constructor
	public GeneralInfoService(WaveManager waveManager){
		super(waveManager);
		this.updateRunConditions();
		this.updateMsgDelays();
		this.serviceGroup = "230.0.0.2";
		this.threadName = "GeneralInfoService";
		runAlgorithm = this;
	}

	//Class Methods	
	public void updateRunConditions(){
		this.runCondition1 = true;
		this.runCondition2 = true;
	}

	public void updateMsgDelays(){
		this.delay = waveManager.delay;
		this.controlMsgDelay = delay*2;
		this.serviceMsgDelay = delay;
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
	public void computeData(ArrayList<Object> params){
		//Parameters
		String fromCarID = (String) params.get(0);
		int heading = (int) params.get(1);
		int vehicleSpeed = (int) params.get(2);
		double vehicleLattitude = (double) params.get(3);
		double vehicleLongitude = (double) params.get(4);

		double distanceBetweenVehicles = calculateDistance(vehicleLattitude, vehicleLongitude);
		if(distanceBetweenVehicles<150){
			//Only way to check ahead so far is checking the direction
			if(checkIfAhead(heading, vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed<waveManager.getSpeed()){
					if(vehicleAheadInfo[0].equals("") || vehicleSpeed<Integer.parseInt(vehicleAheadInfo[1]) && distanceBetweenVehicles<(Double.parseDouble(vehicleAheadInfo[2])+30) || vehicleAheadInfo[0].equals(fromCarID)){
						vehicleAheadInfo[0] = fromCarID;
						vehicleAheadInfo[1] = ""+vehicleSpeed;
						vehicleAheadInfo[2] = ""+distanceBetweenVehicles;
					}
					int speedDifference = waveManager.getSpeed() - Integer.parseInt(vehicleAheadInfo[1]);
					output = "o Calculated: Traffic Ahead Slower by "+speedDifference+" Km/h";
					waveManager.userInterface.turnOnGeneralInfoCarAhead(speedDifference);
				}else{
					output = "o Calculated: Vehicle is ahead but is not slower so it is not considered";
				}
			}else if(checkIfBehind(heading, vehicleLattitude, vehicleLongitude)){
				if(vehicleSpeed>waveManager.getSpeed()){
					if(vehicleBehindInfo[0].equals("") || vehicleSpeed>Integer.parseInt(vehicleBehindInfo[1]) && distanceBetweenVehicles<(Double.parseDouble(vehicleBehindInfo[2])+30) || vehicleBehindInfo[0].equals(fromCarID)){
						vehicleBehindInfo[0] = fromCarID;
						vehicleBehindInfo[1] = ""+vehicleSpeed;
						vehicleBehindInfo[2] = ""+distanceBetweenVehicles;
					}

					int speedDifference = Integer.parseInt(vehicleBehindInfo[1]) - waveManager.getSpeed();
					output = "o Calculated: Traffic Behind Faster by "+speedDifference+" Km/h";
					waveManager.userInterface.turnOnGeneralInfoCarBehind(speedDifference);
				}else{
					output = "o Calculated: Vehicle is behind but not faster so it is not considered";
				}
			}else if(checkIfOncoming(heading, vehicleLattitude, vehicleLongitude)){
				if(waveManager.headlights == 2){
					output = "o Calculated: Oncoming vehicles, please lower your high-beams";
				}
			}else{
				output = "o Calculated: Vehicle is not in critical area, therefore not considered";
			}
			waveManager.userInterface.computedGeneralInfo(output);

			//Decide if trafficService should start sending
			if(distanceBetweenVehicles<100){
				if(heading<waveManager.heading+10 && heading>waveManager.heading-10){
					if(waveManager.vehiclesAccountedFor.size()==0){
						closebyVehiclesTimestamp = System.currentTimeMillis();
					}

					listVehicle(fromCarID, heading, vehicleSpeed, vehicleLattitude, vehicleLongitude);
					if(System.currentTimeMillis()<closebyVehiclesTimestamp+5000){
						if(waveManager.vehiclesAccountedFor.size()>5){
							if(waveManager.speed[4]>(waveManager.getSpeed()*1.25)){
								waveManager.inTraffic = true;							
								waveManager.userInterface.computedGeneralInfo("o Calculated: In traffic");
							}
						}
					}else{
						for(int i=0; i<waveManager.vehiclesAccountedFor.size(); i++){
							waveManager.vehiclesAccountedFor.remove(i);
							//only remove 0 since removing element(0) will make the second element element(0) on next iteration
						}
						waveManager.inTraffic = false;
					}
				}
			}
		}else{
			output = "o Calculated: Vehicle is too far away to be considered";
			waveManager.userInterface.computedGeneralInfo(output);
		}
	}

	private void listVehicle(String fromCarID, int heading,  int vehicleSpeed, double vehicleLattitude, double vehicleLongitude){
		boolean isDuplicate = false;
		for(ArrayList<Object> vehicle: waveManager.vehiclesAccountedFor){
			if(vehicle.get(0).equals(fromCarID)){
				isDuplicate = true;
			}	
		}

		if(!isDuplicate){
			if(waveManager.vehiclesAccountedFor.size()> 9){
				waveManager.vehiclesAccountedFor.remove(0);
			}
			ArrayList<Object> vehicle = new ArrayList<Object>();
			vehicle.add(fromCarID);
			vehicle.add(heading);
			vehicle.add(vehicleSpeed);
			vehicle.add(vehicleLattitude);
			vehicle.add(vehicleLongitude);
			waveManager.vehiclesAccountedFor.add(vehicle);
		}
	}

	public void eraseAheadVehicle(){
		Arrays.fill(vehicleAheadInfo, "");
	}

	public void eraseBehindVehicle(){
		Arrays.fill(vehicleBehindInfo, "");
	}

}