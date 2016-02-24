package MyCount;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class PanelRight extends JPanel implements ActionListener{
 /**
	 * 
	 */
private static final long serialVersionUID = 18L;
private JButton FILELOADBUTTON;
private JButton NEWCOUNTBUTTON;
private CountGraph GRAPH;
 
public PanelRight(){
	 this.GRAPH = null;
	 this.setLayout(new FlowLayout(FlowLayout.LEADING));
	 this.setPreferredSize(new Dimension(150,500));
	 
	 
	 this.FILELOADBUTTON = new JButton("Load Count");
	 this.FILELOADBUTTON.addActionListener(this);
	 this.FILELOADBUTTON.setPreferredSize(new Dimension(140,24));
	 this.FILELOADBUTTON.setFocusPainted(false);
	 
	 this.NEWCOUNTBUTTON = new JButton("New Count");
	 this.NEWCOUNTBUTTON.setPreferredSize(new Dimension(140,24));
	 this.NEWCOUNTBUTTON.addActionListener(this);
	 this.NEWCOUNTBUTTON.setFocusPainted(false);
	 
	 
	 
	 this.add(this.FILELOADBUTTON);
	 this.add(this.NEWCOUNTBUTTON);
 }
 
 @Override
 public void actionPerformed(ActionEvent e){
 	Object source = e.getSource();
	 if(source == FILELOADBUTTON){
		 final JFileChooser fc = new JFileChooser();
		 int retVal = fc.showOpenDialog(this);
		 if(retVal == fc.APPROVE_OPTION){
			File f = fc.getSelectedFile();
			if(this.GRAPH != null){
			 GRAPH.drawCountFromFile(f);	
			}
		 }
	 }else if(source == this.NEWCOUNTBUTTON){
	   this.GRAPH.drawCountFromFile(null);
	 }
 }
 
	public void setGraph(CountGraph g){
		this.GRAPH = g;
	}
}
