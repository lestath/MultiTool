import java.awt.Dimension;
import java.awt.EventQueue;

import MyGraph.PopupLabel;
import MyGraph.PopupWindow;


public class PopupTest {
	public static void main(String[] args) {
		 EventQueue.invokeLater(
				   new Runnable(){
					    @Override
					    public void run(){
					    	PopupLabel pl = new PopupLabel("resources/aboutfile");
							new PopupWindow(pl,"Multitool2.0 - about",new Dimension(300,300));
						}
					   }
				  );
	}
}
