import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface
{
    private JFrame mainFrame;
    
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel buttonPanel;
    private JPanel outputPanel;
    
    private JScrollPane scroll;
    
    private JLabel carID;
    private JLabel gps;
    private JLabel direction;
    private JLabel speed;
    private JLabel brakeAmount;
    private JLabel vehicleType;
    
    private JButton button1;
    private JButton button2;
    private JButton button3;

    private JTextArea output;

    public UserInterface()
    {
       mainFrame = new JFrame();
       
       mainPanel = new JPanel();
       topPanel = new JPanel();
       leftPanel = new JPanel();
       rightPanel = new JPanel();
       buttonPanel = new JPanel();
       outputPanel = new JPanel();
       
       button1 = new JButton("  Gas  ");
       button2 = new JButton("Brake");
       button3 = new JButton("Siren");

       carID = new JLabel("Car ID:");
       gps = new JLabel("GPS:");
       direction = new JLabel("Direction:");
       speed = new JLabel("Speed:");
       brakeAmount = new JLabel("Brake Amount:");
       vehicleType = new JLabel("Vehicle Type:");
       
       carID.setHorizontalAlignment(JLabel.CENTER);
       gps.setHorizontalAlignment(JLabel.CENTER);
       direction.setHorizontalAlignment(JLabel.CENTER);
       speed.setHorizontalAlignment(JLabel.CENTER);
       brakeAmount.setHorizontalAlignment(JLabel.CENTER);
       vehicleType.setHorizontalAlignment(JLabel.CENTER);
            
       output = new JTextArea();
       output.setFont(new Font("Open Sans", Font.PLAIN, 12));
       output.setEditable(false);

       scroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scroll.setPreferredSize(new Dimension(1100,500));
       
       topPanel.setLayout(new GridLayout(1,2));
       leftPanel.setLayout(new GridLayout(4,1));
       rightPanel.setLayout(new GridLayout(5,1));
       
       topPanel.setPreferredSize(new Dimension(500,100));
       leftPanel.setPreferredSize(new Dimension(500,150));
       rightPanel.setPreferredSize(new Dimension(500,150));
       buttonPanel.setPreferredSize(new Dimension(1000,50));
       outputPanel.setPreferredSize(new Dimension(1100,750));
       
       topPanel.add(leftPanel);
       topPanel.add(rightPanel);
       leftPanel.add(carID);
       leftPanel.add(vehicleType);
       rightPanel.add(gps);
       rightPanel.add(direction);
       rightPanel.add(speed);
       rightPanel.add(brakeAmount);
       buttonPanel.add(button1);
       buttonPanel.add(button2);
       buttonPanel.add(button3);
       outputPanel.add(scroll);
       
       mainPanel.add(topPanel);
       mainPanel.add(buttonPanel);
       mainPanel.add(outputPanel);
       mainFrame.add(mainPanel);
       
       mainFrame.setTitle("WAVE Interface");
       mainFrame.setSize(1150,750);
       mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       mainFrame.setVisible(true);
    }

    public void output(String outputText){
    	output.append(outputText+"\n");
    }
    
    public void writeCarID(String outputText){
    	carID.setText("CarID: "+outputText);
    }
    
    public void writeVehicleType(String outputText){
    	vehicleType.setText("Vehicle Type: "+outputText);
    }
    
    public void writeDirection(String outputText){
    	direction.setText("Direction: "+outputText);
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
    
    public void actionPerformed(ActionEvent e) {
    		
    }
    
}
