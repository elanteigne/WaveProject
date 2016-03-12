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
				waveManager.userInterface.computedTrafficInfo(clusterVariables);
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
		int direction = (int)(double)(waveManager.heading/22.5);
		int speed = waveManager.getSpeed();
		double longitude = waveManager.GPSlongitude;
		double lattitude = waveManager.GPSlattitude;
		
		//Calculated variables
		int trafficLevel = 0;
		double speedDiff = 30;
		double distToTraffic = 0;	
		
		//Output variables
		String[] directionWords = {"N","NNE", "NE", "NEE", "E", "SEE", "SE", "SSE", "S","SSW", "SW", "SWW","W", "NWW", "NW", "NNW"};
		
		//TRAFFIC LEVEL ALGORITHM
		//If cluster in the same direction
		if(direction == directionCluster || direction == directionCluster + 1 || direction == directionCluster - 1 ){
			
			//If cluster is ahead
			if(checkIfAhead((int)(directionCluster*22.5), latCluster, lngCluster)){
				
				distToTraffic = calculateDistance(latCluster, lngCluster);
				speedDiff = speedDifference(speed, speedCluster);				
				
				output = "Cluster size: " + sizeCluster + ". Speed: "+speed+". Cluster Speed: "+speedCluster+". Speed Diff: " +  speedDiff+"%";
				waveManager.userInterface.computedTrafficInfo(output);
				
				//If cluster speed is smaller
				if(speed < speedCluster){
					trafficLevel = 0;	
				}else{	
					if(sizeCluster>9){
						if(speedDiff > 40){
							trafficLevel = 3;
						}else if(speedDiff > 30){
							trafficLevel = 3;
						}else if(speedDiff > 20){
							trafficLevel = 2;
						}else if(speedDiff > 10){
							trafficLevel = 2;
						}else{
							trafficLevel = 1;
						}
					}else if(sizeCluster>6){
						if(speedDiff > 40){
							trafficLevel = 3;
						}else if(speedDiff > 30){
							trafficLevel = 2;
						}else if(speedDiff > 20){
							trafficLevel = 2;
						}else if(speedDiff > 10){
							trafficLevel = 1;
						}else{
							trafficLevel = 1;
						}
					}else if(sizeCluster>3){
						if(speedDiff > 40){
							trafficLevel = 2;
						}else if(speedDiff > 30){
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
				
				if(trafficLevel != 0){
					output = "o Calculated: Traffic level: " + trafficLevel + ". Traffic ahead in: " +  String.format("%.2f", distToTraffic) + "km (" + directionWords[directionCluster] + ") at " + speedCluster + "km/h";
					waveManager.userInterface.computedTrafficInfo(output);
					waveManager.userInterface.turnOnTrafficAhead(trafficLevel, (int)distToTraffic, speedCluster);

				}else{
					output = "o Calculated: Traffic ahead is going the same speed";
					waveManager.userInterface.computedTrafficInfo(output);
				}
			}else{
				output = "o Calculated: Traffic Cluster is not considered because it is Behind";
				waveManager.userInterface.computedTrafficInfo(output);
			}
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
		
		//Get most Prevalent directions
		String[] dirString = getAvgDir(vehicles, numVehicles).split("/");
		dir[0] = Integer.parseInt(dirString[0]);
		dir[1] = Integer.parseInt(dirString[1]);
		dirPrv[0] = Integer.parseInt(dirString[2]);			
		dirPrv[1] = Integer.parseInt(dirString[3]);

		//Approximate direction of local vehicle to prevalent directions
		if(dir[0] != dir[1]){
			if(Math.abs(direction-dir[1]) > Math.abs(direction-dir[0])){
				direction = 0;
			}else{
				direction = 1;
			}
		}else{//TO do: if directions are equal and local isnt close at all
			if(dirPrv[0]>dirPrv[1]){
				direction = 0;
			}else{
				direction = 1;
			}
		}
		dirPrv[direction]++;
		
		//Get Average speed for both directions (including this vehicle)
		String[] spdString = getAvgSpd(vehicles, numVehicles, dir, speed, direction).split("/");
		spd[0] = Integer.parseInt(spdString[0]);
		spd[1] = Integer.parseInt(spdString[1]);
		
		//Get Average GPS
		String[] gpsString = getAvgGPS(vehicles, numVehicles, longitude, lattitude, dir[direction]).split("/");
		gps[0] = Double.parseDouble(gpsString[0]);
		gps[1] = Double.parseDouble(gpsString[1]);
		
		output = "o Calculated: Speed: " + spd[direction] + "; Heading: " + (int)(dir[direction]*22.5) + "; Size: " + dirPrv[direction] + "; LAT/LNG: " + String.format("%.7f", gps[0]) + " / " + String.format("%.7f", gps[1]);
		waveManager.userInterface.computedTrafficInfo(output);
		
		return  dir[direction]  + "/" + spd[direction] + "/" + dirPrv[direction] + "/" + gps[0] + "/" + gps[1];
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
	
	/*Avg direction to the two most prevalent directions
	To do: add two other directions for cross-traffic.*/	
	public static String getAvgDir(ArrayList<ArrayList<Object>> vehicles, int vLength ){
		
		int[] direction = new int[vLength];
		int[] prevalence = new int[16];
		Arrays.fill(prevalence, 0);
		int[] laneDir = new int[]{0,0};
		int[] dirPrv = new int[]{0,0};
		
		ArrayList<Object> v = new ArrayList<Object>();
		
		if(vLength == 1){
			v = vehicles.get(0);
			laneDir[0] = (int)(double)((int)v.get(1)/22.5);
			dirPrv[0] = 1;
		}else{
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
		}
		
		return laneDir[0]+"/"+laneDir[1]+"/"+dirPrv[0]+"/"+dirPrv[1];
	}
	
	//Average Vehicle Speed in two directions
	//To do: add two other directions for cross-traffic.
	public static String getAvgSpd(ArrayList<ArrayList<Object>> vehicles, int vLength, int laneDir[], int thisVehicleSpeed, int thisVehicleDirection){
		int speed = 0;
		int direction; //double d;
		int[] spd = new int[]{0,0};
		int[] count = new int[]{0,0};
		ArrayList<Object> v = new ArrayList<Object>();
		
		//Approximate this vehicle's direction to add it's speed
		spd[thisVehicleDirection] = spd[thisVehicleDirection] + thisVehicleSpeed;
		count[thisVehicleDirection]++;
			
		//Average Vehicle Array speeds
		if(vLength>=1){
			for(int i = 0; i<vLength; i++){
				v = vehicles.get(i);
				speed = (int)v.get(2);
				direction = (int)((double)((int)v.get(1))/22.5);

				//Approx. direction of current vehicle to two lane directions
				if(direction != laneDir[0] && direction != laneDir[1]){
					if(Math.abs(direction-laneDir[1]) > Math.abs(direction-laneDir[0])){
						spd[0] = spd[0]+speed;
						count[0]++;
					}else{
						spd[1] = spd[1] + speed;
						count[1]++;
						}
				}
			}
			if(count[0]!=0){
			spd[0]=spd[0]/count[0];
			}
			if(count[1]!=0){
			spd[1]=spd[1]/count[1];
			}
		}else if(vLength == 1){
			
			v = vehicles.get(0);
			speed = (int)v.get(2);
			direction = (int)((double)((int)v.get(1))/22.5);
			
			if((direction == laneDir[thisVehicleDirection])){
				if(thisVehicleDirection == laneDir[0]){
					spd[0]=spd[0]+speed;
					count[0]++;
				}else{
					spd[1]=spd[1]+speed;
					count[1]++;
				}
			}else{
				if(thisVehicleDirection == laneDir[0]){
					spd[1]=spd[1]+speed;
					count[1]++;
				}else{
					spd[0]=spd[0]+speed;
					count[0]++;
				}
			}
			
			if(count[0]!=0){
				spd[0]=spd[0]/count[0];
			}
			if(count[1]!=0){
			spd[1]=spd[1]/count[1];
			}
		}else{
			
			if(thisVehicleDirection == laneDir[0]){
				spd[0] = thisVehicleSpeed;
				spd[1]= 0;
				count[1] = 0;
			}else{
				spd[1] = thisVehicleSpeed;
				spd[0]= 0;
				count[0] = 0;
			}
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