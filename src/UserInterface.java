import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface implements Runnable{
	//Objects
	private Thread userInterfaceThread;    
	
	//UI Objects
	private JFrame mainFrame;
    
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel buttonPanel;
    private JPanel outputPanel;
    private JPanel computedDataPanel;
    private JPanel generalInfoServicePanel;
    private JPanel brakeServicePanel;
    
    private JLabel leftPanelLabel;
    private JLabel centerPanelLabel;
    private JLabel rightPanelLabel;
    private JLabel carID;
    private JLabel gps;
    private JLabel bearing;
    private JLabel speed;
    private JLabel brakeAmount;
    private JLabel vehicleType;
    private JLabel generalInfo;
    private JLabel suggestedBrakeAmount;
    private JLabel suggestedBrakeSpeed;
    
    private JButton button1;
    private JButton button2;
    private JButton button3;

    private JTextArea output;
    private JScrollPane scroll;
    
    private JTextArea computedGeneralInfo;
    private JScrollPane computedGeneralInfoScroll;
    private JTextArea computedBrakeInfo;
    private JScrollPane computedBrakeInfoScroll;

	//Class Methods
	public void start(){
		if(userInterfaceThread==null){
			userInterfaceThread = new Thread(this, "UserInterfaceThread");
			userInterfaceThread.start();
		}
	}
    
    public UserInterface()
    {
       mainFrame = new JFrame();
       
       mainPanel = new JPanel();
       topPanel = new JPanel();
       leftPanel = new JPanel();
       centerPanel = new JPanel();
       rightPanel = new JPanel();
       buttonPanel = new JPanel();
       outputPanel = new JPanel();
       computedDataPanel = new JPanel();
       generalInfoServicePanel = new JPanel();
       brakeServicePanel = new JPanel();
       
       button1 = new JButton("  Gas  ");
       button2 = new JButton("Brake");
       button3 = new JButton("Siren");

       leftPanelLabel = new JLabel("<html><u>Vehicle Info</u></html>");
       centerPanelLabel = new JLabel("<html><u>Variable Data</u></html>");
       rightPanelLabel = new JLabel("<html><u>Calculated Info</u></html>");
       carID = new JLabel("Car ID: xxx-xxx-xxx-xxx");
       gps = new JLabel("GPS: -, -");
       bearing = new JLabel("Bearing: - degrees");
       speed = new JLabel("Speed: -");
       brakeAmount = new JLabel("Brake Amount: -");
       vehicleType = new JLabel("Vehicle Type: -");
       generalInfo = new JLabel("General Info:");
       suggestedBrakeAmount = new JLabel("Speed Adjustment:");
       suggestedBrakeSpeed = new JLabel("Speed of Brake Applied:");

       leftPanelLabel.setHorizontalAlignment(JLabel.CENTER);
       centerPanelLabel.setHorizontalAlignment(JLabel.CENTER);
       rightPanelLabel.setHorizontalAlignment(JLabel.CENTER);
       carID.setHorizontalAlignment(JLabel.CENTER);
       gps.setHorizontalAlignment(JLabel.CENTER);
       bearing.setHorizontalAlignment(JLabel.CENTER);
       speed.setHorizontalAlignment(JLabel.CENTER);
       brakeAmount.setHorizontalAlignment(JLabel.CENTER);
       vehicleType.setHorizontalAlignment(JLabel.CENTER);
       generalInfo.setHorizontalAlignment(JLabel.CENTER);
       suggestedBrakeAmount.setHorizontalAlignment(JLabel.CENTER);
       suggestedBrakeSpeed.setHorizontalAlignment(JLabel.CENTER);

       JLabel outputLabel = new JLabel("<html><u>Sending/Receiving</u></html>");
       output = new JTextArea();
       output.setFont(new Font("Open Sans", Font.PLAIN, 12));
       output.setEditable(false);
       scroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scroll.setPreferredSize(new Dimension(1100,200));

       JLabel generalInfoServiceOutputLabel = new JLabel("<html><u>General Info Service Computed Information</u></html>");
       computedGeneralInfo = new JTextArea();
       computedGeneralInfo.setFont(new Font("Open Sans", Font.PLAIN, 12));
       computedGeneralInfo.setEditable(false);
       computedGeneralInfoScroll = new JScrollPane (computedGeneralInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       computedGeneralInfoScroll.setPreferredSize(new Dimension(540,200));

       JLabel brakeServiceOutputLabel = new JLabel("<html><u>Brake Service Computed Information</u></html>");
       computedBrakeInfo = new JTextArea();
       computedBrakeInfo.setFont(new Font("Open Sans", Font.PLAIN, 12));
       computedBrakeInfo.setEditable(false);
       computedBrakeInfoScroll = new JScrollPane (computedBrakeInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       computedBrakeInfoScroll.setPreferredSize(new Dimension(540,200));
       
       topPanel.setLayout(new GridLayout(1,3));
       leftPanel.setLayout(new GridLayout(5,1));
       centerPanel.setLayout(new GridLayout(6,1));
       rightPanel.setLayout(new GridLayout(5,1));
       computedDataPanel.setLayout(new GridLayout(1,2));
       
       topPanel.setPreferredSize(new Dimension(600,150));
       leftPanel.setPreferredSize(new Dimension(600,150));
       centerPanel.setPreferredSize(new Dimension(600,150));
       rightPanel.setPreferredSize(new Dimension(600,150));
       buttonPanel.setPreferredSize(new Dimension(1000,50));
       outputPanel.setPreferredSize(new Dimension(1125,750));
       computedDataPanel.setPreferredSize(new Dimension(1125,1000));
       generalInfoServicePanel.setPreferredSize(new Dimension(550,1000));
       brakeServicePanel.setPreferredSize(new Dimension(550,1000));
       
       topPanel.add(leftPanel);
       topPanel.add(centerPanel);
       topPanel.add(rightPanel);
       leftPanel.add(leftPanelLabel);
       leftPanel.add(carID);
       leftPanel.add(vehicleType);
       centerPanel.add(centerPanelLabel);
       centerPanel.add(gps);
       centerPanel.add(bearing);
       centerPanel.add(speed);
       centerPanel.add(brakeAmount);
       rightPanel.add(rightPanelLabel);
       rightPanel.add(generalInfo);
       rightPanel.add(suggestedBrakeAmount);
       rightPanel.add(suggestedBrakeSpeed);
       buttonPanel.add(button1);
       buttonPanel.add(button2);
       buttonPanel.add(button3);
       outputPanel.add(outputLabel);
       outputPanel.add(scroll);
       outputPanel.add(computedDataPanel);
       computedDataPanel.add(generalInfoServicePanel);
       computedDataPanel.add(brakeServicePanel);
       generalInfoServicePanel.add(generalInfoServiceOutputLabel);
       generalInfoServicePanel.add(computedGeneralInfoScroll);
       brakeServicePanel.add(brakeServiceOutputLabel);
       brakeServicePanel.add(computedBrakeInfoScroll);
       
       mainPanel.add(topPanel);
       mainPanel.add(buttonPanel);
       mainPanel.add(outputPanel);
       
       mainFrame.add(mainPanel); 
       mainFrame.setTitle("WAVE Interface");
       mainFrame.setSize(1150,1000);
       mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       mainFrame.setVisible(true);
    }
    
    public void run(){
    	while(true){
    		 try{
    			 output.setCaretPosition(output.getDocument().getLength());
    			 computedGeneralInfo.setCaretPosition(computedGeneralInfo.getDocument().getLength());
    			 computedBrakeInfo.setCaretPosition(computedBrakeInfo.getDocument().getLength());
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
    
    public void writeCarID(String outputText){
    	carID.setText("CarID: "+outputText);
    }
    
    public void writeVehicleType(String outputText){
    	vehicleType.setText("Vehicle Type: "+outputText);
    }
    
    public void writeBearing(int outputText){
    	bearing.setText("Bearing: "+outputText+" degrees");
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
    
    public void actionPerformed(ActionEvent e) {
    		
    }
    
}
