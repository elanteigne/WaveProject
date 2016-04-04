import java.text.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unused")

public class TrafficService extends Service implements Runnable {
	//Class Variables

	//Resources

	//Global
	public String clusterVariables = "";
	public enum compassDirections {N,NNE,NE,NEE,E,SEE,SE,SSE,S,SSW,SW,SWW,W,NWW,NW,NNW};

	//Constructor
	public TrafficService(WaveManager waveManager){
		super(waveManager);
		this.updateRunConditions();
		this.updateMsgDelays();
		this.serviceGroup = "230.0.0.5";
		this.threadName = "TrafficService";
		runAlgorithm = this;
	}

	public void updateRunConditions(){
		this.runCondition1 = waveManager.getTrafficValue();
		this.runCondition2 = true;
	}
	
	public void updateMsgDelays(){
		this.delay = waveManager.delay;
		this.controlMsgDelay = delay*2;
		this.serviceMsgDelay = delay*4;
	}

	public void sendControlMessage(){
		this.clusterVariables = getClusterValues();
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		messageID++;
		waveManager.userInterface.updateTrafficServicePacketsSent(messageID);
	}

	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, ""+clusterVariables);
		messageID++;
		waveManager.userInterface.updateTrafficServicePacketsSent(messageID);
	}


	public void computeData(ArrayList<Object> params){
		//Variables 
		int direction = (int)(double)(waveManager.heading/22.5);
		int speed = waveManager.getSpeed();
		double longitude = waveManager.GPSlongitude;
		double lattitude = waveManager.GPSlattitude;
		
		//Parameters
		int directionCluster = (int) params.get(0);
		int speedCluster = (int) params.get(1);
		int sizeCluster = (int) params.get(2);
		double latCluster = (double) params.get(3);
		double lngCluster = (double) params.get(4);

		//Calculated variables
		int trafficLevel = 0;
		double speedDiff = 30;
		double distToTraffic = 0;	

		//Output variables
//		String[] directionWords = {"N","NNE", "NE", "NEE", "E", "SEE", "SE", "SSE", "S","SSW", "SW", "SWW","W", "NWW", "NW", "NNW"};

		//TRAFFIC LEVEL ALGORITHM
		//If cluster in the same direction
		if(direction == directionCluster || (direction == directionCluster + 1) || (direction == directionCluster - 1) ){

			//If cluster is ahead

			if(checkIfAhead((int)(directionCluster*22.5), latCluster, lngCluster)){

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
						}else{
							trafficLevel = 1;
						}
					}else if(sizeCluster>3){
						if(speedDiff > 40){
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
					//output = "o Calculated: Traffic level: " + trafficLevel + ". Traffic ahead in: " +  String.format("%.2f", distToTraffic) + "km (" + directionWords[directionCluster] + ") at " + speedCluster + "km/h";
					output = "o Calculated: Traffic level: " + trafficLevel + ". Traffic ahead in: " +  String.format("%.2f", distToTraffic*1000)+ "m (" + compassDirections.values()[directionCluster] + ") at " + speedCluster + "km/h";
					waveManager.userInterface.computedTrafficInfo(output);

				}else{
					output = "o Calculated: Traffic ahead is going faster or the same speed";
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

		int[] mostPrevalentDirections = new int[2];
		int[] numberVehiclesInDirections = new int[2];
		int[] speedInDirections = new int[2];
		int[] vehicleCount = new int[2];
		double[] avgGpsVehicles = new double[2];

		//Get most Prevalent directions
		String[] dirString = getAvgDir(vehicles, numVehicles).split("/");
		mostPrevalentDirections[0] = Integer.parseInt(dirString[0]);
		mostPrevalentDirections[1] = Integer.parseInt(dirString[1]);
		numberVehiclesInDirections[0] = Integer.parseInt(dirString[2]);			
		numberVehiclesInDirections[1] = Integer.parseInt(dirString[3]);

		//Approximate direction of local vehicle to prevalent directions
		if(mostPrevalentDirections[0] != mostPrevalentDirections[1]){
			if(Math.abs(direction-mostPrevalentDirections[1]) > Math.abs(direction-mostPrevalentDirections[0])){
				direction = 0;
			}else{
				direction = 1;
			}
		}else {
			if(numberVehiclesInDirections[0]>numberVehiclesInDirections[1]){//TO do: if directions are equal and local isnt close at all
				direction = 0;
			}else{
				direction = 1;
			}
		}
		numberVehiclesInDirections[direction]++;

		//Get Average speed for both directions (including this vehicle)
		String[] spdString = getAvgSpd(vehicles, numVehicles, mostPrevalentDirections, numberVehiclesInDirections, speed, direction).split("/");
		speedInDirections[0] = Integer.parseInt(spdString[0]);
		speedInDirections[1] = Integer.parseInt(spdString[1]);
		vehicleCount[0] = Integer.parseInt(spdString[2]);
		vehicleCount[1] = Integer.parseInt(spdString[3]);

		//Get Average GPS
		String[] gpsString = getAvgGPS(vehicles, numVehicles, longitude, lattitude, mostPrevalentDirections[direction]).split("/");
		avgGpsVehicles[0] = Double.parseDouble(gpsString[0]);
		avgGpsVehicles[1] = Double.parseDouble(gpsString[1]);

		output = "o Calculated: Speed: " + speedInDirections[direction] + "; Heading: " + (int)(mostPrevalentDirections[direction]*22.5) + "; Size: " + vehicleCount[direction] + "; LAT/LNG: " + String.format("%.7f", avgGpsVehicles[0]) + " / " + String.format("%.7f", avgGpsVehicles[1]);	
		waveManager.userInterface.computedTrafficInfo(output);

		return  mostPrevalentDirections[direction]  + "/" + speedInDirections[direction] + "/" + vehicleCount[direction] + "/" + avgGpsVehicles[0] + "/" + avgGpsVehicles[1];
	}

	//METHODS

	//Calculate difference in speed
	public static double speedDifference(int speed, int speedCluster){
		if(speed != 0){
			return ((speed - speedCluster)*100)/speed;	
		}else{
			return 0;
		}
	}

	/*Avg direction to the two most prevalent directions
	To do: add two other directions for cross-traffic.*/	//SEEK CHRISTIAN
	public static String getAvgDir(ArrayList<ArrayList<Object>> vehicles, int vLength ){

		int[] prevalence = new int[16];
		Arrays.fill(prevalence, 0);
		int[] laneDir = new int[]{0,0};
		int[] dirPrv = new int[]{0,0};

		if(vLength == 1){
			laneDir[0] = (int)(double)((int)vehicles.get(0).get(1)/22.5);
			dirPrv[0] = 1;
		}else{
			for(ArrayList<Object> v:vehicles){
				if((int)v.get(1) < 11.25 || (int)v.get(1) > 348.75){
					prevalence[0]++;
					}
				for(int j = 1; j<prevalence.length; j++){
					if((int)v.get(1) < j*22.5 + 11.25 && (int)v.get(1) > j*22.5 - 11.25){
						prevalence[j]++;
					}
				}
			}
			//if the direction k has highest appearance, update the top count to the appearance amount;
			//set previous highest value to second, highest to first (two most used directions)

			for(int w = 0; w<2; w++){
				int top_count = 0;
				for(int k = 0; k<prevalence.length; k++){
					if(prevalence[k] >= top_count){
						top_count = prevalence[k];
						laneDir[w] = k;
						dirPrv[w] = prevalence[k];
					}
				}
				prevalence[laneDir[0]] = 0;
			}
		}
		return laneDir[0]+"/"+laneDir[1]+"/"+dirPrv[0]+"/"+dirPrv[1];
	}

	//Average Vehicle Speed in two directions
	//To do: add two other directions for cross-traffic.        SEEK CHRISTIAN
	public static String getAvgSpd(ArrayList<ArrayList<Object>> vehicles, int vLength, int laneDir[], int dirPrv[], int thisVehicleSpeed, int thisVehicleDirection){
		int speed = 0;
		int direction; //double d;
		int[] spd = new int[]{0,0};
		int[] count = new int[]{0,0};

		//Approximate this vehicle's direction to add it's speed
		spd[thisVehicleDirection] += thisVehicleSpeed;
		count[thisVehicleDirection]++;

		//Average Vehicle Array speeds
		if(vLength>=1){
			for(ArrayList<Object> v:vehicles){
				speed = (int)v.get(2);
				direction = (int)((double)((int)v.get(1))/22.5);

				//Approx. direction of current vehicle to two lane directions
				if(direction == laneDir[0] || (Math.abs(direction-laneDir[1]) < Math.abs(direction-laneDir[0]))){
					spd[0] += speed;
					count[0]++;
				}else if(direction == laneDir[1]||(Math.abs(direction-laneDir[1]) > Math.abs(direction-laneDir[0]))){
					spd[1] += speed;
					count[1]++;
				}
			}

			if(count[0]!=0){
				spd[0]/=count[0];
			}
			if(count[1]!=0){
				spd[1]/=count[1];
			}
		}else if(vLength == 1){

			speed = (int)vehicles.get(0).get(2);
			direction = (int)((double)((int)vehicles.get(0).get(1))/22.5);
			if(((direction == laneDir[thisVehicleDirection])^(thisVehicleDirection == laneDir[0]))){
					spd[1]+=speed;
					count[1]++;	
			}else{
					spd[0]+=speed;
					count[0]++;
			}
			if(count[0]!=0){
				spd[0]/=count[0];
			}
			if(count[1]!=0){
				spd[1]/=count[1];
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
		return spd[0]+"/"+spd[1]+"/"+count[0]+"/"+count[1]+"/";//To-do: Re-add count here to keep track of number vehicles that have been approximated to the directions*
	}

	//Avg GPS coords of vehicles in same direction
	public static String getAvgGPS(ArrayList<ArrayList<Object>> vehicles, int vLength, double longitude, double lattitude, int direction){
		double lng = longitude;
		double lat = lattitude;
		int count = 1;
		
		for(ArrayList<Object> v:vehicles){
			if((int)v.get(1)/22.5 > direction-1 || (int)v.get(1)/22.5 + 1 < direction+1){
				lat +=  (double)v.get(3);
				lng +=  (double)v.get(4);
				count++;
			}
		}
		return lat/count + "/" + lng/count;
	}
}