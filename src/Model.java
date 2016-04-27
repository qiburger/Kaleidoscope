

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 * This is the Model class for a bouncing ball/other shapes. It is an Observable,
 * which means that it can notifyObservers that something in the
 * model has changed, and they should take appropriate actions.
 * 
 * @author David Matuszek, modified by swapneel
 */
public class Model extends Observable {

    // Randomize size here - other randomization is done in Controller
    // because they require other information such as view's size
    // or other models' colors
    Random rn = new Random();
    public final int Object_SIZE = rn.nextInt(30) + 30;
    private int xPosition = 0;
    private int yPosition = 0;
    private int xLimit, yLimit;
    private int xDelta = 12;
    private int yDelta = 8;
    private Timer timer;
    private Color color = Color.BLUE;

    // 0 for ball, 1 for rectangle, 2 for square, 3 for cross
    private int shape = 0;
    private int totalReflections = 16;

	/**
     * Sets the "walls" that the ball should bounce off from.
     * 
     * @param xLimit The position (in pixels) of the wall on the right.
     * @param yLimit The position (in pixels) of the floor.
     */
    public void setLimits(int xLimit, int yLimit) {
        this.xLimit = xLimit - Object_SIZE;
        this.yLimit = yLimit - Object_SIZE;
//        xPosition = Math.min(xPosition, xLimit);
//        yPosition = Math.min(yPosition, yLimit);

        // If we don't do this, the graph will get stuck when we resize
        xPosition = Math.max(xPosition, 0);
        xPosition = Math.min(xPosition, this.xLimit);

        yPosition = Math.max(yPosition, 0);
        yPosition = Math.min(yPosition, this.yLimit);

    }

    /**
     * @return The balls X position.
     */
    public int getX() {
        return xPosition;
    }

    /**
     * @return The balls Y position.
     */
    public int getY() {
        return yPosition;
    }

    /**
     * Setter for x - helps set x randomly
     * @param xLocation location of x
     */
    public void setX(int xLocation){
        this.xPosition = xLocation;

    }

    /**
     * Setter for y
     * @param yLocation position of y
     */
    public void setY(int yLocation){
        this.yPosition = yLocation;

    }
    
    /**
     * Tells the ball to start moving. This is done by starting a Timer
     * that periodically executes a TimerTask. The TimerTask then tells
     * the ball to make one "step."
     */
    public void start() {
        timer = new Timer(20, new Strobe());
        timer.start();
    }
    
    /**
     * Tells the ball to stop where it is.
     */
    public void pause() {
        timer.stop();
    }

    /**
     * Set the speed - this is technically the % of speed level
     * Max speed here is 50
     * @param speedLevel int speed level from 1 - 100
     */
    public void setSpeed(int speedLevel){
        int maxSpeed = 20;
        xDelta = maxSpeed * speedLevel / 100;
        yDelta = maxSpeed * speedLevel / 100;
    }
    
    /**
     * Tells the ball to advance one step in the direction that it is moving.
     * If it hits a wall, its direction of movement changes.
     */
    public void makeOneStep() {
        // Do the work
        xPosition += xDelta;
        if (xPosition < 0 || xPosition >= xLimit) {
            xDelta = -xDelta;
            xPosition += xDelta;
        }
        yPosition += yDelta;
        if (yPosition < 0 || yPosition >= yLimit) {
            yDelta = -yDelta;
            yPosition += yDelta;
        }


        // Notify observers
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for totalReflections
     * @return total number of totalReflections
     */
    public int getTotalReflections() {
        return totalReflections;
    }

    /**
     * Setter for totalReflections - again just a toggle
     */
    public void setReflections() {
        totalReflections += 8;
        if (totalReflections > 24){
            totalReflections = 8;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Tells the model to advance one "step."
     */
    private class Strobe implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						makeOneStep();
						return null;
					}
			};
			worker.execute();
		}
    }

    /**
     * Getter for color
     * @return color for this model
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for color.
     * This setter just automatically toggles from red to green to blue
     */
    public void setColor(){
        if(this.color.equals(Color.BLUE)) this.color = Color.RED;
        else if(this.color.equals(Color.RED)) this.color = Color.GREEN;
        else if(this.color.equals(Color.GREEN)) this.color = Color.CYAN;
        else if (this.color.equals(Color.CYAN)) this.color = Color.PINK;
        else if (this.color.equals(Color.PINK)) this.color = Color.YELLOW;
        else if (this.color.equals(Color.YELLOW)) this.color = Color.BLUE;

        setChanged();
        notifyObservers();
    }

    /**
     * Helper function to help us set up obejcts to have different colors
     * @param input color to offset based on
     */
    public void offsetColorByOne(Color input){
        this.color = input;
        setColor();
    }

    /**
     * Getter for shape
     * @return 0 for ball, 1 for rectangle, 2 for square
     */
    public int getShape(){
        return shape;
    }


    /**
     * Setter for shape
     * @param i # indicating the shape
     */
    public void setShape(int i){
        this.shape = i % 4;

    }

    /**
     * This is a helper function to get the parameter necessary for View to
     * draw every reflection; Note that the kaleidoscope is effectively
     * symmetric about a point! So now I only need to change the coordinates/origin.
     * Thankfully we don't need to do matrix manipulation for these 2d objects
     *
     * @param reflection the # of reflection View is trying to draw
     * @param centerX new origin's current x coordinate
     * @param centerY new origin's current y coordinate
     * @return list of parameters for drawing
     */
    public ArrayList<Integer> getParameters(int reflection, int centerX, int centerY){
        ArrayList<Integer> output = new ArrayList<>();

        int reflectedX = getParametersHelper(reflection, xPosition, yPosition, centerX, centerY, 0);
        int reflectedY = getParametersHelper(reflection, xPosition, yPosition, centerX, centerY, 1);

        output.add(reflectedX);
        output.add(reflectedY);
        output.add(Object_SIZE);

        if (shape == 0 || shape == 2){
            output.add(Object_SIZE);
        }else if (shape == 1){
            output.add(Object_SIZE/2);
        }else{
            output.add(Object_SIZE/15);
        }

        return output;
    }

    /**
     * This is the helper function that helps get the reflection's location
     * Basically we try to change everything back to setting the center as (0, 0)
     * Do to sin / cos, and then revert back to new coordinates
     * @param reflection  the # of reflection View is trying to draw
     * @param oldX original x value
     * @param oldY original y value
     * @param centerX new origin's current x coordinate
     * @param centerY new origin's current y coordinate
     * @param xOrY 0 for reflected x value, 1 for y
     * @return reflected x value or y value
     */
    private int getParametersHelper(int reflection, int oldX, int oldY, int centerX, int centerY, int xOrY){

        int newX = oldX - centerX;
        int newY = oldY - centerY;

        // Use arc tangent to find the angle
        // Note the positive quadrant is bottom right
        double angle = Math.atan((double) newX / newY);
//

        // Length of vector pointing from new origin to (x, y)
        double vectorLength = Math.sqrt(newX*newX + newY*newY);

        double delta = 2 * Math.PI / totalReflections;

        newX = (int) (vectorLength * Math.sin(angle + reflection * delta));
        newY = (int) (vectorLength * Math.cos(angle + reflection * delta));

        // Revert back
        if (xOrY == 0){
            return newX + centerX;
        }else{
            return newY + centerY;
        }

    }


}