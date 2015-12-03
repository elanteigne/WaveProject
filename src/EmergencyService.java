import java.util.concurrent.TimeUnit;

public class EmergencyService extends Service implements Runnable {
		//Class Variables
			private WaveManager waveManager;
			private String EmergencyVehicleType;
			
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
				sendMessage("Service", serviceGroup, serviceGroup, ""+EmergencyVehicleType);
			}
			
			//The check to see if I send
			public boolean checkSiren(){
				if(waveManager.sirensOn){			
					return true;
				}
				return false;
			}
			
			public void run(){
				if(checkSiren()){
					sendControlMessage();
					//wait 
					try{ TimeUnit.MILLISECONDS.sleep(500); } catch(Exception e){ }
					
					int count = 0;
					while(count<5){
						sendServiceMessage();
						
						//Wait
						try{ TimeUnit.MILLISECONDS.sleep(500); } catch(Exception e){ }
						count++;
					}
				}
			}
			//Method to calculate speed adjustment based on received packets
			public void computeData(String emergencytype){
				if(emergencytype .equals ("Ambulance")){
					EmergencyVehicleType =("Ambulance");
				}else if(emergencytype .equals ("Police")){
					EmergencyVehicleType=("Police Car");				
				}else if(emergencytype .equals ("Fire")){
					EmergencyVehicleType=("Fire Truck");				
				}else{
					EmergencyVehicleType=("Error : Unknown Type");				
				}
				System.out.println("There is a " + EmergencyVehicleType + " in your area. Be aware.");
			}

}
