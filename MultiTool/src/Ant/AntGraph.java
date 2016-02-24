package Ant;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class AntGraph extends JPanel implements Runnable {

 /**
	 * 
	 */
private static final long serialVersionUID = 10L;
private int X,Y;
private BufferedImage ANT;
private AffineTransform TRANSF;
//private boolean UP;
//private boolean TURN;

 public AntGraph(){
	   // TURN = false;
	   // UP = true;
	    URL resource = getClass().getResource("ant.png");
		    try {
				this.ANT= ImageIO.read(resource);
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		TRANSF = new AffineTransform();
		TRANSF.rotate(Math.toRadians(20));
		//inicjalizacja ustawień panelu
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.setPreferredSize(new Dimension(600,500));
		this.X = 150;
		this.Y = 460;
		Thread thr = new Thread(this);
		thr.start();
 }
 
 public BufferedImage rotate90ToRight( BufferedImage inputImage ){
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		BufferedImage returnImage = new BufferedImage( height, width , inputImage.getType()  );

		for( int x = 0; x < width; x++ ) {
			for( int y = 0; y < height; y++ ) {
				returnImage.setRGB(height-y -1, x, inputImage.getRGB( x, y  ));
	//Again check the Picture for better understanding
			}
		}
		return returnImage;
	}
 
 @Override
 protected void paintComponent(Graphics g){

	Graphics2D g2d = (Graphics2D) g;
	if(this.X == 150){
		if(this.Y == 460){
			this.ANT = rotate90ToRight(this.ANT);
		}
		this.Y=this.Y-1;
	}
	if(this.X == 300){
		if(this.Y == 10){
			this.ANT = rotate90ToRight(this.ANT);
		}
		this.Y=this.Y+1;
	}
	if(this.Y == 460){
		if(this.X == 300){
			this.ANT = rotate90ToRight(this.ANT);
		}
		this.X = this.X -1;
	}
	if(this.Y == 10){
		if(this.X == 150){
			this.ANT = rotate90ToRight(this.ANT);
		}
		this.X = this.X +1;
	}
	
	g2d.clearRect (0, 0, this.getWidth(),this.getHeight());
	g2d.drawImage(this.ANT,this.X,this.Y,this);

 }
 
 @Override
  public void run(){
		while(true){
			 this.repaint();
			 try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
  }
}
