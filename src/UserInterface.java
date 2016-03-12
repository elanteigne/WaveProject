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
    private JPanel calculatedInfoPanel1;
    private JPanel calculatedInfoPanel2;
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
    private JPanel sirenInfoPanel;
    
    private ImageIcon carAheadRed;
    private ImageIcon carAheadOrange;
    private ImageIcon carAheadYellow;
    private ImageIcon carBehindRed;
    private ImageIcon carBehindOrange;
    private ImageIcon carBehindYellow;
    private ImageIcon brakingRed;
    private ImageIcon brakingOrange;
    private ImageIcon brakingYellow;
    private ImageIcon sirenIconOff;
    private ImageIcon sirenIconOn;
    private ImageIcon trafficAheadRed;
    private ImageIcon trafficAheadOrange;
    private ImageIcon trafficAheadYellow;
    private ImageIcon backArrow;
    private ImageIcon sirenGrey;
    private ImageIcon generalInfoGreyAhead;
    private ImageIcon generalInfoGreyBehind;
    private ImageIcon brakingAheadGrey;
    private ImageIcon trafficIconGrey;
    
    private JLabel leftPanelLabel;
    private JLabel centerPanelLabel;
    private JLabel rightPanelLabel;
    private JLabel calcInfoLabel;
    private JLabel carID;
    private JLabel gps;
    private JLabel heading;
    private JLabel speed;
    private JLabel brakeAmount;
    private JLabel vehicleType;
    private JLabel generalInfoCarAhead;
    private JLabel generalInfoCarAheadSpeed;
    private JLabel generalInfoCarBehind;
    private JLabel generalInfoCarBehindSpeed;
    private JLabel brakingCarAhead;
    private JLabel brakingCarAheadSpeed;
    private JLabel trafficAhead;
    private JLabel trafficAheadDistance;
    private JLabel emergencySiren;
    private JLabel emergencySirenDistance;
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
    private JLabel groupsListeningToLabel;
    private JLabel generalInfoServiceOutputLabel;
    private JLabel brakeServiceOutputLabel;
    private JLabel emergencyServiceOutputLabel;
    private JLabel trafficServiceOutputLabel;
    private JLabel sirenDirection;
    private JLabel sirenStatusLabel;
    
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
    
    private boolean sirenFlashing; 
    
    private long carAheadIconTimestamp = 0;
    private long carBehindIconTimestamp = 0;
    private long brakeIconTimestamp = 0;
    private long sirenIconTimestamp = 0;
    private long sirenFlashingTimestamp = 0;
    private long trafficAheadTimestamp = 0;
    
    private int UIscaleMain = 4;    
    private int UIscaleDev = 4;    
    private int InnerTextScaleMain = UIscaleMain+1;
    private int OuterTextScaleMain = InnerTextScaleMain+1;
    private int InnerTextScaleDev = UIscaleDev+1;
    private int OuterTextScaleDev = InnerTextScaleDev+1;

    private int generalInfoComputations = 0;
    private int brakeComputations = 0;
    private int emergencyComputations = 0;
    private int trafficComputations = 0;
    
	//Class Methods
    public UserInterface(final WaveManager waveManager){
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
	   calculatedInfoPanel1 = new JPanel();
	   calculatedInfoPanel2 = new JPanel();
	   speedometerPanel = new JPanel();
	   buttonPanel = new JPanel();
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
	   sirenInfoPanel = new JPanel();
	   
	   //Light Blue -> C0DEFF
	   //Medium Blue -> A0CFFF
	   //Dark Blue -> 6CADE8
	   mainPanel.setBackground(Color.decode("#A0CFFF"));
	   devPanel.setBackground(Color.decode("#A0CFFF"));
	   topPanel.setBackground(Color.decode("#A0CFFF"));
	   leftPanel.setBackground(Color.decode("#A0CFFF"));
	   centerPanel.setBackground(Color.decode("#A0CFFF"));
	   rightPanel.setBackground(Color.decode("#A0CFFF"));
	   staticDataPanel.setBackground(Color.decode("#A0CFFF"));
	   variableDataPanel.setBackground(Color.decode("#A0CFFF"));
	   calculatedInfoPanel1.setBackground(Color.decode("#C0DEFF"));
	   calculatedInfoPanel2.setBackground(Color.decode("#C0DEFF"));
	   speedometerPanel.setBackground(Color.decode("#A0CFFF"));
	   buttonPanel.setBackground(Color.decode("#C0DEFF"));
	   outputPanel.setBackground(Color.decode("#C0DEFF"));
	   otherInfoPanel.setBackground(Color.decode("#C0DEFF"));
	   packetInfoPanel.setBackground(Color.decode("#C0DEFF"));
	   controlsPanel.setBackground(Color.decode("#6CADE8"));
	   delayPanel.setBackground(Color.decode("#6CADE8"));
	   sentPacketInfoPanel.setBackground(Color.decode("#A0CFFF"));
	   receivedPacketInfoPanel.setBackground(Color.decode("#A0CFFF"));
	   computedDataPanel.setBackground(Color.decode("#A0CFFF"));
	   leftComputedDataPanel.setBackground(Color.decode("#A0CFFF"));
	   rightComputedDataPanel.setBackground(Color.decode("#A0CFFF"));
	   sirenInfoPanel.setBackground(Color.decode("#C0DEFF"));
	   
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
	   carAheadRed = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\CarAheadRed.png");
	   carAheadOrange = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\CarAheadOrange.png");
	   carAheadYellow = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\CarAheadYellow.png");
	   carBehindRed = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\CarBehindRed.png");
	   carBehindOrange = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\CarBehindOrange.png");
	   carBehindYellow = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\CarBehindYellow.png");
	   brakingRed = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\BrakingRed.png");
	   brakingOrange = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\BrakingOrange.png");
	   brakingYellow = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\BrakingYellow.png");
	   sirenIconOff = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\SirenOff.png");
	   sirenIconOn = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\SirenOn.png");
	   trafficAheadRed = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\TrafficIconRed.png");
	   trafficAheadOrange = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\TrafficIconOrange.png");
	   trafficAheadYellow = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\TrafficIconYellow.png");
	   backArrow = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\BackArrow.png");
	   generalInfoGreyAhead = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\GeneralInfoIconGreyUp.png");
	   generalInfoGreyBehind = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\GeneralInfoIconGreyDown.png");
	   brakingAheadGrey = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\BrakeIconGrey.png");
	   sirenGrey = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\SirenGrey.png");
	   trafficIconGrey = new ImageIcon("C:\\Users\\DavidJF\\Google Drive\\myWorkspace\\WaveProject\\images\\TrafficIconGrey.png");
	   
	   //Labels
	   leftPanelLabel = new JLabel("<html><u>Vehicle Info</u></html>");
	   centerPanelLabel = new JLabel("<html><u>Variable Data</u></html>");
	   rightPanelLabel = new JLabel("<html><u>Calculated Info</u></html>");
	   calcInfoLabel = new JLabel("<html><u>Calculated Info</u></html>");
	   carID = new JLabel("Car ID: xxx-xxx-xxx-xxx");
	   gps = new JLabel("GPS: -, -");
	   heading = new JLabel("Heading: - degrees");
	   speed = new JLabel(){
		    private int[] points;
		    private int currentSpeed;
			private int height= UIscaleMain*40;
			private int width = UIscaleMain*40;
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
			
			public int[] getCoordNeedle(int speed, int width, int height){
				double pX, pY;
				double angle = initialAngle;
				pX = 5 + width/2 + (6.5*width/16)*Math.sin(Math.toRadians(angle+speed))*(-1);
				pY = 5 + height/2 + (6.5*height/16)*Math.cos(Math.toRadians(angle+speed));
				int[] coord = {(int)pX , (int)pY};
				return coord;
			}
			
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				currentSpeed = waveManager.speed[0];

				g2d.fill(new Ellipse2D.Double(5, 5, width, height));
				g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));

				g2d.setColor(Color.BLUE);
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
				points = this.getCoordNeedle(currentSpeed, width, height);

				g2d.setColor(Color.RED);
				g2d.drawLine(width/2 + (int) 5, height/2 + (int) 5, points[0], points[1]); 
				
			}
		};
	   brakeAmount = new JLabel("Brake Amount: -");
	   vehicleType = new JLabel("Vehicle Type: -");
	   generalInfoCarAhead = new JLabel(generalInfoGreyAhead);
	   generalInfoCarAheadSpeed = new JLabel();
	   generalInfoCarBehind = new JLabel(generalInfoGreyBehind);
	   generalInfoCarBehindSpeed = new JLabel();
	   brakingCarAheadSpeed = new JLabel();
	   brakingCarAhead = new JLabel(brakingAheadGrey);
	   trafficAhead = new JLabel(trafficIconGrey);
	   trafficAheadDistance = new JLabel();
	   emergencySiren = new JLabel(sirenGrey);
	   emergencySirenDistance = new JLabel();
	   sirenDirection = new JLabel(backArrow);
	   sirenDirection.setVisible(false);
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
	   groupsListeningToLabel = new JLabel("Listening To 0 Service Group(s)");
	   sirenStatusLabel = new JLabel("<html> <font style='font-weight:bold; color:#a0aaae'>Sirens OFF</u></html>");
	
	   leftPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   centerPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   rightPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   carID.setHorizontalAlignment(JLabel.CENTER);
	   carID.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2));
	   gps.setHorizontalAlignment(JLabel.CENTER);
	   gps.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2));
	   heading.setHorizontalAlignment(JLabel.CENTER);
	   heading.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2));
	   speed.setHorizontalAlignment(JLabel.CENTER);
	   speed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2));
	   brakeAmount.setHorizontalAlignment(JLabel.CENTER);
	   brakeAmount.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2));
	   vehicleType.setHorizontalAlignment(JLabel.CENTER);
	   vehicleType.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2)); 
	   generalInfoCarAhead.setHorizontalAlignment(JLabel.CENTER);
	   generalInfoCarAhead.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*2)); 
	   generalInfoCarAhead.setHorizontalAlignment(JLabel.CENTER);
	   generalInfoCarAheadSpeed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*3)); 
	   generalInfoCarBehind.setHorizontalAlignment(JLabel.CENTER);
	   generalInfoCarBehindSpeed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*3)); 
	   brakingCarAhead.setHorizontalAlignment(JLabel.CENTER);   
	   brakingCarAheadSpeed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*3)); 
	   emergencySiren.setHorizontalAlignment(JLabel.CENTER);
	   emergencySirenDistance.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*3)); 
	   emergencySirenDistance.setHorizontalAlignment(JLabel.CENTER);
	   trafficAhead.setHorizontalAlignment(JLabel.CENTER);
	   trafficAheadDistance.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*3)); 
	   sender.setHorizontalAlignment(JLabel.CENTER);  
	   receiver.setHorizontalAlignment(JLabel.CENTER);  
	   generalInfoPacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   generalInfoPacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2));
	   brakeServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   brakeServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   emergencyServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   emergencyServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2));  
	   trafficServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   trafficServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2));  
	   numPacketsReceived.setHorizontalAlignment(JLabel.CENTER);   
	   numPacketsReceived.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2));  
	   numPacketsPassed.setHorizontalAlignment(JLabel.CENTER);   
	   numPacketsPassed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   numPacketsOmitted.setHorizontalAlignment(JLabel.CENTER);    
	   numPacketsOmitted.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   delay.setHorizontalAlignment(JLabel.CENTER);          
	   groupsListeningToLabel.setHorizontalAlignment(JLabel.CENTER);    
	   groupsListeningToLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   generalInfoCarBehind.setHorizontalAlignment(JLabel.CENTER);
	   sirenStatusLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleMain*3)); 
	   sirenStatusLabel.setHorizontalAlignment(JLabel.CENTER);
	   
	   //Output boxes
	   JLabel outputLabel = new JLabel("<html><u>Sending/Receiving</u></html>");
	   outputLabel.setHorizontalAlignment(JLabel.CENTER);     
	   output = new JTextArea();
	   output.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScaleDev*2));
	   output.setEditable(false);
	   consoleScroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   consoleScroll.setPreferredSize(new Dimension(UIscaleDev*220,UIscaleDev*40));
	
	   generalInfoServiceOutputLabel = new JLabel("<html><u>General Info Service Computed Information</u> <font style='color:blue'>Computations:</font> 0</html>");
	   generalInfoServiceOutputLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   computedGeneralInfo = new JTextArea();
	   computedGeneralInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScaleDev*2));
	   computedGeneralInfo.setEditable(false);
	   computedGeneralInfoScroll = new JScrollPane (computedGeneralInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedGeneralInfoScroll.setPreferredSize(new Dimension(UIscaleDev*108,UIscaleDev*30));
	
	   brakeServiceOutputLabel = new JLabel("<html><u>Brake Service Computed Information</u> <font style='color:blue'>Computations:</font> 0</html>");
	   brakeServiceOutputLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   computedBrakeInfo = new JTextArea();
	   computedBrakeInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScaleDev*2));
	   computedBrakeInfo.setEditable(false);
	   computedBrakeInfoScroll = new JScrollPane (computedBrakeInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedBrakeInfoScroll.setPreferredSize(new Dimension(UIscaleDev*108,UIscaleDev*30));
	   
	   emergencyServiceOutputLabel = new JLabel("<html><u>Emergency Service Computed Information</u> <font style='color:blue'>Computations:</font> 0</html>");
	   emergencyServiceOutputLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   computedEmergencyInfo = new JTextArea();
	   computedEmergencyInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScaleDev*2));
	   computedEmergencyInfo.setEditable(false);
	   computedEmergencyInfoScroll = new JScrollPane (computedEmergencyInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedEmergencyInfoScroll.setPreferredSize(new Dimension(UIscaleDev*108,UIscaleDev*30));

	   trafficServiceOutputLabel = new JLabel("<html><u>Traffic Service Computed Information</u> <font style='color:blue'>Computations:</font> 0</html>");
	   trafficServiceOutputLabel.setFont(new Font("Open Sans", Font.BOLD, OuterTextScaleDev*2)); 
	   computedTrafficInfo = new JTextArea();
	   computedTrafficInfo.setFont(new Font("Open Sans", Font.PLAIN, InnerTextScaleDev*2));
	   computedTrafficInfo.setEditable(false);
	   computedTrafficInfoScroll = new JScrollPane (computedTrafficInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedTrafficInfoScroll.setPreferredSize(new Dimension(UIscaleDev*108,UIscaleDev*30));
	   
	   topPanel.setLayout(new GridLayout(1,3));
	   staticDataPanel.setLayout(new GridLayout(3,1));
	   variableDataPanel.setLayout(new GridLayout(4,1));
	   calculatedInfoPanel1.setLayout(new GridLayout(2,2));
	   calculatedInfoPanel2.setLayout(new GridLayout(3,2));
	   buttonPanel.setLayout(new GridLayout(1,2));
	   otherInfoPanel.setLayout(new GridLayout(1,2));
	   packetInfoPanel.setLayout(new GridLayout(1,2));
	   sentPacketInfoPanel.setLayout(new GridLayout(5,1));
	   receivedPacketInfoPanel.setLayout(new GridLayout(4,1));
	   computedDataPanel.setLayout(new GridLayout(1,2));
	   sirenInfoPanel.setLayout(new GridLayout(1,2));
	   
	   topPanel.setPreferredSize(new Dimension(UIscaleMain*250,UIscaleMain*100));
	   staticDataPanel.setPreferredSize(new Dimension(UIscaleMain*140,UIscaleMain*15));
	   variableDataPanel.setPreferredSize(new Dimension(UIscaleMain*140,UIscaleMain*30));
	   rightPanel.setPreferredSize(new Dimension(UIscaleMain*140,UIscaleMain*100));
	   calculatedInfoPanel1.setPreferredSize(new Dimension(UIscaleMain*70,UIscaleMain*65));
	   calculatedInfoPanel2.setPreferredSize(new Dimension(UIscaleMain*70,UIscaleMain*90));
	   buttonPanel.setPreferredSize(new Dimension(UIscaleMain*250,UIscaleMain*10));
	   outputPanel.setPreferredSize(new Dimension(UIscaleMain*225,UIscaleMain*300));
	   leftComputedDataPanel.setPreferredSize(new Dimension(UIscaleMain*110,UIscaleMain*140));
	   rightComputedDataPanel.setPreferredSize(new Dimension(UIscaleMain*110,UIscaleMain*140));
	   speedometerPanel.setPreferredSize(new Dimension(UIscaleMain*50,UIscaleMain*80));
	   speed.setPreferredSize(new Dimension(UIscaleMain*42,UIscaleMain*50));
	   
	   otherInfoPanel.setPreferredSize(new Dimension(UIscaleDev*160,UIscaleDev*16));
	   packetInfoPanel.setPreferredSize(new Dimension(UIscaleDev*160,UIscaleDev*16));
	   sentPacketInfoPanel.setPreferredSize(new Dimension(UIscaleDev*100,UIscaleDev*8));
	   receivedPacketInfoPanel.setPreferredSize(new Dimension(UIscaleDev*100,UIscaleDev*20));
	   computedDataPanel.setPreferredSize(new Dimension(UIscaleDev*225,UIscaleDev*200));
	   delayPanel.setPreferredSize(new Dimension(UIscaleDev*10,UIscaleDev*20));
	   outputLabel.setPreferredSize(new Dimension(UIscaleDev*225,UIscaleDev*8));
	   
	   //Add components to panels
	   topPanel.add(leftPanel);
	   topPanel.add(centerPanel);
	   topPanel.add(rightPanel);
	   leftPanel.add(leftPanelLabel);
	   leftPanel.add(staticDataPanel);
	   leftPanel.add(calcInfoLabel);
	   leftPanel.add(calculatedInfoPanel1);
	   calculatedInfoPanel1.add(brakingCarAhead);
	   calculatedInfoPanel1.add(brakingCarAheadSpeed);
	   calculatedInfoPanel1.add(emergencySiren);
	   calculatedInfoPanel1.add(sirenInfoPanel);
	   sirenInfoPanel.add(emergencySirenDistance);
	   sirenInfoPanel.add(sirenDirection);
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
	   rightPanel.add(calculatedInfoPanel2);
	   calculatedInfoPanel2.add(generalInfoCarAhead);
	   calculatedInfoPanel2.add(generalInfoCarAheadSpeed);
	   calculatedInfoPanel2.add(generalInfoCarBehind);
	   calculatedInfoPanel2.add(generalInfoCarBehindSpeed);
	   calculatedInfoPanel2.add(trafficAhead);
	   calculatedInfoPanel2.add(trafficAheadDistance);
	   buttonPanel.add(delayPanel);
	   buttonPanel.add(controlsPanel);
	   controlsPanel.add(speedUpButton);
	   controlsPanel.add(speedDownButton);
	   controlsPanel.add(brakeUpButton);
	   controlsPanel.add(brakeDownButton);
	   if(waveManager.vehicleType.equals("Emergency")){
		   speedometerPanel.add(sirenStatusLabel);
		   controlsPanel.add(sirenButton);
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
	   mainFrame.setSize(UIscaleMain*250,UIscaleMain*125);
	   mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   mainFrame.setVisible(true);
	   
	   devFrame.add(devPanel); 
	   devFrame.setTitle("WAVE Dev Interface");
	   devFrame.setSize(UIscaleDev*230,UIscaleDev*168);
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
    			 if(sirenFlashing){
        			 checkSiren();
    			 }
    			 checkIconTimestamps();
    		 }catch(Exception e){ }
    	}
    }

    public void output(String outputText){
    	output.append(outputText+"\n");
    }
    
    public void computedGeneralInfo(String outputText){
    	computedGeneralInfo.append(outputText+"\n");
    	generalInfoComputations ++;
    	generalInfoServiceOutputLabel.setText("<html><u>General Info Service Computed Information</u> <font style='color:blue'>Computations:</font>"+generalInfoComputations+"</html>");
    }

    public void computedBrakeInfo(String outputText){
    	computedBrakeInfo.append(outputText+"\n");
    	brakeComputations ++;
    	brakeServiceOutputLabel.setText("<html><u>Brake Service Computed Information</u> <font style='color:blue'>Computations:</font>"+brakeComputations+"</html>");
    }

    public void computedEmergencyInfo(String outputText){
    	computedEmergencyInfo.append(outputText+"\n");
    	emergencyComputations ++;
    	emergencyServiceOutputLabel.setText("<html><u>Brake Service Computed Information</u> <font style='color:blue'>Computations:</font>"+emergencyComputations+"</html>");
    }

    public void computedTrafficInfo(String outputText){
    	computedTrafficInfo.append(outputText+"\n");
    	trafficComputations ++;
    	trafficServiceOutputLabel.setText("<html><u>Brake Service Computed Information</u> <font style='color:blue'>Computations:</font>"+trafficComputations+"</html>");
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
    
    public void turnOnGeneralInfoCarAhead(int speedDifference){
    	if(speedDifference<=20){
    		generalInfoCarAhead.setIcon(carAheadYellow);
		}else if(speedDifference<=40){
			generalInfoCarAhead.setIcon(carAheadOrange);
		}else{
			generalInfoCarAhead.setIcon(carAheadRed);
		}	
    	generalInfoCarAheadSpeed.setText("- "+speedDifference+" Km/h");
    	generalInfoCarAheadSpeed.setVisible(true);
    	carAheadIconTimestamp = System.currentTimeMillis();
    }
    
    public void turnOnGeneralInfoCarBehind(int speedDifference){
    	if(speedDifference<=20){
    		generalInfoCarBehind.setIcon(carBehindYellow);
		}else if(speedDifference<=40){
			generalInfoCarBehind.setIcon(carBehindOrange);
		}else{
			generalInfoCarBehind.setIcon(carBehindRed);
		}	
    	generalInfoCarBehindSpeed.setText("+ "+speedDifference+" Km/h");
    	generalInfoCarBehindSpeed.setVisible(true);
    	carBehindIconTimestamp = System.currentTimeMillis();
    }
    
    public void turnOnBrakeApplied(int brakeAmount, int distance){
    	if(brakeAmount<=20){
    		brakingCarAhead.setIcon(brakingYellow);
		}else if(brakeAmount<=40){
			brakingCarAhead.setIcon(brakingOrange);
		}else{
			brakingCarAhead.setIcon(brakingRed);
		}
    	brakingCarAheadSpeed.setText(brakeAmount+"%, "+distance+"m");
    	brakingCarAheadSpeed.setVisible(true);
    	brakeIconTimestamp = System.currentTimeMillis();
    }
    
    public void turnOnEmergencySiren(int distance, boolean behind){
		if(sirenFlashing!=true){
			sirenFlashing = true;
	    	sirenFlashingTimestamp = System.currentTimeMillis();
		}
		emergencySiren.setIcon(sirenIconOff);
		emergencySirenDistance.setText(distance+"m");
		emergencySirenDistance.setVisible(true);
		if(behind){
			sirenDirection.setVisible(true);
		}
    	sirenIconTimestamp = System.currentTimeMillis();
    }
    
    public void turnOnTrafficAhead(int trafficDensity, int distance, int avSpeed){
    	if(trafficDensity==1){
    		trafficAhead.setIcon(trafficAheadYellow);
		}else if(trafficDensity==2){
			trafficAhead.setIcon(trafficAheadOrange);
		}else{
			trafficAhead.setIcon(trafficAheadRed);
		}
		trafficAheadDistance.setText(distance+"m, "+avSpeed+" Km/h");
		trafficAheadDistance.setVisible(true);
		trafficAheadTimestamp = System.currentTimeMillis();
    }
    
    public void checkSiren(){
    	if(sirenFlashing){
    		if(sirenFlashingTimestamp+1000>System.currentTimeMillis()){
	    		if(sirenFlashingTimestamp+500>System.currentTimeMillis()){
	        		emergencySiren.setIcon(sirenIconOn);
	    		}else{
	        		emergencySiren.setIcon(sirenIconOff);
	    		}
    		}else{
    			sirenFlashingTimestamp =System.currentTimeMillis();
    		}
    	}
    }
    
    public void checkIconTimestamps(){
    	long currentTime = System.currentTimeMillis();
    	if(carAheadIconTimestamp!=0 && carAheadIconTimestamp+4000<currentTime){
    		generalInfoCarAhead.setIcon(generalInfoGreyAhead);
	    	generalInfoCarAheadSpeed.setVisible(false);
    		carAheadIconTimestamp=0;
    	}
		if(carBehindIconTimestamp!=0 && carBehindIconTimestamp+4000<currentTime){
			generalInfoCarBehind.setIcon(generalInfoGreyBehind);
	    	generalInfoCarBehindSpeed.setVisible(false);
			carBehindIconTimestamp=0;
		}
		if(brakeIconTimestamp!=0 && brakeIconTimestamp+2000<currentTime){
			brakingCarAhead.setIcon(brakingAheadGrey);
			brakingCarAheadSpeed.setVisible(false);
			brakeIconTimestamp=0;
		}
		if(sirenIconTimestamp!=0 && sirenIconTimestamp+2000<currentTime){
			emergencySiren.setIcon(sirenGrey);
			emergencySirenDistance.setVisible(false);
			sirenDirection.setVisible(false);
			sirenIconTimestamp=0;
			sirenFlashing = false;
		}
		if(trafficAheadTimestamp!=0 && trafficAheadTimestamp+5000<currentTime){
			trafficAhead.setIcon(trafficIconGrey);
			trafficAheadDistance.setVisible(false);
			trafficAheadTimestamp=0;
		}
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
    			sirenStatusLabel.setText("<html> <font style='font-weight:bold; color:#a0aaae'>Sirens OFF</u></html>");
    		}else{
    			waveManager.sirensOn=true;
    			sirenStatusLabel.setText("<html> <font style='font-weight:bold; color:green'>Sirens ON</u></html>");
    		}
    	}else if(e.getSource().equals(delayDownButton)){
    		waveManager.delay*=0.5;
			delay.setText("Smallest Delay = "+waveManager.delay+"ms");
    	}else if(e.getSource().equals(delayUpButton)){
    		waveManager.delay*=1.5;
			delay.setText("Smallest Delay = "+waveManager.delay+"ms");
    	}
    }
    
}
