import javax.swing.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")

public class AerialDisplay implements Runnable{
	
	//Objects
	public WaveManager waveManager;
	private Thread AerialDisplayThread;    
	private int xCoord, yCoord;
	private int dispDelay = 75;

	//UI
	private JFrame mainFrame;
    private JLabel backLabel;
	public BufferedImage img;
    
	//Class Methods
    public AerialDisplay(WaveManager waveManager){
    	
        this.waveManager=waveManager;
	    mainFrame = new JFrame();

	    mainFrame.setTitle("Aerial Display");
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    mainFrame.setBounds(0,0,screenSize.width, screenSize.height);
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setLayout(new CardLayout());

	    ImageIcon icon = null;
	   
		try {
			img = ImageIO.read(new File("Backgroundv4.png"));
			icon = new ImageIcon(img);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Temporary addition of backLabel so it doesn't start as an empty frame.
	    backLabel = new JLabel();
        backLabel.setBackground(Color.BLACK);  
        backLabel.setIcon(icon); 
        mainFrame.add(backLabel);

	    mainFrame.setVisible(true);
	    
    }
    
    public void start(){
		if(AerialDisplayThread==null){
			AerialDisplayThread = new Thread(this, "AerialDisplayThread");
			AerialDisplayThread.start();
		}
	}
    
    public void stop(){
		if(AerialDisplayThread!=null){
			AerialDisplayThread.stop();
			AerialDisplayThread = null;
		}
	}
   
    public void run(){
    	
    	int numVehicles = 10;
        int x[] = {0,0,0,0,0,0,0,0,685,615};
        int y[] = {0,0,0,0,0,0,0,0,400,400};
        
    	vehicles v = new vehicles();
    	mainFrame.remove(backLabel);
    	mainFrame.add(v);
    	
    	while(true){
    		
    		v.repaint();
    		
    		try { Thread.sleep(dispDelay); }
               catch (InterruptedException e) { }
    		
    		for(int i = 0; i<waveManager.vehiclesAccountedFor.size(); i++){
    		x[i] = coordToDisplay(waveManager.vehiclesAccountedFor.get(i).get(3));
    		y[i] = coordToDisplay(waveManager.vehiclesAccountedFor.get(i).get(4));
    		//waveManager.userInterface.output(">>>>>>>>>x,y: "+x[i]+" "+y[i]);
    		}
    		//waveManager.GPSlattitude = 
    	
    		waveManager.GPSlongitude = waveManager.GPSlongitude+waveManager.speed*2.0833/(111000*10000);
    
    		
    		y[8] = y[8] - 4;
    		y[9] = y[9] + 8;
    		if(y[8] < 50){ y[8] = 700;};
    		if(y[9] > 700){ y[9] = 50;};
    		//waveManager.userInterface.output(">>>>>>>>" + x[0]+x[8]+x[9] +" > "+  y[0]+y[8]+y[9]);
    		v.updateXY(x,y,img,numVehicles);
    	}
    }

    
    public int coordToDisplay(Object o){
    	//we'll say the road is 11.1km long, each gps point is 111km, the display is about 700 pixel high
    	//0.1 gps points to 700 pixels gives 0.000143 gps/pixel > gps * 6993
    	double gpsCoord = (double)o;
    	gpsCoord = (gpsCoord - 50)*6993; // Assume were at 50long, 50 lat
    	int position = (int) gpsCoord;
    	return position;
    }
}

class vehicles extends JPanel{

    public int w = 35;
    public int h = 35;
    
    public int x[] = {0,0,0,0,0,0,0,0,0,0};
    public int y[] = {0,0,0,0,0,0,0,0,0,0};
    
    public int numVehicles;
    public BufferedImage img = null;
    
    public void paintComponent(Graphics g) {
    				super.paintComponent(g);
    		        Graphics2D j = (Graphics2D) g;
    		        j.drawImage(img, 0, 0, null);
    		        j.setPaint(Color.MAGENTA);
    		        for(int i = 0; i<numVehicles;i++){
    		        	if(x[i]!=0 || y[i]!=0){
    		        	j.fillOval(x[i], y[i], w, h);	
    		        	}
    		        }
    		    }
    public void updateXY(int[] x, int[] y, BufferedImage img, int numVehicles){
    	this.img = img;
    	this.numVehicles = numVehicles;
    	for(int i = 0; i<numVehicles; i++){
    	this.y[i] = y[i];
    	this.x[i] = x[i];
    	}
    	
    }
}
