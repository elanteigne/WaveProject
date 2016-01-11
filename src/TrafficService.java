import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*Traffic functions


	1)Traffic routing
	Check number of vehicles in vicinity
	Check speed, direction of vehicles in vicinity 
	Check for emergency vehicles
	Check for accidents
	Make decision about current route (change or stay)
	Includes speed up, slow down, emergency notification (simple)*/

/*2) Convenience Functions

	Check surrounding vehicles lights
	Check time of day (compare to dusk, dawn, night times)
	Reuse speed/direction check
	Night time traffic (turn on headlights, high/low beam)
	Affects lights, signaling, etc.*/
	
public class TrafficService extends Service implements Runnable {
	//Class Variables
	
	private Thread trafficServiceThread;
	
	//Resources
	public int delay;
	public int messageID = 0;
	public String serviceGroup = "230.0.0.5";
	private String output;
	
	//Speed, direction (int), lights, turn signals, emergency signal, vehicle type, gps coord [x,y,z] 
	
/*private static int[][] vehicles = new int[][] {
												  { 90, 5, 0, 0, 0, 1 },
												  { 45, 1, 0, 0, 0, 0 }, 
												  { 39, 1, 0, 0, 0, 0 },
												  { 66, 6, 0, 0, 0, 0 },
												  { 0, 1, 0, 0, 1, 0 }
												};*/				
												
