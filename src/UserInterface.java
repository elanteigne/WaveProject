import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private JPanel buttonPanel;
    private JPanel outputPanel;
    private JPanel packetInfoPanel;
    private JPanel delayPanel;
    private JPanel controlsPanel;
    private JPanel sentPacketInfoPanel;
    private JPanel receivedPacketInfoPanel;
    private JPanel computedDataPanel;
    private JPanel leftComputedDataPanel;
    private JPanel rightComputedDataPanel;
    
    private JLabel leftPanelLabel;
    private JLabel centerPanelLabel;
    private JLabel rightPanelLabel;
    private JLabel carID;
    private JLabel gps;
    private JLabel heading;
    private JLabel speed;
    private JLabel brakeAmount;
    private JLabel vehicleType;
    private JLabel generalInfo;
    private JLabel suggestedBrakeAmount;
    private JLabel suggestedBrakeSpeed;
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
    
    public int UIscale = 4;    
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
	   buttonPanel = new JPanel();
	   outputPanel = new JPanel();
	   outputPanel = new JPanel();       
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
	   
	   //Labels
	   leftPanelLabel = new JLabel("<html><u>Vehicle Info</u></html>");
	   centerPanelLabel = new JLabel("<html><u>Variable Data</u></html>");
	   rightPanelLabel = new JLabel("<html><u>Calculated Info</u></html>");
	   carID = new JLabel("Car ID: xxx-xxx-xxx-xxx");
	   gps = new JLabel("GPS: -, -");
	   heading = new JLabel("Heading: - degrees");
	   speed = new JLabel("Speed: -");
	   brakeAmount = new JLabel("Brake Amount: -");
	   vehicleType = new JLabel("Vehicle Type: -");
	   generalInfo = new JLabel("General Info:");
	   suggestedBrakeAmount = new JLabel("Speed Adjustment:");
	   suggestedBrakeSpeed = new JLabel("Speed of Brake Applied:");
	   sender = new JLabel("<html><u>Sender</u></html>");
	   generalInfoPacketsSent = new JLabel("GeneralInfoService Packets Sent: 0 ");
	   brakeServicePacketsSent = new JLabel("BreakService Packets Sent: 0 ");
	   emergencyServicePacketsSent = new JLabel("EmergencyService Packets Sent: 0 ");
	   trafficServicePacketsSent = new JLabel("TrafficService Packets Sent: 0 ");
	   receiver = new JLabel("<html><u>Receiver</u></html>");
	   numPacketsReceived = new JLabel("Received Packets: 0 ");
	   numPacketsPassed = new JLabel("Packets Passed: 0 ");
	   numPacketsOmitted = new JLabel("Omitted Packets: 0 ");
	   delay = new JLabel("Smallest Delay = "+waveManager.delay+"ms");
	   sirensLabel = new JLabel("OFF");
	
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
	   generalInfo.setHorizontalAlignment(JLabel.CENTER);
	   generalInfo.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   suggestedBrakeAmount.setHorizontalAlignment(JLabel.CENTER);
	   suggestedBrakeAmount.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
	   suggestedBrakeSpeed.setHorizontalAlignment(JLabel.CENTER);   
	   suggestedBrakeSpeed.setFont(new Font("Open Sans", Font.BOLD, OuterTextScale*2)); 
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
	   leftPanel.setLayout(new GridLayout(6,1));
	   centerPanel.setLayout(new GridLayout(6,1));
	   rightPanel.setLayout(new GridLayout(6,1));
	   buttonPanel.setLayout(new GridLayout(1,2));
	   packetInfoPanel.setLayout(new GridLayout(1,2));
	   sentPacketInfoPanel.setLayout(new GridLayout(5,1));
	   receivedPacketInfoPanel.setLayout(new GridLayout(4,1));
	   computedDataPanel.setLayout(new GridLayout(1,2));
	   
	   topPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*30));
	   leftPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*30));
	   centerPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*30));
	   rightPanel.setPreferredSize(new Dimension(UIscale*140,UIscale*30));
	   buttonPanel.setPreferredSize(new Dimension(UIscale*200,UIscale*10));
	   outputPanel.setPreferredSize(new Dimension(UIscale*225,UIscale*150));
	   packetInfoPanel.setPreferredSize(new Dimension(UIscale*160,UIscale*16));
	   sentPacketInfoPanel.setPreferredSize(new Dimension(UIscale*100,UIscale*8));
	   receivedPacketInfoPanel.setPreferredSize(new Dimension(UIscale*100,UIscale*20));
	   computedDataPanel.setPreferredSize(new Dimension(UIscale*225,UIscale*200));
	   leftComputedDataPanel.setPreferredSize(new Dimension(UIscale*110,UIscale*140));
	   rightComputedDataPanel.setPreferredSize(new Dimension(UIscale*110,UIscale*140));
	   delayPanel.setPreferredSize(new Dimension(UIscale*10,UIscale*20));
	   outputLabel.setPreferredSize(new Dimension(UIscale*225,UIscale*8));
	   
	   //Add components to panels
	   topPanel.add(leftPanel);
	   topPanel.add(centerPanel);
	   topPanel.add(rightPanel);
	   leftPanel.add(leftPanelLabel);
	   leftPanel.add(carID);
	   leftPanel.add(vehicleType);
	   centerPanel.add(centerPanelLabel);
	   centerPanel.add(gps);
	   centerPanel.add(heading);
	   centerPanel.add(speed);
	   centerPanel.add(brakeAmount);
	   rightPanel.add(rightPanelLabel);
	   rightPanel.add(generalInfo);
	   rightPanel.add(suggestedBrakeAmount);
	   rightPanel.add(suggestedBrakeSpeed);
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
	   outputPanel.add(outputLabel);
	   outputPanel.add(consoleScroll);
	   outputPanel.add(computedDataPanel);
	   packetInfoPanel.add(sentPacketInfoPanel);
	   packetInfoPanel.add(receivedPacketInfoPanel);
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
	   mainFrame.setSize(UIscale*230,UIscale*50);
	   mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   mainFrame.setVisible(true);
	   
	   devFrame.add(devPanel); 
	   devFrame.setTitle("WAVE Dev Interface");
	   devFrame.setSize(UIscale*230,UIscale*150);
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
    			 writeSpeed(waveManager.speed);
    			 writeBrakeAmount(waveManager.brakeAmount);
    			 writeHeading(waveManager.heading);
    			 writeGPS(waveManager.GPSlattitude, waveManager.GPSlongitude);
    			 writeVehicleType(waveManager.vehicleType);
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
    
    public void writeGeneralInfo(String outputText){
    	generalInfo.setText("General Info: "+outputText);
    }
    
    public void writeSuggestedSpeedAdjustment(String outputText){
    	suggestedBrakeAmount.setText("Speed Adjustment: "+outputText);
    }
    
    public void writeSpeedBrakeApplied(String outputText){
    	suggestedBrakeSpeed.setText("Speed of Brake Applied: "+outputText);
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
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().equals(speedUpButton)){
    		if(waveManager.speed>=0&&waveManager.speed<100){
        		waveManager.speed+=10;
    		}
    	}else if(e.getSource().equals(speedDownButton)){
    		if(waveManager.speed>0&&waveManager.speed<=100){
        		waveManager.speed-=10;
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
    
}
