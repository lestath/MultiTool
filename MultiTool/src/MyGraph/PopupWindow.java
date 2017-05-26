package MyGraph;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class PopupWindow extends JFrame {
JLabel screen;
 
 public PopupWindow(JLabel screen,String title,Dimension size){
	 super(title);
	 this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
	 setSize(size);
	 screen.setSize(size);
	 setScreen(screen);
	 this.setLayout(fl);
	 this.add(getScreen());
	 this.setLocationRelativeTo(null);
	 setResizable(false);
	 setVisible(true);
 }
 
 public JLabel getScreen() {
		return screen;
	}

	public void setScreen(JLabel screen) {
		this.screen = screen;
	}

 
 
}