	private static double[][] vehicles = new double[][]{
												  { 47, 3, 1, 0, 0, 1 },//99.137
												  { 55, 3, 1, 0, 0, 0 },
												  { 43, 3, 0, 0, 0, 0 },
												  { 5, 3, 1, 0, 0, 0 },
												  { 0, 1, 2, 0, 1, 0 }
												};										

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
		// waveManager.userInterface.updateEmergencyServicePacketsSent(messageID);
	}
	
	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, "");
		 messageID++;
		// waveManager.userInterface.updateEmergencyServicePacketsSent(messageID);
	}
		
	public void run(){
		while(true){
			delay = waveManager.delay;
			System.out.println(""+waveManager.sirensOn);
			if(waveManager.sirensOn){
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
	}
	
	/*1) TRAFFIC ROUTING ALOGORITHM*/
	
	public void routeTraffic(){
		
		//Get relevant variables (# vehicles, # emergency vehicles, # emergencies,
		
		int numVehicles = checkVehicles();
		int numEVehicles = checkEVehicles();
		int numEmergencies = checkEmergency();
		int[] dir = new int[2];
		int[] dirPrv = new int[2];
		int[] spd = new int[2];
		
		String[] dirString = getAvgDir().split("/"); 
		dir[0] = Integer.parseInt(dirString[0]);
		dir[1] = Integer.parseInt(dirString[1]);
		dirPrv[0] = Integer.parseInt(dirString[2]);			
		dirPrv[1] = Integer.parseInt(dirString[3]);		
		String[] spdString = getAvgSpd(dir).split("/");
		spd[0] = Integer.parseInt(spdString[0]);
		spd[1] = Integer.parseInt(spdString[1]);
		
		int direction = directionStoI(waveManager.bearing);
		
		boolean switchRoad = false;
		boolean switchLane = false;
		String caution = "";
		
		//Approximate this vehicles direction
			
		if(direction != dir[0] && direction != dir[1]){
			if(direction-dir[1] > direction-dir[0]){direction = 0;
			}else{
				direction = 1;
				}
		}
		
		//Algorithm
		
		if(numEVehicles > 1){
			switchLane = true;
			caution = caution + "Warning: " + numEVehicles + "(s) emergency vehicles in area. Proceed with caution. ";
		}
		if(numEmergencies > 1){
			switchLane = true;
			caution = caution + "Warning: " + numEmergencies + "(s) emergencies in area. Proceed with caution. ";
		}
		
		if(dirPrv[direction] > 2){
			switchRoad = true;
			caution = caution + "Heavy Traffic ahead. Please find an alternative route. ";
			}
		
		double comparitor = waveManager.speed - spd[direction];
		if(comparitor<0){
			//waveManager.speedAdjustment = (int)(comparitor*(-1));
			//add to waveManager			
		}else{
			/*To do: increase speed */
		}
		System.out.println("Traffic:");
		System.out.println(caution);
		//switch lanes, switch road, speed up, slow down, maintain, maintain with caution (for each previous)
		//switch L:yes/no, switch road:yes/no, speedup (amount), slow down (amt), 
	}

	/*2) CONVENIENCE ALGORITHM*/
	
	public void enhanceConvenience(){
		//Speed, direction (int), lights, turn signals, emergency signal, vehicle type
		int numVehicles = checkVehicles();
	//	int numEVehicles = checkLights();
	//	int numEmergencies = checkTurns();
		int[] dir = new int[2];
		int[] dirPrv = new int[2];
		int[] spd = new int[2];
		
			String[] dirString = getAvgDir().split("/"); 
			dir[0] = Integer.parseInt(dirString[0]);
			dir[1] = Integer.parseInt(dirString[1]);
			dirPrv[0] = Integer.parseInt(dirString[2]);
			dirPrv[1] = Integer.parseInt(dirString[3]);
			String[] spdString = getAvgSpd(dir).split("/");
			spd[0] = Integer.parseInt(spdString[0]);
			spd[1] = Integer.parseInt(spdString[1]);
		
		int direction = directionStoI(waveManager.direction);
		boolean switchRoad = false;
		boolean switchLane = false;
		String caution = "";
		
	}
		//Vehicle Check
	
		public static int checkVehicles(){
			int count = vehicles.length;
			return count;
		}
		
	
	
	//Adjust lights
		
	/*checks time of day and date to determine darkness, adjusts lights accordingly
	checks for vehicles in opposite direction of vehicle. Adjusts lights accordingly
	Ie. if high beams are on, turn off if vehicle is approaching in other lane
	lights = 0,1,2,3 =  none, low, high, emergency*/
	
	public static int adjustLights(int dir[], int direction, int lights, int time, int date){
		//Set default to current lights setting
		int adj = lights;
		
		//Check for on-coming traffic and adjust accordingly
		if(lights == 2){
			if(direction == dir[0]){
				for(int i =0; i<vehicles.length; i++){
						if(vehicles[i][1] == dir[1] || vehicles[i][1] == dir[1] + 1 || vehicles[i][1] == dir[1] - 1){
						adj = 1;
						}
					}
			}else{
				for(int i =0; i<vehicles.length; i++){
						if(vehicles[i][1] == dir[0] || vehicles[i][1] == dir[0] + 1 || vehicles[i][1] == dir[0] - 1){
						adj = 1;
						}
					}
				}
		}
		
		return adj;
	}
		
	//Turn Check

	public static int checkTurns(int dir[]){
		int count = vehicles.length;
		return count;
	}
	
	/*return average flow of traffic in two directions
	Ie. Avg speed north,east,etc. (use vectors to approx. to a given direction
	approx. to the two most prevalent directions (ie. northeast, southwest)
	vehicle can check if it is heading in the same direction
	Average Vehicle direction in two directions
	To do: add two other directions for cross-traffic.*/
	
	public static String getAvgDir(){
		
		int[] direction = new int[vehicles.length];
		int[] prevalence = new int[]{0,0,0,0,0,0,0,0};
		int[] dir = new int[]{0,0};
		int[] dirPrv = new int[]{0,0};

		for(int i = 0; i<vehicles.length; i++){
			direction[i] = vehicles[i][1];
			for(int j = 0; j<8; j++){
				if(direction[i] == j){
					prevalence[j]++;
					}
				}
		}
		//if the direction k has highest appearance, update the top count to the appearance amount;
		//set previous highest value to second, highest to first (two most used directions)
		
		int top_count = 0;
		for(int w = 0; w<2; w++){
			for(int k = 0; k<8; k++){
				
				if(prevalence[k] >= top_count){
					top_count = prevalence[k];
					dir[w] = k;
					dirPrv[w] = prevalence[k];
				}
			}
			//System.out.println("Chun: " + Arrays.toString(prevalence));
			prevalence[dir[0]] = 0;
			top_count = 0;
		}
		return dir[0]+"/"+dir[1]+"/"+dirPrv[0]+"/"+dirPrv[1];
	}
	
	//Average Vehicle Speed in two directions
	//To do: add two other directions for cross-traffic.
	
	public static String getAvgSpd(int dir[]){
		 
		int speed = 0;
		int direction;
		int[] spd = new int[]{0,0};
		int[] count = new int[]{0,0};
		
		//Replaced re-running of getAvgDir to input of dir[0],dir[1], more efficient
		
		/*String avgDir = getAvgDir();
		String[] dirString = avgDir.split("/"); 
		dir[0] = Integer.parseInt(dirString[0]);
		dir[1] = Integer.parseInt(dirString[1]);*/
		
		for(int i = 0; i<vehicles.length; i++){
			
			speed = vehicles[i][0];
			direction = vehicles[i][1];
			
			if(direction != dir[0] && direction != dir[1]){
				if(direction-dir[1] > direction-dir[0]){direction = dir[0];
				}else{
					direction = dir[1];
					}
			}
			if(direction == dir[0]){
				spd[0] = spd[0] + speed;
				count[0]++;
			}else{
				spd[1] = spd[1] + speed;
				count[1]++;
			}
		}
		spd[0] = spd[0]/count[0];
		spd[1] = spd[1]/count[1];
		
		return spd[0]+"/"+spd[1];
	}
	
	//Traffic Density
	
	public static int calcTrafficDensity(){
		
		return 1;
	}
	
	//Emergency Vehicles number
	
	public static int checkEVehicles(){
		int count = 0;
		for(int i = 0; i<vehicles.length; i++){
			
			if(vehicles[i][5] == 1){ count++;}
		}
		return count;
	}
	
	//Emergency amount (crashes, etc.)
	
	public static int checkEmergency(){
		int count = 0;
		for(int i = 0; i<vehicles.length; i++){
			
			if(vehicles[i][4] == 1){ count++;}
		}
		return count;
	}
}
