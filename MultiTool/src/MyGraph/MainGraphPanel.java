package MyGraph;

import java.awt.*;
import javax.swing.JPanel;


public class MainGraphPanel extends JPanel{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 6L;

	public MainGraphPanel(){
		  this.setLayout(new GridBagLayout());  	  
		  GridBagConstraints c = new GridBagConstraints();
		  setPreferredSize(new Dimension(600,500));
			c.fill = GridBagConstraints.HORIZONTAL;
			
			c.weightx = 0.5;			
			GraphPanel gPanel = new GraphPanel();
			ButtonPanel b1 = new ButtonPanel();
			PanelRight panelRight = new PanelRight();
			TopBar topbar = new TopBar();
			
            c.insets = new Insets(0,0,0,0);
            

            b1.addGraph(gPanel);   
            panelRight.addGraph(gPanel);
            topbar.addGraph(gPanel);
            
            c.insets = new Insets(0,0,0,0);
            c.ipady = 30;
            c.ipadx = 0;
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.00;
			add(topbar,c);
			
            c.ipady =60;
            c.ipadx = 780;
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0.0;
			add(b1,c);
			
			
           //	c.gridwidth = 1;
            c.ipady = 500;
            c.ipadx = 650;
			c.gridx = 0;
			c.gridy = 2;	
			c.weightx = 0.6;
			gPanel.panelRight = panelRight;
			gPanel.buttonPanel = b1;
			add(gPanel, c);

			c.ipady = 500;
			c.ipadx = 130;
			c.gridx = 5;
			c.gridy = 2;		
			c.weightx = 0.95;
            add(panelRight,c);
            
			setVisible(true);
		 }
	}
