package MyGraph;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klasa reprezentująca górne menu
 */
public class TopBar extends JMenuBar implements ActionListener {


	private JMenu filemenu;
		private JMenuItem filemenuimport_interpolation;
		private JMenuItem filemenuimport_approximation;
		private JMenuItem filemenuimport_points;
		private JMenuItem filemenuimport_normal;
	private JMenu helpmenu;
		private JMenuItem helpmenu_program;
		private JMenuItem helpmenu_about;
		
	private GraphPanel graph;
	
	private static final long serialVersionUID = 1L;
	public TopBar(){

		//menu_file
		filemenu = new JMenu("File");
			// podelementy
			this.filemenuimport_interpolation =new JMenuItem("import from file and interpolate");
			this.filemenuimport_approximation =new JMenuItem("import from file and approximate");
			this.filemenuimport_points =new JMenuItem("import from file and generate points");
			this.filemenuimport_normal =new JMenuItem("import from file and generate normal mode");
			
			//eventy
			this.filemenuimport_interpolation.addActionListener(this);
			this.filemenuimport_approximation.addActionListener(this);
			this.filemenuimport_points.addActionListener(this);
			this.filemenuimport_normal.addActionListener(this);
			
			//dodawanie
			this.filemenu.add(this.filemenuimport_interpolation);
			this.filemenu.add(this.filemenuimport_approximation);
			this.filemenu.add(this.filemenuimport_points);
			this.filemenu.add(this.filemenuimport_normal);
		//menu_help
		this.helpmenu = new JMenu("Help");
		   //podelementy
		   this.helpmenu_program = new JMenuItem("Program");
		   this.helpmenu_about = new JMenuItem("About");
		   //eventy
		   this.helpmenu_about.addActionListener(this);
		   this.helpmenu_program.addActionListener(this);
		   //dodawanie
		   this.helpmenu.add(this.helpmenu_program);
		   this.helpmenu.add(this.helpmenu_about);
		   
		   
			
		this.add(this.filemenu);
		this.add(this.helpmenu);
	}
	
	/**
	 * Metoda wiążąca z panelem graficznym (przydatna w delegacjach)
	 * @param graph
	 */
	public void addGraph(GraphPanel graph){
		this.graph = graph;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
			 if(source == this.helpmenu_program){
				 //TODO zaprojektować okno pomocy, które zostanie uruchomione tutaj
				 System.out.println("pomoc");
			 }if(source == this.helpmenu_about){
				 //TODO zaprojektować okno o mnie, które zostanie uruchomione tutaj
				 System.out.println("o programie");
			 }else{
				 //oknowyboru pliku
				 JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");
				    chooser.setFileFilter(filter);
				    int returnVal = chooser.showOpenDialog(this);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				            if(this.graph!=null){
				            	if(source == this.filemenuimport_approximation){
				            		this.graph.generateFromFile(chooser.getSelectedFile(),this.graph.APPROXIMATION);
				            	}else if(source == this.filemenuimport_interpolation){
				            		this.graph.generateFromFile(chooser.getSelectedFile(),this.graph.INTERPOLATION);
				            	}else if(source == this.filemenuimport_points){
				            		this.graph.generateFromFile(chooser.getSelectedFile(),this.graph.POINTS);
				            	}else if(source == this.filemenuimport_normal){
				            		this.graph.generateFromFile(chooser.getSelectedFile(),this.graph.NORMAL_MODE);
				            	}
				            }
				    }
			 }
		
	}

}
