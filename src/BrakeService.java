import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BrakeService extends Service implements Runnable{
	//Objects

	//Resources
	private String[] vehicleBrakingInfo = {"","0","0"};

	//Constructor
	public BrakeService(WaveManager waveManager){
		super(waveManager);
		this.updateRunConditions();
		this.updateMsgDelays();
		this.serviceGroup = "230.0.0.3";
		this.threadName = "BrakeService";
		runAlgorithm = this;
	}

	//Class Methods
	public void updateRunConditions(){
		this.runCondition1 = waveManager.getSpeed()>10 && checkBrake();
		this.runCondition2 = checkBrake();
	}
	
	public void updateMsgDelays(){
		this.delay = waveManager.delay;
		this.controlMsgDelay = delay;
		this.serviceMsgDelay = delay;
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "0");
		messageID++;
		waveManager.userInterface.updateBrakeServicePacketsSent(messageID);
	}

	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, ""+waveManager.brakeAmount);
		messageID++;
		waveManager.userInterface.updateBrakeServicePacketsSent(messageID);
	}


	//The check to see if I send
	public boolean checkBrake(){
		if(waveManager.brakeAmount>0){			
			return true;
		}
		return false;
	}

	//Method to calculate speed adjustment based on received packets
	//brake amount of other vehicles ahead should influence suggested brake amount calculation
	//Certain weather conditions should affect brake amount calculation by a percentage
	public void computeData(ArrayList<Object> params){
		//Parameters
				String fromCarID = (String) params.get(0);
				int heading = (int) params.get(1);
				int speed = (int) params.get(2);
				double vehicleLattitude = (double) params.get(3);
				double vehicleLongitude = (double) params.get(4);
				int brakeAmount = (int) params.get(5);
				
		//This should check if it is ahead on the SAME ROAD if possible
		if(checkIfAhead(heading, vehicleLattitude, vehicleLongitude)){			
			//affects speed at which you get to desired brake amount
			double distanceBetweenVehicles = calculateDistance(vehicleLattitude, vehicleLongitude);	
			
			//affects the brake amount that should be applied
			int speedDifference = waveManager.getSpeed()-speed;

			//If vehicle ahead is going faster then there is no point in braking
			if(speedDifference>0){
				if(vehicleBrakingInfo[0].equals("") || distanceBetweenVehicles<Double.parseDouble(vehicleBrakingInfo[2]) || vehicleBrakingInfo[0].equals(fromCarID)){
					vehicleBrakingInfo[0] = fromCarID;
					vehicleBrakingInfo[1] = ""+brakeAmount;
					vehicleBrakingInfo[2] = ""+distanceBetweenVehicles;
				}
				waveManager.suggestedBrakeAmount = brakeAmount;

				//The more of a speed difference there is the more the brake should be applied
				//Possibly include weather conditions here
				if(speedDifference<25){
					waveManager.additionalBrakeAmount = 5;
				}else if(speedDifference<40){
					waveManager.additionalBrakeAmount = 10;
				}else if(speedDifference<60){
					//Get data to ensure the values below
					waveManager.additionalBrakeAmount = 15;
				}else if(speedDifference<75){
					waveManager.additionalBrakeAmount = 20;
				}else if(speedDifference<90){
					waveManager.additionalBrakeAmount = 30;
				}else{
					waveManager.additionalBrakeAmount = 40;
				}
				//The closer the vehicle ahead is the faster the brake should be applied
				if(distanceBetweenVehicles<50){
					waveManager.suggestedBrakeSpeed = 4;
				}else if(distanceBetweenVehicles<100){
					waveManager.suggestedBrakeSpeed = 3;
				}else if(distanceBetweenVehicles<150){
					waveManager.suggestedBrakeSpeed = 2;
				}else{
					waveManager.suggestedBrakeSpeed = 1;
				}
				output = "o Calculated: SpeedDifference = "+speedDifference+" km/h, mySpeed = "+waveManager.getSpeed()+" km/h,"+""
						+ " DistanceBetweenVehicles = "+distanceBetweenVehicles+" m, SuggestedBrakeAmount = "+waveManager.suggestedBrakeAmount+"%,"
						+" AdditionalBrakeAmount = "+waveManager.additionalBrakeAmount+"%, SuggestedBrakeSpeed = '"+waveManager.suggestedBrakeSpeed+"'";
				waveManager.userInterface.turnOnBrakeApplied(Integer.parseInt(vehicleBrakingInfo[1]), (int)Double.parseDouble(vehicleBrakingInfo[2]));
			}else{
				output = "o Calculated: Vehicle ahead is going faster than you, therefore braking is not considered";
			}
		}else{
			output = "o Calculated: Vehicle is not ahead, therefore braking is not considered";
		}
		waveManager.userInterface.computedBrakeInfo(output);
	}

	public void eraseData(){
		Arrays.fill(vehicleBrakingInfo, "");
	}
}
