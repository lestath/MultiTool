package MyCount;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
//import java.awt.Graphics2D;


public class CountGraph extends JPanel{
	 /**
		 * 
		 */
    private static final long serialVersionUID =12L;
    private boolean FROMFILEFLAG; // flaga rysowania grafu z pliku
    private File TEXTFILE; // zmienna plikowa 

	public CountGraph(){
		this.FROMFILEFLAG = false;
		this.TEXTFILE = null;
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.setPreferredSize(new Dimension(600,500));
	}
	
	@Override 
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect (0, 0, this.getWidth(),this.getHeight());
		if(this.FROMFILEFLAG){
		 this.drawFromFile(g2d);
		}
	}
	
	public void drawCountFromFile(File f){
		this.TEXTFILE  = f;
		this.FROMFILEFLAG = true;
		this.repaint();
	}
   public void drawFromFile(Graphics2D g2d){
	 if(this.TEXTFILE != null){
		 g2d.setColor(Color.CYAN);
		 g2d.drawLine(0,(this.getHeight()/2),this.getWidth(),(this.getHeight()/2));
	 }else{
		this.FROMFILEFLAG = false;
		
		this.repaint();
	 }  
   }
}
