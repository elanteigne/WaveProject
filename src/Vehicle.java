public class Vehicle extends Thread{
		public String CarID;
		public double GPSlattitude;
		public double GPSlongitude;
		public int speed;
		public int brakeAmount;
		public int bearing;
		public String vehicleType;
		public boolean sirensOn;

	   // constructor
	   public Vehicle(String CarID, double GPSlattitude, double GPSlongitude, int speed, int brakeAmount, int bearing, String vehicleType, boolean sirensOn) {
	      this.CarID = CarID;
		  this.GPSlattitude = GPSlattitude;
	      this.GPSlongitude = GPSlongitude;
	      this.speed = speed;
	      this.brakeAmount = brakeAmount;
	      this.bearing = bearing;
	      this.vehicleType = vehicleType;
	      this.sirensOn = sirensOn;
	   }
	       /*// getters
	       public String getCarID() { return CarID; }
	       public double getGPSlat() { return GPSlattitude; }
	       public double getGPSlng() { return GPSlongitude; }
	       public int getSpeed() { return speed; }
	       
	       // setters*/
}

//Speed, direction (int), lights, turn signals, emergency signal, vehicle type, gps coord [x,y,z] 
	/*private static int[][] vehicles = new int[][] {
												  { 90, 5, 0, 0, 0, 1 },
												  { 45, 1, 0, 0, 0, 0 }, 
												  { 39, 1, 0, 0, 0, 0 },
												  { 66, 6, 0, 0, 0, 0 },
												  { 0, 1, 0, 0, 1, 0 }
												};*/