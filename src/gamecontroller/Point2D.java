package gamecontroller;

import java.util.Locale;
import java.util.Objects;

public class Point2D {
	
	private  double x;
	private  double y;

	/**
	 * Create a new {@link Point2D} with the given double coordinates.
	 *
	 * @param x x-coordinate of the point
	 * @param y y-coordinate of the point
	 */
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Point2D)) {
			return false;
		}
		Point2D other = (Point2D) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(%.2f, %.2f)", x, y);
	}
}
