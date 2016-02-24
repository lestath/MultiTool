import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;

import MyCount.MainCount;
import MyGraph.*;
import Ant.*;
public class MainFrame extends JFrame{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	MainFrame(){
		  super("Multi Tool");
		  ImageIcon mainicon = new ImageIcon(getClass().getClassLoader().getResource("images/mainicon.png")); 
		  ImageIcon graphicon = new ImageIcon(getClass().getClassLoader().getResource("images/graph.png"));
		  ImageIcon anticon = new ImageIcon(getClass().getClassLoader().getResource("images/ant.png"));
		  ImageIcon counticon = new ImageIcon(getClass().getClassLoader().getResource("images/count.png"));
		  
		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  setIconImage(mainicon.getImage());
		  setSize(780,650);
		  setResizable(false);
		  setVisible(true);
		  MainGraphPanel graph = new MainGraphPanel();
		  MainCount count = new MainCount();
		  MainAnt ant = new MainAnt();
		  
		  
		  JTabbedPane MainTabbedPanel = new JTabbedPane();
		  
		  MainTabbedPanel.addTab("Graph ",graphicon,graph,"You can draw simple graphs");	
		 // MainTabbedPanel.addTab("Counts",counticon,count,"You can upload counts");
		 // MainTabbedPanel.addTab("Ants",anticon,ant,"Ants");
		  
		  add(MainTabbedPanel);
		  
		 }
	}