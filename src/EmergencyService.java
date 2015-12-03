
	public class EmergencyService extends Service implements Runnable {
		//Class Variables
			private WaveManager waveManager;
			private String EmergType;
			
			private Thread emergencyServiceThread;
			public String serviceGroup = "230.0.0.3";
			
			//Constructor
			public EmergencyService(WaveManager waveManager){
				super(waveManager);
			}

			//Class Methods
			public void start(){
				if(emergencyServiceThread==null){
					emergencyServiceThread = new Thread(this, "EmergencyService");
					emergencyServiceThread.start();
				}
			}
			
			public void sendControlMessage(){
				sendMessage("Control", waveManager.controlGroup, serviceGroup, "0/0");
			}
			
			public void sendServiceMessage(){
				sendMessage("Service", serviceGroup, serviceGroup, ""+waveManager.breakAmount);
			}
			
			//The check to see if I send
			public boolean checkSiren(int SirenAmount){
				if(SirenAmount != 0){			
					return true;
				}
				return false;
			}
			
			public void run(){
			
			}
			//Method to calculate speed adjustment based on received packets
			public void ComputeData(String emergencytype){
				if(emergencytype .equals ("Ambulance")){
					EmergType =("Ambulance");
				}else if(emergencytype .equals ("Police")){
					EmergType=("Police Car");				
				}else if(emergencytype .equals ("Fire")){
					EmergType=("Fire Truck");				
				}else{
					EmergType=("Error : Unknown Type");				
				}
				System.out.println("There is a " + EmergType + " in your area. Be aware.");
			}

}
