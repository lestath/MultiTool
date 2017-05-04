package MyUtils;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

/**
 * 
 * Klasa przechowująca zestaw punktów dla pojedynczego wykresu oraz wzory funkcji w postaci ciagu znakow
 *
 */
public class GraphPoints{
	private boolean alreadyCalculated; //flaga ustawia się na true jeżeli nastapiło wyliczenie punktów
	private ArrayList<Point> points;
	private ArrayList<Point> polarpoints;
	private ArrayList<Point> fromfilepoints;
	private File source; //źródło grafu z pliku
	private String pattern;
	private Color color;
	
	private int method; //metoda rysowania z punktów
	
	public GraphPoints(String pat,Color c,int method){
		
		this.points = new ArrayList<Point>();
		this.polarpoints = new ArrayList<Point>();
		this.pattern = pat;
		this.color = c;
		this.alreadyCalculated = false;
		this.setMethod(method);
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void deletePoints(){
		if(this.points!= null){
			this.points.clear();
		}
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void clearAll() {
		this.points.clear();
		this.polarpoints.clear();
	}

	public ArrayList<Point> getPolarpoints() {
		return polarpoints;
	}

	public void setPolarpoints(ArrayList<Point> polarpoints) {
		this.polarpoints = polarpoints;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public boolean isAlreadyCalculated() {
		return alreadyCalculated;
	}

	public void setAlreadyCalculated(boolean alreadyCalculated) {
		this.alreadyCalculated = alreadyCalculated;
	}

	public ArrayList<Point> getFromfilepoints() {
		return fromfilepoints;
	}

	public void setFromfilepoints(ArrayList<Point> fromfilepoints) {
		this.fromfilepoints = fromfilepoints;
	}

	
	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}
	
}
