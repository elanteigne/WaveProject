import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class UserInterface implements Runnable, ActionListener{
	//Objects
	public WaveManager waveManager;
	private Thread userInterfaceThread;    
	
	//UI Objects
	private JFrame mainFrame;
	private JFrame devFrame;
    
    private JPanel mainPanel;
    private JPanel devPanel;
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel staticDataPanel;
    private JPanel variableDataPanel;
    private JPanel calculatedInfoPanel;
    private JPanel buttonPanel;
    private JPanel outputPanel;
    private JPanel packetInfoPanel;
    private JPanel otherInfoPanel;
    private JPanel delayPanel;
    private JPanel controlsPanel;
    private JPanel sentPacketInfoPanel;
    private JPanel receivedPacketInfoPanel;
    private JPanel computedDataPanel;
    private JPanel leftComputedDataPanel;
    private JPanel rightComputedDataPanel;
    private JPanel speedometerPanel;
    
    private ImageIcon carAheadRed;
    private ImageIcon carAheadOrange;
    private ImageIcon carAheadYellow;
    private ImageIcon carBehindRed;
    private ImageIcon carBehindOrange;
    private ImageIcon carBehindYellow;
    private ImageIcon brakingRed;
    private ImageIcon brakingOrange;
    private ImageIcon brakingYellow;
    private ImageIcon sirenIcon;
    
    private JLabel leftPanelLabel;
    private JLabel centerPanelLabel;
    private JLabel rightPanelLabel;
    private JLabel carID;
    private JLabel gps;
    private JLabel heading;
    private JLabel speed;
    private JLabel brakeAmount;
    private JLabel vehicleType;
    private JLabel suggestedSpeedAdjustment;
    private JLabel suggestedSpeedAdjustmentValue;
    private JLabel generalInfoCarAhead;
    private JLabel generalInfoCarAheadSpeed;
    private JLabel generalInfoCarBehind;
    private JLabel generalInfoCarBehindSpeed;
    private JLabel brakingCarAhead;
    private JLabel emergencySiren;
    private JLabel generalInfoPacketsSent;
    private JLabel brakeServicePacketsSent;
    private JLabel emergencyServicePacketsSent;
    private JLabel trafficServicePacketsSent;
    private JLabel numPacketsReceived;
    private JLabel numPacketsPassed;
    private JLabel numPacketsOmitted;
    private JLabel sender;
    private JLabel receiver;
    private JLabel delay;
    private JLabel sirensLabel;
    private JLabel groupsListeningToLabel;
    
    private JButton speedUpButton;
    private JButton speedDownButton;
    private JButton brakeUpButton;
    private JButton brakeDownButton;
    private JButton sirenButton;
    private JButton delayDownButton;
    private JButton delayUpButton;

    private JTextArea output;
    private JScrollPane consoleScroll;
    
    private JTextArea computedGeneralInfo;
    private JScrollPane computedGeneralInfoScroll;
    private JTextArea computedBrakeInfo;
    private JScrollPane computedBrakeInfoScroll;
    private JTextArea computedEmergencyInfo;
    private JScrollPane computedEmergencyInfoScroll;
    private JTextArea computedTrafficInfo;
    private JScrollPane computedTrafficInfoScroll;
    
    private long carAheadIconTimestamp = 0;
    private long carBehindIconTimestamp = 0;
    private long brakeIconTimestamp = 0;
    private long sirenIconTimestamp = 0;
    
    public int UIscale = 5;    
    public int InnerTextScale = UIscale+1;
    public int OuterTextScale = InnerTextScale+1;
    
	//Class Methods
    public UserInterface(WaveManager waveManager){
       this.waveManager=waveManager;
	   mainFrame = new JFrame();
	   devFrame = new JFrame();
	   
	   mainPanel = new JPanel();
	   devPanel = new JPanel();
	   topPanel = new JPanel();
	   leftPanel = new JPanel();
	   centerPanel = new JPanel();
	   rightPanel = new JPanel();
	   staticDataPanel = new JPanel();
	   variableDataPanel = new JPanel();
	   calculatedInfoPanel = new JPanel();
	   speedometerPanel = new JPanel();
	   buttonPanel = new JPanel();
	   outputPanel = new JPanel();
	   outputPanel = new JPanel();      
	   otherInfoPanel = new JPanel();  
	   packetInfoPanel = new JPanel();
	   controlsPanel = new JPanel();
	   delayPanel = new JPanel();
	   sentPacketInfoPanel = new JPanel();
	   receivedPacketInfoPanel = new JPanel();
	   computedDataPanel = new JPanel();
	   leftComputedDataPanel = new JPanel();
	   rightComputedDataPanel = new JPanel();
	   
	   //Make buttons increase gas and brake
	   speedUpButton = new JButton("Faster");
	   speedDownButton = new JButton("Slower");
	   brakeUpButton = new JButton("Brake More");
	   brakeDownButton = new JButton("Brake Less");
	   sirenButton = new JButton("Siren");
	   delayDownButton = new JButton("Smaller");
	   delayUpButton = new JButton("Larger");
	   delayDownButton.addActionListener(this);
	   delayUpButton.addActionListener(this);
	   speedUpButton.addActionListener(this);
	   speedDownButton.addActionListener(this);
	   brakeUpButton.addActionListener(this);
	   brakeDownButton.addActionListener(this);
	   sirenButton.addActionListener(this);
	   
	   //Images
	   carAheadRed = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\CarAheadRed.png");
	   carAheadOrange = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\CarAheadOrange.png");
	   carAheadYellow = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\CarAheadYellow.png");
	   carBehindRed = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\CarBehindRed.png");
	   carBehindOrange = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\CarBehindOrange.png");
	   carBehindYellow = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\CarBehindYellow.png");
	   brakingRed = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\BrakingRed.png");
	   brakingOrange = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\BrakingOrange.png");
	   brakingYellow = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\BrakingYellow.png");
	   sirenIcon = new ImageIcon("C:\\Users\\OWNER\\workspace\\WaveProject\\images\\Siren.png");
	   
	   //Labels
	   leftPanelLabel = new JLabel("<html><u>Vehicle Info</u></html>");
	   centerPanelLabel = new JLabel("<html><u>Variable Data</u></html>");
	   rightPanelLabel = new JLabel("<html><u>Calculated Info</u></html>");
	   carID = new JLabel("Car ID: xxx-xxx-xxx-xxx");
	   gps = new JLabel("GPS: -, -");
	   heading = new JLabel("Heading: - degrees");
	   speed = new JLabel("Speed: -"){
		    private int[] points;
		    private int currentSpeed;
			private int height= 200;
			private int width = 200;
			private int initialAngle = 59;
			private static final long serialVersionUID = 1L;

			public int[] getCoordXY(int speed, int width, int height){
				double pX, pY;
				double angle = initialAngle;
				pX = 5 + width/2 + (7*width/16)*Math.sin(Math.toRadians(angle+speed))*(-1);
				pY = 5 + height/2 + (7*height/16)*Math.cos(Math.toRadians(angle+speed));
				int[] coord = {(int)pX , (int)pY};
				return coord;
			}

			@Override
			public Dimension getPreferredSize(){return new Dimension(210,210);}
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				currentSpeed = waveManager.speed[0];

				g2d.fill(new Ellipse2D.Double(5, 5, width, height));
				g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));

				//Draw border of Speedometer
				g2d.setColor(Color.GREEN);
				g2d.draw(new Ellipse2D.Double(5, 5, width, height));

				g2d.setColor(Color.WHITE);
				for(int i=0; i<250;i+=20){
					points = this.getCoordXY(i, width, height);
					if(i>=100){
						g2d.drawString(""+i,(int)(points[0]-2*width/32),(int)(points[1]+1*width/32));
					}else{
						g2d.drawString(""+i,(int)(points[0]-1*width/32),(int)(points[1]+1*width/32));
					}
				}
				
				//Find points for tick reference
				points = this.getCoordXY(currentSpeed, width, height);

				g2d.setColor(Color.RED);
				g2d.drawLine(width/2 + (int) 5, height/2 + (int) 5, points[0], points[1]); 
				
			}
		};
	   brakeAmount = new JLabel("Brake Amount: -");
	   vehicleType = new JLabel("Vehicle Type: -");
	   generalInfoCarAhead = new JLabel();
	   generalInfoCarAheadSpeed = new JLabel();
	   generalInfoCarBehind = new JLabel();
	   generalInfoCarBehindSpeed = new JLabel();
	   suggestedSpeedAdjustment = new JLabel("Speed Adjustment:");
	   suggestedSpeedAdjustmentValue = new JLabel("0 Km/h");
	   brakingCarAhead = new JLabel();
	   emergencySiren = new JLabel(sirenIcon);
	   emergencySiren.setVisible(false);
	   sender = new JLabel("<html><u>Trasmitted</u></html>");
	   generalInfoPacketsSent = new JLabel("GeneralInfoService Packets Sent: 0 ");
	   brakeServicePacketsSent = new JLabel("BreakService Packets Sent: 0 ");
	   emergencyServicePacketsSent = new JLabel("EmergencyService Packets Sent: 0 ");
	   trafficServicePacketsSent = new JLabel("TrafficService Packets Sent: 0 ");
	   receiver = new JLabel("<html><u>Received</u></html>");
	   numPacketsReceived = new JLabel("Received Packets: 0 ");
	   numPacketsPassed = new JLabel("Packets Passed: 0 ");
	   numPacketsOmitted = new JLabel("Omitted Packets: 0 ");
	   delay = new JLabel("Smallest Delay = "+waveManager.delay+"ms");
	   sirensLabel = new JLabel("OFF");
	   groupsListeningToLabel = new JLabel("Listening To 0 Service Group(s)");
	
	   leftPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   centerPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   rightPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   carID.setHorizontalAlignment(JLabel.CENTER);
	   carID.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));
	   gps.setHorizontalAlignment(JLabel.CENTER);
	   gps.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));
	   heading.setHorizontalAlignment(JLabel.CENTER);
	   heading.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));
	   speed.setHorizontalAlignment(JLabel.CENTER);
	   speed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));
	   brakeAmount.setHorizontalAlignment(JLabel.CENTER);
	   brakeAmount.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));
	   vehicleType.setHorizontalAlignment(JLabel.CENTER);
	   vehicleType.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   generalInfoCarAhead.setHorizontalAlignment(JLabel.CENTER);
	   generalInfoCarAhead.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   suggestedSpeedAdjustment.setHorizontalAlignment(JLabel.CENTER);
	   suggestedSpeedAdjustment.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   suggestedSpeedAdjustmentValue.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*3)); 
	   generalInfoCarAheadSpeed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*3)); 
	   generalInfoCarBehindSpeed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*3)); 
	   brakingCarAhead.setHorizontalAlignment(JLabel.CENTER);   
	   brakingCarAhead.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   sender.setHorizontalAlignment(JLabel.CENTER);  
	   receiver.setHorizontalAlignment(JLabel.CENTER);  
	   generalInfoPacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   generalInfoPacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));
	   brakeServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   brakeServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   emergencyServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   emergencyServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));  
	   trafficServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   trafficServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));  
	   numPacketsReceived.setHorizontalAlignment(JLabel.CENTER);   
	   numPacketsReceived.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2));  
	   numPacketsPassed.setHorizontalAlignment(JLabel.CENTER);   
	   numPacketsPassed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   numPacketsOmitted.setHorizontalAlignment(JLabel.CENTER);    
	   numPacketsOmitted.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   delay.setHorizontalAlignment(JLabel.CENTER);          
	   groupsListeningToLabel.setHorizontalAlignment(JLabel.CENTER);    
	   groupsListeningToLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   
	   //Output boxes
	   JLabel outputLabel = new JLabel("<html><u>Sending/Receiving</u></html>");
	   outputLabel.setHorizontalAlignment(JLabel.CENTER);     
	   output = new JTextArea();
	   output.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScale*2));
	   output.setEditable(false);
	   consoleScroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   consoleScroll.setPreferredSize(new Dimension(UIscale*220,UIscale*40));
	
	   JLabel generalInfoServiceOutputLabel = new JLabel("<html><u>General Info Service Computed Information</u></html>");
	   computedGeneralInfo = new JTextArea();
	   computedGeneralInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScale*2));
	   computedGeneralInfo.setEditable(false);
	   computedGeneralInfoScroll = new JScrollPane (computedGeneralInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedGeneralInfoScroll.setPreferredSize(new Dimension(UIscale*108,UIscale*30));
	
	   JLabel brakeServiceOutputLabel = new JLabel("<html><u>Brake Service Computed Information</u></html>");
	   computedBrakeInfo = new JTextArea();
	   computedBrakeInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScale*2));
	   computedBrakeInfo.setEditable(false);
	   computedBrakeInfoScroll = new JScrollPane (computedBrakeInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedBrakeInfoScroll.setPreferredSize(new Dimension(UIscale*108,UIscale*30));
	   
	   JLabel emergencyServiceOutputLabel = new JLabel("<html><u>Emergency Service Computed Information</u></html>");
	   computedEmergencyInfo = new JTextArea();
	   computedEmergencyInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScale*2));
	   computedEmergencyInfo.setEditable(false);
	   computedEmergencyInfoScroll = new JScrollPane (computedEmergencyInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedEmergencyInfoScroll.setPreferredSize(new Dimension(UIscale*108,UIscale*30));

	   JLabel trafficServiceOutputLabel = new JLabel("<html><u>Traffic Service Computed Information</u></html>");
	   computedTrafficInfo = new JTextArea();
	   computedTrafficInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScale*2));
	   computedTrafficInfo.setEditable(false);
	   computedTrafficInfoScroll = new JScrollPane (computedTrafficInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedTrafficInfoScroll.setPreferredSize(new Dimension(UIscale*108,UIscale*30));
	   
	   topPanel.setLayout(new GridLayout(1,3));
	   staticDataPanel.setLayout(new GridLayout(4,1));
	   variableDataPanel.setLayout(new GridLayout(4,1));
	   calculatedInfoPanel.setLayout(new GridLayout(4,2));
	   buttonPanel.setLayout(new GridLayout(1,2));
	   otherInfoPanel.setLayout(new GridLayout(1,2));
	   packetInfoPanel.setLayout(new GridLayout(1,2));
	   sentPacketInfoPanel.setLayout(new GridLayout(5,1));
	   receivedPacketInfoPanel.setLayout(new GridLayout(4,1));
	   computedDataPanel.setLayout(new GridLayout(1,2));
	   
	   topPanel.setPreferredSize(new Dimension(UIscale*250,UIscale*100));
	   staticDataPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*30));
	   variableDataPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*30));
	   rightPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*100));
	   calculatedInfoPanel.setPreferredSize(new Dimension(UIscale*70,UIscale*90));
	   buttonPanel.setPreferredSize(new Dimension(UIscale*200,UIscale*10));
	   outputPanel.setPreferredSize(new Dimension(UIscale*225,UIscale*300));
	   otherInfoPanel.setPreferredSize(new Dimension(UIscale*160,UIscale*16));
	   packetInfoPanel.setPreferredSize(new Dimension(UIscale*160,UIscale*16));
	   sentPacketInfoPanel.setPreferredSize(new Dimension(UIscale*100,UIscale*8));
	   receivedPacketInfoPanel.setPreferredSize(new Dimension(UIscale*100,UIscale*20));
	   computedDataPanel.setPreferredSize(new Dimension(UIscale*225,UIscale*200));
	   leftComputedDataPanel.setPreferredSize(new Dimension(UIscale*110,UIscale*140));
	   rightComputedDataPanel.setPreferredSize(new Dimension(UIscale*110,UIscale*140));
	   delayPanel.setPreferredSize(new Dimension(UIscale*10,UIscale*20));
	   speedometerPanel.setPreferredSize(new Dimension(UIscale*50,UIscale*50));
	   outputLabel.setPreferredSize(new Dimension(UIscale*225,UIscale*8));
	   
	   //Add components to panels
	   topPanel.add(leftPanel);
	   topPanel.add(centerPanel);
	   topPanel.add(rightPanel);
	   leftPanel.add(leftPanelLabel);
	   leftPanel.add(staticDataPanel);
	   staticDataPanel.add(carID);
	   staticDataPanel.add(vehicleType);
	   centerPanel.add(centerPanelLabel);
	   centerPanel.add(variableDataPanel);
	   centerPanel.add(speedometerPanel);
	   variableDataPanel.add(gps);
	   variableDataPanel.add(heading);
	   variableDataPanel.add(brakeAmount);
	   speedometerPanel.add(speed);
	   rightPanel.add(rightPanelLabel);
	   rightPanel.add(calculatedInfoPanel);
	   calculatedInfoPanel.add(suggestedSpeedAdjustment);
	   calculatedInfoPanel.add(suggestedSpeedAdjustmentValue);
	   calculatedInfoPanel.add(generalInfoCarAhead);
	   calculatedInfoPanel.add(generalInfoCarAheadSpeed);
	   calculatedInfoPanel.add(generalInfoCarBehind);
	   calculatedInfoPanel.add(generalInfoCarBehindSpeed);
	   calculatedInfoPanel.add(brakingCarAhead);
	   calculatedInfoPanel.add(emergencySiren);
	   buttonPanel.add(delayPanel);
	   buttonPanel.add(controlsPanel);
	   controlsPanel.add(speedUpButton);
	   controlsPanel.add(speedDownButton);
	   controlsPanel.add(brakeUpButton);
	   controlsPanel.add(brakeDownButton);
	   if(waveManager.vehicleType.equals("Emergency")){
		   controlsPanel.add(sirenButton);
		   controlsPanel.add(sirensLabel);
	   }
	   outputPanel.add(packetInfoPanel);
	   outputPanel.add(otherInfoPanel);
	   outputPanel.add(outputLabel);
	   outputPanel.add(consoleScroll);
	   outputPanel.add(computedDataPanel);
	   packetInfoPanel.add(sentPacketInfoPanel);
	   packetInfoPanel.add(receivedPacketInfoPanel);
	   otherInfoPanel.add(groupsListeningToLabel);
	   delayPanel.add(delay);
	   delayPanel.add(delayDownButton);
	   delayPanel.add(delayUpButton);
	   sentPacketInfoPanel.add(sender);
	   sentPacketInfoPanel.add(generalInfoPacketsSent);
	   sentPacketInfoPanel.add(brakeServicePacketsSent);
	   sentPacketInfoPanel.add(emergencyServicePacketsSent);
	   sentPacketInfoPanel.add(trafficServicePacketsSent);
	   receivedPacketInfoPanel.add(receiver);
	   receivedPacketInfoPanel.add(numPacketsReceived);
	   receivedPacketInfoPanel.add(numPacketsOmitted);
	   receivedPacketInfoPanel.add(numPacketsPassed);
	   computedDataPanel.add(leftComputedDataPanel);
	   computedDataPanel.add(rightComputedDataPanel);
	   leftComputedDataPanel.add(generalInfoServiceOutputLabel);
	   leftComputedDataPanel.add(computedGeneralInfoScroll);
	   rightComputedDataPanel.add(brakeServiceOutputLabel);
	   rightComputedDataPanel.add(computedBrakeInfoScroll);
	   leftComputedDataPanel.add(emergencyServiceOutputLabel);
	   leftComputedDataPanel.add(computedEmergencyInfoScroll);
	   rightComputedDataPanel.add(trafficServiceOutputLabel);
	   rightComputedDataPanel.add(computedTrafficInfoScroll);
	   mainPanel.add(topPanel);
	   mainPanel.add(buttonPanel);
	   devPanel.add(outputPanel);
	   
	   mainFrame.add(mainPanel); 
	   mainFrame.setTitle("WAVE Interface");
	   mainFrame.setSize(UIscale*250,UIscale*125);
	   mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   mainFrame.setVisible(true);
	   
	   devFrame.add(devPanel); 
	   devFrame.setTitle("WAVE Dev Interface");
	   devFrame.setSize(UIscale*230,UIscale*168);
	   devFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   devFrame.setVisible(true);
    }
    
    public void start(){
		if(userInterfaceThread==null){
			userInterfaceThread = new Thread(this, "UserInterfaceThread");
			userInterfaceThread.start();
		}
	}
    
    public void run(){
    	while(true){
    		 try{
    			 output.setCaretPosition(output.getDocument().getLength());
    			 computedGeneralInfo.setCaretPosition(computedGeneralInfo.getDocument().getLength());
    			 computedBrakeInfo.setCaretPosition(computedBrakeInfo.getDocument().getLength());
    			 computedEmergencyInfo.setCaretPosition(computedEmergencyInfo.getDocument().getLength());
    			 computedTrafficInfo.setCaretPosition(computedTrafficInfo.getDocument().getLength());

    			 writeCarID(waveManager.CarID);
    			 writeSpeed(waveManager.speed[0]);
    			 writeBrakeAmount(waveManager.brakeAmount);
    			 writeHeading(waveManager.heading);
    			 writeGPS(waveManager.GPSlattitude, waveManager.GPSlongitude);
    			 writeVehicleType(waveManager.vehicleType);
    			 checkIconTimestamps();
    		 }catch(Exception e){ }
    	}
    }

    public void output(String outputText){
    	output.append(outputText+"\n");
    }
    
    public void computedGeneralInfo(String outputText){
    	computedGeneralInfo.append(outputText+"\n");
    }

    public void computedBrakeInfo(String outputText){
    	computedBrakeInfo.append(outputText+"\n");
    }

    public void computedEmergencyInfo(String outputText){
    	computedEmergencyInfo.append(outputText+"\n");
    }

    public void computedTrafficInfo(String outputText){
    	computedTrafficInfo.append(outputText+"\n");
    }
    
    public void writeCarID(String outputText){
    	carID.setText("CarID: "+outputText);
    }
    
    public void writeVehicleType(String outputText){
    	vehicleType.setText("Vehicle Type: "+outputText);
    }
    
    public void writeHeading(int outputText){
    	heading.setText("Heading: "+outputText+" degrees");
    }
    
    public void writeGPS(double lattitude, double longitude){
    	gps.setText("GPS: "+lattitude+", "+longitude);
    }
    
    public void writeSpeed(int outputText){
    	speed.setText("Speed: "+outputText);
    }
    
    public void writeBrakeAmount(int outputText){
    	brakeAmount.setText("Brake Amount: "+outputText);
    	
    }
    
    public void writeGeneralInfoCarAhead(int speedDifference, int vehicleSpeed){
    	if(speedDifference<=20){
    		generalInfoCarAhead.setIcon(carAheadYellow);
		}else if(speedDifference<=40){
			generalInfoCarAhead.setIcon(carAheadOrange);
		}else{
			generalInfoCarAhead.setIcon(carAheadRed);
		}	
    	generalInfoCarAhead.setVisible(true);
    	generalInfoCarAheadSpeed.setText(vehicleSpeed+" Km/h");
    	generalInfoCarAheadSpeed.setVisible(true);
    	carAheadIconTimestamp = System.currentTimeMillis();
    }
    
    public void writeGeneralInfoCarBehind(int speedDifference, int vehicleSpeed){
    	if(speedDifference<=20){
    		generalInfoCarBehind.setIcon(carBehindYellow);
		}else if(speedDifference<=40){
			generalInfoCarBehind.setIcon(carBehindOrange);
		}else{
			generalInfoCarBehind.setIcon(carBehindRed);
		}	
    	generalInfoCarBehind.setVisible(true);
    	generalInfoCarBehindSpeed.setText(vehicleSpeed+" Km/h");
    	generalInfoCarBehindSpeed.setVisible(true);
    	carBehindIconTimestamp = System.currentTimeMillis();
    }
    
    public void writeSuggestedSpeedAdjustment(String outputText){
    	suggestedSpeedAdjustmentValue.setText(outputText+" Km/h");
    }
    
    public void writeSpeedBrakeApplied(int brakeAmount){
    	if(brakeAmount<=20){
    		brakingCarAhead.setIcon(brakingYellow);
		}else if(brakeAmount<=40){
			brakingCarAhead.setIcon(brakingOrange);
		}else{
			brakingCarAhead.setIcon(brakingRed);
		}
    	brakingCarAhead.setVisible(true);
    	brakeIconTimestamp = System.currentTimeMillis();
    }
    
    public void writeEmergencySiren(){
		emergencySiren.setVisible(true);
    	sirenIconTimestamp = System.currentTimeMillis();
    }
    
    public void updateNumPacketsReceived(int output){
    	numPacketsReceived.setText("Received Packets: "+output+" ");
    }

    public void updateNumPacketsOmitted(int output){
    	numPacketsOmitted.setText("Omitted Packets: "+output+" ");
    }
    
    public void updateNumPacketsPassed(int output){
    	numPacketsPassed.setText("Packets Passed: "+output+" ");
    }

    public void updateGeneralInfoServicePacketsSent(int output){
    	generalInfoPacketsSent.setText("GeneralInfoService Packets Sent: "+output+"");
    }
    
    public void updateBrakeServicePacketsSent(int output){
    	brakeServicePacketsSent.setText("BrakeService Packets Sent: "+output+"");
    }
    
    public void updateEmergencyServicePacketsSent(int output){
    	emergencyServicePacketsSent.setText("EmergencyService Packets Sent: "+output+"");
    }

    public void updateTrafficServicePacketsSent(int output){
    	trafficServicePacketsSent.setText("TrafficService Packets Sent: "+output+"");
    }

    public void updateNumberGroupsListeningTo(int output){
    	groupsListeningToLabel.setText("Listening To "+output+" Service Group(s)");
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().equals(speedUpButton)){
    		if(waveManager.speed[0]>=0&&waveManager.speed[0]<240){
        		waveManager.addSpeed(waveManager.speed[0]+=10);
    		}
    	}else if(e.getSource().equals(speedDownButton)){
    		if(waveManager.speed[0]>0&&waveManager.speed[0]<=240){
        		waveManager.addSpeed(waveManager.speed[0]-=10);
    		}
    	}else if(e.getSource().equals(brakeUpButton)){
    		if(waveManager.brakeAmount>=0&&waveManager.brakeAmount<100){
        		waveManager.brakeAmount+=10;
    		}
    	}else if(e.getSource().equals(brakeDownButton)){
    		if(waveManager.brakeAmount>0&&waveManager.brakeAmount<=100){
        		waveManager.brakeAmount-=10;
    		}
    	}else if(e.getSource().equals(sirenButton)){
    		if(waveManager.sirensOn){
    			waveManager.sirensOn=false;
    			sirensLabel.setText("OFF");
    		}else{
    			waveManager.sirensOn=true;
    			sirensLabel.setText("ON");
    		}
    	}else if(e.getSource().equals(delayDownButton)){
    		waveManager.delay*=0.5;
			delay.setText("Smallest Delay = "+waveManager.delay+"ms");
    	}else if(e.getSource().equals(delayUpButton)){
    		waveManager.delay*=1.5;
			delay.setText("Smallest Delay = "+waveManager.delay+"ms");
    	}
    }
    
    public void checkIconTimestamps(){
    	long currentTime = System.currentTimeMillis();
    	if(carAheadIconTimestamp!=0 && carAheadIconTimestamp+2000<currentTime){
    		generalInfoCarAhead.setVisible(false);
	    	generalInfoCarAheadSpeed.setVisible(false);
    		carAheadIconTimestamp=0;
    	}
		if(carBehindIconTimestamp!=0 && carBehindIconTimestamp+2000<currentTime){
			generalInfoCarBehind.setVisible(false);
	    	generalInfoCarBehindSpeed.setVisible(false);
			carBehindIconTimestamp=0;
		}
		if(brakeIconTimestamp!=0 && brakeIconTimestamp+2000<currentTime){
			brakingCarAhead.setVisible(false);
			brakeIconTimestamp=0;
		}
		if(sirenIconTimestamp!=0 && sirenIconTimestamp+2000<currentTime){
			emergencySiren.setVisible(false);
			sirenIconTimestamp=0;
		}
    }
}
