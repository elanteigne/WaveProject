import java.util.concurrent.TimeUnit;

public class BrakeService extends Service implements Runnable{
	//Objects
	private Thread brakeServiceThread;
	
	//Resources
	public int delay;
	public String serviceGroup = "230.0.0.3";
	public int messageID = 0;
	private String output;
	private String[] vehicleBrakingInfo = {"","",""};
	private double vehicleBrakingTimestamp = 0;
	
	//Constructor
	public BrakeService(WaveManager waveManager){
		super(waveManager);
		delay = waveManager.delay;
	}

	//Class Methods
	public void start(){
		if(brakeServiceThread==null){
			brakeServiceThread = new Thread(this, "BrakeService");
			brakeServiceThread.start();
		}
	}
	
	public void run(){
		while(true){
			delay = waveManager.delay;
			if(waveManager.getSpeed()>10){
				if(checkBrake()){				
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
			checkTimestamps();
		}
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
	public void computeData(String fromCarID, int heading, int speed, double vehicleLattitude, double vehicleLongitude, int brakeAmount){
		//This should check if it is ahead on the SAME ROAD if possible
		if(checkIfAhead(heading, vehicleLattitude, vehicleLongitude)){
			//Distance away affect speed at which you get to desired amount
			//Speed difference affects the amount of brake that should be pressed
			//The amount others ahead are breaking should influence how much you need to break
			//If there are weather conditions it should affect break amount by a certain percentage
			
			double distanceBetweenVehicles = calculateDistance(vehicleLattitude, vehicleLongitude);
			int speedDifference = waveManager.getSpeed()-speed;

			//If vehicle ahead is going faster then there is no point in braking
			if(speedDifference>0){
				if(vehicleBrakingInfo[0].equals("") || speed<Integer.parseInt(vehicleBrakingInfo[1]) && distanceBetweenVehicles<(Double.parseDouble(vehicleBrakingInfo[2])+30)){
					vehicleBrakingInfo[0] = fromCarID;
					vehicleBrakingInfo[1] = ""+speed;
					vehicleBrakingInfo[2] = ""+brakeAmount;
					vehicleBrakingTimestamp = System.currentTimeMillis();
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
				waveManager.userInterface.computedBrakeInfo(output);
				waveManager.userInterface.turnOnBrakeApplied(brakeAmount, (int)distanceBetweenVehicles);
			}else{
				output = "o Calculated: Vehicle ahead is going faster than you, therefore braking is not considered";
				waveManager.userInterface.computedBrakeInfo(output);
			}
		}else{
			output = "o Calculated: Vehicle is not ahead, therefore braking is not considered";
			waveManager.userInterface.computedBrakeInfo(output);
		}
	}

	private void checkTimestamps(){
		if(vehicleBrakingTimestamp+2000<System.currentTimeMillis()){
			vehicleBrakingInfo[0] = "";
			vehicleBrakingInfo[1] = "";
			vehicleBrakingInfo[2] = "";
			
		}
		
	}
}
