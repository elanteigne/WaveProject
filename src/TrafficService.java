import java.text.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class TrafficService extends Service implements Runnable {
	//Class Variables
	private Thread trafficServiceThread;
	
	//Resources
	public int delay;
	public int messageID = 0;
	public String serviceGroup = "230.0.0.5";
	private String output;
	
	//Global
	public String clusterVariables = "";
	
	//Constructor
	public TrafficService(WaveManager waveManager){
		super(waveManager);
		delay = waveManager.delay;
	}
	
	//Class Methods
	public void start(){
		if(trafficServiceThread==null){
			trafficServiceThread = new Thread(this, "TrafficService");
			trafficServiceThread.start();
		}
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		messageID++;
		waveManager.userInterface.updateTrafficServicePacketsSent(messageID);
	}
	
	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, ""+clusterVariables);
		messageID++;
		waveManager.userInterface.updateTrafficServicePacketsSent(messageID);
	}
	
	public void run(){
		while(true){
			
			delay = waveManager.delay*2;

			if(waveManager.getTrafficValue()){
				this.clusterVariables = getClusterValues();
				sendControlMessage(); 
				//Wait 
				try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
				
				int count = 0;
				while(count<5){
					sendServiceMessage();
					
					delay = waveManager.delay*4;
					//Wait
					try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
					count++;
				}
			}
		}
	}

	public void computeData(int directionCluster, int speedCluster, int sizeCluster, double latCluster, double lngCluster){
		//Variables
		int speed = waveManager.getSpeed();
		double longitude = waveManager.GPSlongitude;
		double lattitude = waveManager.GPSlattitude;
		
		//Calculated variables
		int trafficLevel = 0;
		double speedDiff = 30;
		double distToTraffic = 0;	
		
		//If cluster is ahead
		if(checkIfAhead(directionCluster, latCluster, lngCluster)){
				
			distToTraffic = calculateDistance(latCluster, lngCluster);
			speedDiff = speedDifference(speed, speedCluster);	
			
			//If cluster speed is smaller
			if(speed < speedCluster){
				trafficLevel = 0;	
			}else{	
				if(sizeCluster>7){
					if(speedDiff > 30){
						trafficLevel = 3;
					}else if(speedDiff > 10){
						trafficLevel = 2;
					}else{
						trafficLevel = 1;
					}
				}else{
					if(speedDiff > 40){
						trafficLevel = 3;
					}else if(speedDiff > 20){
						trafficLevel = 2;
					}else{
						trafficLevel = 1;
					}
				}
			}
			
			//Update local variable traffic level calculated
			waveManager.trafficLevel = trafficLevel;
			
			if(trafficLevel != 0){
				output = "o Calculated: Traffic level: " + trafficLevel + " ("+sizeCluster+" cars ahead " +  String.format("%.2f", distToTraffic) + "km, " + directionCluster + " degrees, " + speedCluster + "km/h)";
				waveManager.userInterface.computedTrafficInfo(output);
				waveManager.userInterface.turnOnTrafficAhead(trafficLevel, (int)distToTraffic, speedCluster);
			}else{
				output = "o Calculated: Traffic ahead is going faster";
				waveManager.userInterface.computedTrafficInfo(output);
			}
		}else{
			output = "o Calculated: Traffic Cluster is not considered because it is Behind";
			waveManager.userInterface.computedTrafficInfo(output);
		}
	}

	//CLUSTER CALCULATIONS
	public String getClusterValues(){		
		int numVehicles = waveManager.vehiclesAccountedFor.size()+1; //Add one to count yourself
		
		return  waveManager.heading+"/"+waveManager.getSpeed()+"/"+numVehicles;
	}
	
	//METHODS
	
	//Calculate difference in speed
	public static double speedDifference(int speed, int speedCluster){
		
		double d = 0;
		if(speed != 0){
			d = ((speed - speedCluster)*100)/speed;	
		}else{
			d = 0;
		}
		
		return d;
	}
}