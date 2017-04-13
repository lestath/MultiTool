package MyUtils;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * Klasa przechowująca zestaw punktów dla pojedynczego wykresu oraz wzory funkcji w postaci ciagu znakow
 *
 */
public class GraphPoints{
	private ArrayList<Point> points;
	private ArrayList<Point> polarpoints;
	private String pattern;
	private CoorSys system;
	private Color color;
	
	public GraphPoints(String pat,CoorSys system,Color c){
		this.points = new ArrayList<Point>();
		this.polarpoints = new ArrayList<Point>();
		this.pattern = pat;
		this.system = system;
		this.color = c;
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

	public CoorSys getSystem() {
		return system;
	}

	public void setSystem(CoorSys system) {
		this.system = system;
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
	
}
