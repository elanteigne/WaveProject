import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EmergencyService extends Service implements Runnable {

	//Constructor
	public EmergencyService(WaveManager waveManager){
		super(waveManager);
		this.updateMsgDelays();
		this.updateRunConditions();
		this.serviceGroup = "230.0.0.4";
		this.threadName = "EmergencyService";
		runAlgorithm = this;
	}

	//Class Methods
	public void updateRunConditions(){
		this.runCondition1 = waveManager.getSiren();
		this.runCondition2 = true;
	}
		
	public void updateMsgDelays(){
		this.delay = waveManager.delay;
		this.controlMsgDelay = delay;
		this.serviceMsgDelay = delay;
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		messageID++;
		waveManager.userInterface.updateEmergencyServicePacketsSent(messageID);
	}

	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, "");
		messageID++;
		waveManager.userInterface.updateEmergencyServicePacketsSent(messageID);
	}

	//Method to determine emergency siren output based on received packets
	public void computeData(ArrayList<Object> params){
		//Parameters
		int heading = (int) params.get(0);
		double vehicleLattitude = (double) params.get(1);
		double vehicleLongitude = (double) params.get(2);
		
		boolean isRelevant = false;
		double distance = calculateDistance(vehicleLattitude, vehicleLongitude);
		if(distance<300){
			if(checkIfBehind(heading, vehicleLattitude, vehicleLongitude)){
				output = "o Calculated: Emergency Vehicle approaching behind ("+(int)distance+"m). Please be aware.";
				isRelevant = true;
			}else{
				output = "o Calculated: Emergency Vehicle nearby ("+(int)distance+"m). Please be aware.";
			}
			waveManager.userInterface.computedEmergencyInfo(output);
			waveManager.userInterface.turnOnEmergencySiren((int)distance, isRelevant);
		}
	}
}
