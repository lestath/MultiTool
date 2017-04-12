package MyGraph;

import java.awt.*;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * klasa panelu przycisków z słuchaczem zdarzeń
 * 
*/
public class ButtonPanel extends JPanel implements ActionListener{
	 
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private JButton b1;
	private JButton b2;
	private JButton b3;
	 
	 private JTextField t1;
	 
	 private GraphPanel g;
	 
	 public JLabel Label1;
	 
	 public ButtonPanel(){
			  this.Label1 = new JLabel("f(x)=");
			  FlowLayout f= new FlowLayout(FlowLayout.LEFT);
		      this.setPreferredSize(new Dimension(400,100));
			  b1 = new JButton("Draw");		  
			  b1.addActionListener(this);		  
			  b1.setFocusPainted(false);  
			  
			  b2 = new JButton("Coordinate System");
			  b2.addActionListener(this);
			  b2.setFocusPainted(false);
			  
			  b3 = new JButton("Clear");
			  b3.addActionListener(this);
			  b3.setFocusPainted(false);
			  t1 = new JTextField();
			  t1.setPreferredSize(new Dimension(200,24));
			  
			  add(this.Label1);
			  add(t1);
			  add(b1);
			  add(b2);
			  add(b3);
			  
			 }
	@Override
	public void actionPerformed(ActionEvent e){
		 Object source = e.getSource();
		 if(source==this.b1){
				g.allowGraph = true;		 
				g.insertPattern(this.t1.getText());
				g.repaint();		 
		 }else if(source == this.b2){
			 if(g.allowCorSys){
				g.allowCorSys=false;
				g.repaint();  
			  }else{
				g.allowCorSys=true;
				g.repaint(); 
			  }
		 }else if(source == this.b3){
			 g.allowGraph = false;
			 g.clearAll();
			 g.repaint();
		 }
	}
    

    public void addGraph(GraphPanel g){
		 this.g=g;
		}
	}

 
