package GameUI;

/**
 * The Coord class is used to represent the coordinates of a window pixel.
 * All the coordinates used to represent the graphics used in this game are
 * represented by this class.
 * <p> Its attributes are public for easy access.
 * <p> Once a Coord is created, its attributes values cannot be changed.
 * @author Miguel Oliveira
 */
public class Coord {
	
	/**
	 * Represents the distance of the pixel to the left side of the window
	 */
	public final int x;
	
	/**
	 * Represents the distance of the pixel to the top of the window
	 */
	public final int y;
	
	/**
	 * Constructs a new Coord using the values received as it's coordinates
	 * @param x distance to the left side of window
	 * @param y distance to the top of window
	 */
	public Coord (int x, int y) {
		this.x = x;
		this.y = y;
	}
}
