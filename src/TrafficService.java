import java.text.*;
import java.util.*;

public class TrafficService extends Service{
	//Class Variables
	private WaveManager waveManager;
	
	//Resources
	private String controlGroup;
	private String serviceGroup;
	
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
		this.waveManager = waveManager;
		this.controlGroup = waveManager.controlGroup;
	}

	//Class Methods
	public void sendControlMessage(String serviceGroup){
		sendMessage(controlGroup, serviceGroup, "");
		this.serviceGroup = serviceGroup;
	}
	
	public void sendServiceMessage(int breakAmount, String direction){
		sendMessage(serviceGroup, serviceGroup, "");
	}
	
	//Traffic functions
	
	//1)Traffic routing
	
	//Check number of vehicles in vicinity
	//Check speed, direction of vehicles in vicinity 
	//Check for emergency vehicles
	//Check for accidents
	//Make decision about current route (change or stay)
	//Includes speed up, slow down, emergency notification (simple)
	
	/*1) TRAFFIC ROUTING ALOGORITHM*/
	
	public void routeTraffic(){
		
		//Get relevant variables
		
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
			//String[] spdString = getAvgSpd().split("/");
			String[] spdString = getAvgSpd(dir).split("/");
			spd[0] = Integer.parseInt(spdString[0]);
			spd[1] = Integer.parseInt(spdString[1]);
		
		int direction = directionStoI(waveManager.direction);
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
			waveManager.speedAdjustment = (int)(comparitor*(-1));
						
		}else{
			/*To do: increase speed */
		}
		System.out.println("Traffic:");
		System.out.println(caution);
		//switch lanes, switch road, speed up, slow down, maintain, maintain with caution (for each previous)
		//switch L:yes/no, switch road:yes/no, speedup (amount), slow down (amt), 
	}

	//2) Convenience 
	
	//Check surrounding vehicles lights
	//Check time of day (compare to dusk, dawn, night times)
	//Reuse speed/direction check
	//Night time traffic (turn on headlights, high/low beam)
	//Affects lights, signaling, etc.
	
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
		//String[] spdString = getAvgSpd().split("/");
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
	
	//Date and Time
	
	public static String getDateAndTime(){
	
		int dayNumb = 0;
		int time = 0;
	   	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	   	Calendar cal = Calendar.getInstance();
	   	Date date = new Date();
	   
	   	String[] temp = dateFormat.format(date).split(" ");
	   	String[] d = temp[0].split("/");
	   	int year = Integer.parseInt(d[0]);
	   	int month = Integer.parseInt(d[1]);
	   	int day = Integer.parseInt(d[2]);
	   	
	   	String[] t = temp[1].split(":");
	   	int hr =  Integer.parseInt(t[0]);
	   	int min = Integer.parseInt(t[1]);
	   	time = hr*100 + min;
	   	
	   	for(int i = 0; i<month; i++){
	   		if(i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12){
	   			dayNumb = dayNumb + 31;
	   		}else{
	   			if(i == 2){
	   				if(year%100 == 0 || year%4 == 0){
	   					dayNumb = dayNumb + 29;
	   				}else{
	   					dayNumb = dayNumb + 28;
	   				}
	   			}else{
	   				if(i != 0){
	   					dayNumb = dayNumb + 30;	
	   				}
	   			}
	   		}
	   	}
	   	dayNumb = dayNumb + day;
	   	return dayNumb+"/"+time;
	}
	
	//Adjust lights
	/*checks time of day and date to determine darkness, adjusts lights accordingly
	checks for vehicles in opposite direction of vehicle. Adjusts lights accordingly
	Ie. if high beams are on, turn off if vehicle is approaching in other lane
	lights = 0,1,2,3 =  none, low, high, emergency*/
	
	public static int adjustLights(int dir[], int direction, int lights, int time, int date){
		//Set default to current lights setting
		int adj = lights;
		
		//Check for time of day and adjust accordingly
		
		//get date, get time (date in days (365), time in 24hr clock (ie 1200))

		//late autumn to early winter, dusk/dawn uses low beams, high otherwise
		//if(date >=354 || date <= 82){
		if(date >=309 || date <= 37){
			if(time>=1630 || time <= 830){
				if(time>=1730 || time <= 730){
				adj = 2;
				}else{
				adj = 1;
				}
			}
		}
		//late winter to early spring & late summer to early autumn
		//if( (date >=83 || date <= 171) || (date >=263 || date <= 353) ){
		if( (date >=38 || date <= 126) || (date >=218 || date <= 308) ){
			if(time>=1830 || time <= 630){
				if(time>=1930 || time <= 530){
				adj = 2;
				}else{
				adj = 1;
				}
			}
		}
		//late spring to early summer
		//if(date >=172 || date <= 262){
		if(date >=127 || date <= 217){
			if(time>=2030 || time <= 430){
				if(time>=2130 || time <= 330){
				adj = 2;
				}else{
				adj = 1;
				}
			}
		}
		
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
		//int[] dir = new int[2];
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
	
	//Longitutde to meters
	
	public static double lngToMeters(double lat, double lng){
		
		if(lat < 0){ lat = lat*(-1);};
		
		return 22.22;
	}
	
	//Latitude to meters
	
	public static double latToMeters(){
		
		return 22.22;
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
	
	//String direction to int direction
	
	public static int directionStoI(String s){
		/*To do: add case for when string is incorrect */
		int n = 9;
		if(s.equals("N")){n = 0;};
		if(s.equals("NE")){n = 1;};
		if(s.equals("E")){n = 2;};
		if(s.equals("SE")){n = 3;};
		if(s.equals("S")){n = 4;};
		if(s.equals("SW")){n = 5;};
		if(s.equals("W")){n = 6;};
		if(s.equals("NW")){n = 7;};
		return n;
	}
}
