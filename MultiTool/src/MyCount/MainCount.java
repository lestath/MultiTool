package MyCount;

import java.awt.*;
import javax.swing.JPanel;

public class MainCount extends JPanel {
 /**
	 * 
	 */
	private static final long serialVersionUID = 11L;

public MainCount(){
	 GridBagConstraints c = new GridBagConstraints();
	 setPreferredSize(new Dimension(780,500));
	 
	 CountGraph graph = new CountGraph();
	 PanelRight panelright = new PanelRight();
	 
	 panelright.setGraph(graph);
	 
	// c.weightx = 0.1;
	 c.ipadx = 0;
	 c.ipady =0;
	 c.gridwidth=5;
	 add(graph);
	 c.ipadx = 5;
	 c.ipady =0;
	// c.weightx = 0.9;
	 c.gridwidth=2;
	 c.insets= new Insets(0,10,0,0);
	 add(panelright);
	 this.setVisible(true);
	 
	 
 }
}
