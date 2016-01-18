
public class convenienceService {
	
	
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
