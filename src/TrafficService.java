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
			delay = waveManager.delay*4;
			
			sendControlMessage();
			//Wait 
			try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
			if(waveManager.inTraffic == true){
				int count = 0;
				while(count<2){
					this.clusterVariables = getClusterValues();
					//waveManager.userInterface.computedTrafficInfo("Here");
					sendServiceMessage();
					
					delay = waveManager.delay;
					//Wait
					try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
					count++;
				}
			}
		}
	}

	public void computeData(int directionCluster, int speedCluster, int sizeCluster, double latCluster, double lngCluster){
		//Variables
		int direction = (int)(double)(waveManager.heading/22.5);
		int speed = waveManager.getSpeed();
		double longitude = waveManager.GPSlongitude;
		double lattitude = waveManager.GPSlattitude;
		
		//Calculated variables
		int trafficLevel = 0;
		double speedDiff = 0;
		double distToTraffic = 0;	
		
		//Output variables
		String[] directionWords = {"N","NNE", "NE", "NEE", "E", "SEE", "SE", "SSE", "S","SSW", "SW", "SWW","W", "NWW", "NW", "NNW"};
		
		//TRAFFIC LEVEL ALGORITHM
		
		//If cluster in the same direction
		if(direction == directionCluster || direction == directionCluster + 1 || direction == directionCluster - 1 ){
			
			distToTraffic = calculateDistance(latCluster, lngCluster);
			speedDiff = speedDifference(speed, speedCluster);
		
			//If cluster speed is smaller
			if(speed < speedCluster){
				trafficLevel = 0;	
			}else{	
				if(sizeCluster>9){
					if(speedDiff > 30){
						trafficLevel = 3;
					}else if(speedDiff > 10){
						trafficLevel = 2;
					}else{
						trafficLevel = 1;
					}
				}else if(sizeCluster>6){
					if(speedDiff > 40){
						trafficLevel = 3;
					}else if(speedDiff > 20){
						trafficLevel = 2;
					}else if(speedDiff > 10){
						trafficLevel = 1;
					}else{
						trafficLevel = 1;
					}
				}else if(sizeCluster>3){
					if(speedDiff > 30){
						trafficLevel = 2;
					}else if(speedDiff > 20){
						trafficLevel = 1;
					}else{
						trafficLevel = 0;
					}
				}else{
					if(speedDiff > 40){
						trafficLevel = 2;
					}else if(speedDiff > 30){
						trafficLevel = 1;
					}else{
						trafficLevel = 0;
					}
				}
			}
		//Update local variable traffic level calculated
		waveManager.trafficLevel = trafficLevel;

		output = "o Calculated: Traffic level: " + trafficLevel + ". Traffic ahead in: " +  String.format("%.2f", distToTraffic) + "km (" + directionWords[directionCluster] + ") at " + speedCluster + "km/h";
		waveManager.userInterface.computedTrafficInfo(output);
		waveManager.userInterface.turnOnTrafficAhead(trafficLevel, (int)distToTraffic, speedCluster);
		}
	}

	//CLUSTER CALCULATIONS
	public String getClusterValues(){
		int direction = (int)(double)(waveManager.heading/22.5);
		int speed = waveManager.getSpeed();
		double longitude = waveManager.GPSlongitude;
		double lattitude = waveManager.GPSlattitude;
		
		int numVehicles = waveManager.vehiclesAccountedFor.size();
		ArrayList<ArrayList<Object>> vehicles = waveManager.vehiclesAccountedFor;
		
		int[] dir = new int[2];
		int[] dirPrv = new int[2];
		int[] spd = new int[2];
		double[] gps = new double[2];
		
		String[] dirString = getAvgDir(vehicles, numVehicles).split("/");
		dir[0] = Integer.parseInt(dirString[0]);
		dir[1] = Integer.parseInt(dirString[1]);
		dirPrv[0] = Integer.parseInt(dirString[2]);			
		dirPrv[1] = Integer.parseInt(dirString[3]);

		String[] spdString = getAvgSpd(vehicles, numVehicles, dir).split("/");
		spd[0] = Integer.parseInt(spdString[0]);
		spd[1] = Integer.parseInt(spdString[1]);
		
		//Approximate direction of local vehicle (closest direction)
		if(Math.abs(direction-dir[1]) > Math.abs(direction-dir[0])){
			direction = 0;
		}else{
			direction = 1;
		}
		
		String[] gpsString = getAvgGPS(vehicles, numVehicles, longitude, lattitude, dir[direction]).split("/");
		gps[0] = Double.parseDouble(gpsString[0]);
		gps[1] = Double.parseDouble(gpsString[1]);
		
		output = "o Calculated: Speed: " + spd[direction] + "; Heading: " + (int)(dir[direction]*22.5) + "; LAT/LNG: " + String.format("%.7f", gps[0]) + " / " + String.format("%.7f", gps[1]);
		waveManager.userInterface.computedTrafficInfo(output);
	
		return  dir[direction]  + "/" + spd[direction] + "/" + dirPrv[direction] + "/" + gps[0] + "/" + gps[1];
	}
	
	//METHODS
	
	//Calculate difference in speed
	public static double speedDifference(int s1,int s2){
		
		double d = 0;
		if(s1 != 0){
			d = ((s1 - s2)*100)/s1;	
		}else{
			d = 7;
		}
		
		return d;
	}
	
	/*Avg direction to the two most prevalent directions
	To do: add two other directions for cross-traffic.*/	
	public static String getAvgDir(ArrayList<ArrayList<Object>> vehicles, int vLength ){
		
		int[] direction = new int[vLength];
		int[] prevalence = new int[16];
		Arrays.fill(prevalence, 0);
		int[] laneDir = new int[]{0,0};
		int[] dirPrv = new int[]{0,0};
		
		ArrayList<Object> v = new ArrayList<Object>();
		
		for(int i = 0; i<vLength; i++){
			v = vehicles.get(i);
			direction[i] = (int)v.get(1);
			
			int j =0;
			if(direction[i] < 11.25 || direction[i] > 348.75){prevalence[0]++;}
			
			for(j = 1; j<16; j++){
				if(direction[i] < j*22.5 + 11.25 && direction[i] > j*22.5 - 11.25){
					prevalence[j]++;
					}
				}
		}
		//if the direction k has highest appearance, update the top count to the appearance amount;
		//set previous highest value to second, highest to first (two most used directions)
		
		int top_count = 0;
		for(int w = 0; w<2; w++){
			for(int k = 0; k<16; k++){
				
				if(prevalence[k] >= top_count){
					top_count = prevalence[k];
					laneDir[w] = k;
					dirPrv[w] = prevalence[k];
				}
			}
			prevalence[laneDir[0]] = 0;
			top_count = 0;
		}
		return laneDir[0]+"/"+laneDir[1]+"/"+dirPrv[0]+"/"+dirPrv[1];
	}
	
	//Average Vehicle Speed in two directions
	//To do: add two other directions for cross-traffic.
	public static String getAvgSpd(ArrayList<ArrayList<Object>> vehicles, int vLength, int laneDir[]){
		int speed = 0;
		int direction; //double d;
		int[] spd = new int[]{0,0};
		int[] count = new int[]{0,0};
		ArrayList<Object> v = new ArrayList<Object>();
		
		if(vLength>1){
			
			for(int i = 0; i<vLength; i++){
				
				v = vehicles.get(i);
				speed = (int)v.get(2);
				direction = (int)((double)((int)v.get(1))/22.5);

				//Approx. direction of current vehicle to two lane directions
				if(direction != laneDir[0] && direction != laneDir[1]){
					if(direction-laneDir[1] > direction-laneDir[0]){direction = laneDir[0];
					}else{
						direction = laneDir[1];
						}
				}
				
				//Compare to the lanes (add to respective lane speed)
				if(direction == laneDir[0]){
					spd[0] = spd[0] + speed;
					count[0]++;
				}else{
					spd[1] = spd[1] + speed;
					count[1]++;
				}
			}
			
			if(count[0] != 0){
				spd[0] = spd[0]/count[0];
			}
			if(count[1] != 0){
				spd[1] = spd[1]/count[1];
			}

		}else{
			v = vehicles.get(0);
			spd[0] = (int)v.get(2);
			spd[1] = 0;
		}
		
		return spd[0]+"/"+spd[1];
	}
	
	//Avg GPS coords of vehicles in same direction
	public static String getAvgGPS(ArrayList<ArrayList<Object>> vehicles, int vLength, double longitude, double lattitude, int direction){
		double lng = longitude;
		double lat = lattitude;
		int count = 1;
		
		ArrayList<Object> v = new ArrayList<Object>();
		
		for(int i = 0; i<vehicles.size(); i++){
			v = vehicles.get(i);
			
			if((int)v.get(1)/22.5 > direction-1 || (int)v.get(1)/22.5 + 1 < direction+1){
				lat = lat + (double)v.get(3);
				lng = lng + (double)v.get(4);
				count++;
			}
		}
		
		lat = lat/count;
		lng = lng/count;

		return lat + "/" + lng;
	}
}