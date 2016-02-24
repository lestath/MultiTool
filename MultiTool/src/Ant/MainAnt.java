package Ant;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class MainAnt extends JPanel {
 /**
	 * 
	 */
	private static final long serialVersionUID = 9L;

	public MainAnt(){
		 this.setLayout(new GridBagLayout());
		 GridBagConstraints c = new GridBagConstraints();
		 setPreferredSize(new Dimension(780,500));
		 
		 AntGraph graph = new AntGraph();
		 PanelRight panelright = new PanelRight();
		 
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
