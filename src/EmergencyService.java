
public class EmergencyService extends Service {
		//Class Variables
			private WaveManager waveManager;
			
			
			//Resources
			private String controlGroup;
			private String serviceGroup;
			private String EmergType;
			
			//Constructor
			public EmergencyService(WaveManager waveManager){
				super(waveManager);
				this.waveManager = waveManager;
				this.controlGroup = waveManager.controlGroup;
			}

			//Class Methods
			public void sendControlMessage(String serviceGroup){
				sendMessage("Control", controlGroup, waveManager.CarID+"/"+serviceGroup+"/0/0");
				this.serviceGroup = serviceGroup;
			}
			
			public void sendServiceMessage(int breakAmount, String direction){
				sendMessage("Service", serviceGroup, waveManager.CarID+"/"+serviceGroup+"/"+direction+"/"+breakAmount);
			}
			
			//The check to see if I send
			public boolean checkSiren(int SirenAmount){
				if(SirenAmount != 0){			
					return true;
				}
				return false;
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
