package Ant;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Dimension;

public class PanelRight extends JPanel {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 20L;
	public JButton AddButton;
	 public JButton AddButton2;
	 
	 public PanelRight(){
		//inicjalizacja ustawień panelu
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.setPreferredSize(new Dimension(150,500));
		
		Dimension buttonsize = new Dimension(140,24);
		
		// przyciski inicjalizacja
		this.AddButton = new JButton("Add New Ant");
		this.AddButton2 = new JButton("Add New Ant2");

		
		// ustawienie rozmiarów elementów 
		AddButton.setPreferredSize(buttonsize);
		AddButton2.setPreferredSize(buttonsize);
		
		// ustawienia estetyczne fokus
		AddButton.setFocusPainted(false);
		AddButton2.setFocusPainted(false);
		
		// dodanie przycisków
		add(AddButton);
		add(AddButton2);
		this.setVisible(true);
		
	 }

}
