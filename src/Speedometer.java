import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
	/**
	 * 
	 * 
	 */
public class Speedometer extends JLabel{

	public int speed = 1;
	private int height= 200;
	private int width = 200;
	private int initialAngle = 59;
	private static final long serialVersionUID = 1L;

	public int[] getCoordXY(int speed, int width, int height){
		double pX, pY;
		double angle = initialAngle;
		pX = this.getBounds().getWidth()/4 + width/2 + (7*width/16)*Math.sin(Math.toRadians(angle+speed))*(-1);
		pY = this.getBounds().getHeight()/4 + height/2 + (7*height/16)*Math.cos(Math.toRadians(angle+speed));
		int[] coord = {(int)pX , (int)pY};
		return coord;
	}

	@Override
	public void paint(Graphics g) {
		
		
		Graphics2D g2d = (Graphics2D) g;
        
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(15,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));

		g2d.drawLine(5, 5, 5, this.getHeight()-5);
		g2d.drawLine(5, 5, this.getWidth()-5, 5);
		g2d.drawLine(5, this.getHeight()-5, this.getWidth()-5, this.getHeight()-5);
		g2d.drawLine(this.getWidth()-5, this.getHeight()-5, this.getWidth()-5 ,5);
		g2d.fill(new Ellipse2D.Double(this.getBounds().getWidth()/4, this.getBounds().getHeight()/4, width, height));
		System.out.println(this.getSize().getWidth());
		System.out.println(this.getSize().getHeight());
		g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));

		//Draw border of Speedmeter
		g2d.setColor(Color.GREEN);
		g2d.draw(new Ellipse2D.Double(this.getBounds().getWidth()/4, this.getBounds().getHeight()/4, width, height));
		//g2d.draw(new Ellipse2D.Double(width/16, height/16, 7*width/8, 7*height/8));
		
		//Find points for tick reference
		int[] p0 = this.getCoordXY(0, width, height);
		int[] p20 = this.getCoordXY(20, width, height);
		int[] p40 = this.getCoordXY(40, width, height);
		int[] p60 = this.getCoordXY(60, width, height);
		int[] p80 = this.getCoordXY(80, width, height);
		int[] p100 = this.getCoordXY(100, width, height);
		int[] p120 = this.getCoordXY(120, width, height);
		int[] p140 = this.getCoordXY(140, width, height);
		int[] p160 = this.getCoordXY(160, width, height);
		int[] p180 = this.getCoordXY(180, width, height);
		int[] p200 = this.getCoordXY(200, width, height);
		int[] p220 = this.getCoordXY(220, width, height);
		int[] p240 = this.getCoordXY(240, width, height);

		g2d.setColor(Color.WHITE);
		g2d.drawString("0",(int)(p0[0]-1*width/32),(int)(p0[1]+1*width/32));
		g2d.drawString("20",(int)(p20[0]-1*width/32),(int)(p20[1]+1*width/32));
		g2d.drawString("40",(int)(p40[0]-1*width/32),(int)(p40[1]+1*width/32));
		g2d.drawString("60",(int)(p60[0]-1*width/32),(int)(p60[1]+1*width/32));		
		g2d.drawString("80",(int)(p80[0]-1*width/32),(int)(p80[1]+1*width/32));
		g2d.drawString("100",(int)(p100[0]-2*width/32),(int)(p100[1]+1*width/32));
		g2d.drawString("120",(int)(p120[0]-2*width/32),(int)(p120[1]+1*width/32));		
		g2d.drawString("140",(int)(p140[0]-2*width/32),(int)(p140[1]+1*width/32));		
		g2d.drawString("160",(int)(p160[0]-2*width/32),(int)(p160[1]+1*width/32));		
		g2d.drawString("180",(int)(p180[0]-2*width/32),(int)(p180[1]+1*width/32));		
		g2d.drawString("200",(int)(p200[0]-2*width/32),(int)(p200[1]+1*width/32));		
		g2d.drawString("220",(int)(p220[0]-2*width/32),(int)(p220[1]+1*width/32));		
		g2d.drawString("240",(int)(p240[0]-2*width/32),(int)(p240[1]+1*width/32));

		g2d.setColor(Color.RED);
		switch(speed){
		case 20:   g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p20[0], p20[1]);  break;  //20 line
		case 40:   g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p40[0], p40[1]);  break;  //40 line
		case 60:   g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p60[0], p60[1]);  break;  //60 line
		case 80:   g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p80[0], p80[1]);  break;  //80 line
		case 100:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p100[0], p100[1]);  break;  //100 line
		case 120:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p120[0], p120[1]);  break;  //120 line
		case 140:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p140[0], p140[1]);  break;  //140 line
		case 160:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p160[0], p160[1]);  break;  //160 line
		case 180:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 +(int) this.getBounds().getHeight()/4, p180[0], p180[1]);  break;  //180 line
		case 200:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p200[0], p200[1]);  break;  //200 line
		case 220:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p220[0], p220[1]);  break;  //220 line
		case 240:  g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p240[0], p240[1]);  break;  //240 line
		case 0:    g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p0[0], p0[1]);	break;  //0 line
		default:   g2d.drawLine(width/2 + (int) this.getBounds().getWidth()/4, height/2 + (int) this.getBounds().getHeight()/4, p0[0], p0[1]);	break;
		}

	}
	public static void main(String[] args){
		//waveManager = new WaveManager();
		JTextArea output = new JTextArea();
;
		JScrollPane scroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);;
		JFrame frame = new JFrame("Speedometer Test");
		frame.setLayout(new BorderLayout());
		Speedometer s = new Speedometer();
		s.setSize(s.getWidth()/2, s.getHeight()/2);
		frame.add(new JButton("North"), BorderLayout.NORTH);
	    frame.add(new JButton("South"), BorderLayout.SOUTH);
	    frame.add(new JButton("East"), BorderLayout.EAST);
	    //frame.add(new JButton("West"), BorderLayout.WEST);
		frame.add(s,BorderLayout.CENTER);
		//frame.add(s,BorderLayout.SOUTH);
		frame.setSize(1000, 500);
		//frame.pack();
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
			s.speed = 240;
			s.validate();
			s.repaint();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
