package MyGraph;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import MyUtils.CoorSys;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelRight extends JPanel implements ActionListener{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public JTextField COORX;
	 public JTextField COORY;
	 public JTextField SCALEX;
	 public JTextField SCALEY;
	 public JTextField DELTA; // przyrost argumentowy 
	 public JTextField MAXFI; // górna granica kąta fi
	 public JTextField HighIntegralLim;
	 public JTextField LowIntegralLim;
	 public JTextField IntegralResult;
	 public JButton RescaleButton;
	 public JButton ChangeSystemButton;
	 public JButton IntegralButton;
	 private GraphPanel graph;
	 public JLabel SystemNameLabel;
	 public JLabel TLabel; 
	 public JLabel PiLabel;
	 public JLabel FakeLabel;
	 public JLabel IntegralLabel;
	 public JLabel IntegralFuncLabel;
	 public JLabel FakeLabel1;
	 public JLabel IntegralTitle;
	 public JLabel xlabel;
	 public JLabel ylabel;
	 
	 public PanelRight(){
		 this.graph = null;
         setLayout(new FlowLayout(FlowLayout.LEADING));
		 /* deklaracja i inicjalizacja elementów */
		 xlabel = new JLabel("X :");
		 ylabel = new JLabel("Y :");
		 JLabel sxlabel = new JLabel("OX :");
		 JLabel sylabel = new JLabel("OY :");
		 JLabel px1label = new JLabel("px");
		 JLabel px2label = new JLabel("px");
		 JLabel scaleTitle = new JLabel("Scale :");
		 IntegralTitle = new JLabel("Integral :");
		 String str = "\u0394";
		 JLabel deltaLabel = new JLabel(str+"x :");
		 str = "\u03C0";
		 PiLabel = new JLabel(str);
         str = "\u222B";
         IntegralLabel = new JLabel(str);
         IntegralLabel.setFont(new Font("Serif", Font.LAYOUT_RIGHT_TO_LEFT, 30));
         IntegralFuncLabel = new JLabel("f(x)dx");
         
		 PiLabel.setVisible(false);
		 TLabel = new JLabel("T :");
		 TLabel.setVisible(false);
		 FakeLabel = new JLabel("     ");
		 FakeLabel1 = new JLabel("     ");
		 FakeLabel.setVisible(true);
		 
		 RescaleButton = new JButton("Rescale");
		 RescaleButton.addActionListener(this);

		 IntegralButton = new JButton("=");
		 IntegralButton.addActionListener(this);
		 SystemNameLabel = new JLabel("Cartesian");
		 
		 ChangeSystemButton = new JButton("Cartes");
		 ChangeSystemButton.addActionListener(this);
		 
		 LowIntegralLim = new JTextField("0.000");
		 HighIntegralLim = new JTextField("0.000");
		 IntegralResult = new JTextField("0.000");
		 COORX = new JTextField("0.00");
		 COORY = new JTextField("0.00");
		 SCALEX = new JTextField("50");
		 SCALEY = new JTextField("50");
		 DELTA = new JTextField("0.01");
		 MAXFI = new JTextField("12");
		 MAXFI.setVisible(false);
		 
		 
		 SystemNameLabel.setPreferredSize(new Dimension(130,24));
		 xlabel.setPreferredSize(new Dimension(30,24));
		 ylabel.setPreferredSize(new Dimension(30,24));
		 deltaLabel.setPreferredSize(new Dimension(25,24));
		 TLabel.setPreferredSize(new Dimension(25,24));
		 COORX.setPreferredSize(new Dimension(90,24));
         COORY.setPreferredSize(new Dimension(90,24));
         SCALEX.setPreferredSize(new Dimension(60,24));
         SCALEY.setPreferredSize(new Dimension(60,24));
         DELTA.setPreferredSize(new Dimension(100,24));
         MAXFI.setPreferredSize(new Dimension(85,24));
         HighIntegralLim.setPreferredSize(new Dimension(50,24));
         LowIntegralLim.setPreferredSize(new Dimension(50,24));
         FakeLabel.setPreferredSize(new Dimension(130,24));
         FakeLabel1.setPreferredSize(new Dimension(80,24));
		 sxlabel.setPreferredSize(new Dimension(30,24));
		 sylabel.setPreferredSize(new Dimension(30,24));
		 px1label.setPreferredSize(new Dimension(25,24));
		 px1label.setPreferredSize(new Dimension(25,24));
		 PiLabel.setPreferredSize(new Dimension(10,24));
		 IntegralLabel.setPreferredSize(new Dimension(20,40));
		 IntegralFuncLabel.setPreferredSize(new Dimension(50,40));
		 IntegralResult.setPreferredSize(new Dimension(130,24));
		 scaleTitle.setPreferredSize(new Dimension(130,24));
		 IntegralTitle.setPreferredSize(new Dimension(130,24));
		 RescaleButton.setPreferredSize(new Dimension(130,24));
		 ChangeSystemButton.setPreferredSize(new Dimension(130,24));
		 IntegralButton.setPreferredSize(new Dimension(50,24));
		 
		 RescaleButton.setFocusPainted(false);
		 ChangeSystemButton.setFocusPainted(false);
		 IntegralButton.setFocusPainted(false);
		 
         setVisible(true);

        
         COORX.setEditable(false);
         COORY.setEditable(false);
         IntegralResult.setEditable(false);
        
         /* dodanie elementów do layoutu */
          add(SystemNameLabel);
          add(xlabel);
          add(COORX);
          add(ylabel);
          add(COORY);
          add(scaleTitle);
          add(sxlabel);
          add(SCALEX);
          add(px1label);
          add(sylabel);
          add(SCALEY);
          add(px2label);
          add(RescaleButton);
          add(ChangeSystemButton);
          add(deltaLabel);
          add(DELTA);  
          add(TLabel);
          add(MAXFI);
          add(PiLabel);
          add(FakeLabel);
          add(IntegralTitle);
          add(HighIntegralLim);
          add(FakeLabel1);
          add(IntegralLabel);
          add(IntegralFuncLabel);
          add(IntegralButton);
          add(LowIntegralLim);
          add(IntegralResult);
        //  add(IntegralFuncLabel);
		 }

/**
 * Metoda wiążąca z panelem graficznym (przydatna w delegacjach)
 * @param graph
 */
public void addGraph(GraphPanel graph){
	this.graph = graph;
	}

@Override
public void actionPerformed(ActionEvent e){
	Object source = e.getSource();
		 if(source == this.RescaleButton){
			  if(this.graph!=null){
				//   this.graph.clearAll();
				   int sx = 50;
				   int sy = 50;
				  // this.graph.allowGraph=false;
				   try{
				    sx = Integer.parseInt(this.SCALEX.getText());
				    sy = Integer.parseInt(this.SCALEY.getText());
			       }catch(Exception x){
					sx = 50;
					sy = 50;   
				   }
				   finally{
					    if(sx<10){sx = 10; }
					    if(sy<10){sy = 10; }
					   }
					this.graph.scaleX = sx;
					this.graph.scaleY = sy;
					this.SCALEX.setText(Integer.toString(sx));
					this.SCALEY.setText(Integer.toString(sy));	
					this.graph.setRescalemode(true); //wchodzimy w tryb przeskalowania
				    this.graph.repaint();
				  }
			 }else if(source == this.ChangeSystemButton){
				if(this.graph.getSystem()==CoorSys.POLAR){
					this.ChangeSystemButton.setText("Cartes");
				}else{
					this.ChangeSystemButton.setText("Polar    ");
				}
				this.graph.changeSystem();	
				
			 }else if(source == this.IntegralButton){
				double l;
				double h;
				try{
				 l = Double.parseDouble(this.LowIntegralLim.getText());
				 h = Double.parseDouble(this.HighIntegralLim.getText());
				}catch(Exception x){
			     l = 0.00;
			     h = 5.00;
			     this.LowIntegralLim.setText("0.00");
			     this.HighIntegralLim.setText("5.00");
				}
				try{
				 this.IntegralResult.setText(this.graph.calcIntegral(l,h));
				}catch(Exception x){
				 this.IntegralResult.setText("0.000");
				}
				
			 }

		}
}
