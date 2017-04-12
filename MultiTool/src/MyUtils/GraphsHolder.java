package MyUtils;

import java.util.ArrayList;

import javax.swing.JComboBox;

/**
 * 
 * Klasa przetrzymująca zestawy wykresów w postaci list punktów
 *
 */
public class GraphsHolder {
	private ArrayList<GraphPoints> graphlist; 
	
	public GraphsHolder(){
			setGraphlist(new ArrayList<GraphPoints>());
	}

	public ArrayList<GraphPoints> getGraphlist() {
		return graphlist;
	}

	public void setGraphlist(ArrayList<GraphPoints> graphlist) {
		this.graphlist = graphlist;
	}
	
	public void clearAll(){
		for(GraphPoints p:graphlist){
			p.clearAll();
		}
		this.graphlist.clear();
	}
}
