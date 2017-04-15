package MyUtils;

import java.util.Comparator;

/**
 * 
 * Klasa reprezentująca punkt
 *
 */
public class Point implements Comparator<Point>{
	private double X;
	private double Y;
	
	public Point(double x, double y) {
		this.X = x;
		this.Y= y;
	}
	
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}

	@Override
	public int compare(Point o1, Point o2) {
	       double p1 = o1.getX();
	       double p2 = o2.getX();

	       if (p1 > p2) {
	           return 1;
	       } else if (p1 < p2){
	           return -1;
	       } else {
	           return 0;
	       }
	}
}
