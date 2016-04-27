

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * The View "observes" and displays what is going on in the Model.
 * In this example, the Model contains only a single bouncing ball.
 * 
 * @author David Matuszek, modified by swapneel
 */
public class View extends JPanel implements Observer {

    private ArrayList<Model> modelArrayList;

    /**
     * Constructor.
     * @param modelArrayList The Arraylist of Model whose working is to be displayed.
     */
    public View(ArrayList<Model> modelArrayList){
        this.modelArrayList = modelArrayList;
    }

    /**
     * Displays what is going on in the Model. Note: This method should
     * NEVER be called directly; call repaint() instead.
     * 
     * @param g The Graphics on which to paint things.
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
//    	System.out.println(SwingUtilities.isEventDispatchThread());
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Model model : modelArrayList){
            g.setColor(model.getColor());
            /* This is what we will be observing. */
            int totalObjects = model.getTotalReflections();
            for (int i = 0; i < totalObjects; i++){
                ArrayList<Integer> parameters = model.getParameters(i, getWidth() / 2, getHeight() / 2);
                if (model.getShape() == 0){
                    g.fillOval(parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
                }else if (model.getShape() == 3){
                    int size = model.Object_SIZE;
                    g.fillRect(parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
                    g.fillRect(parameters.get(0) + (size/2), parameters.get(1) - (size/2), parameters.get(3), parameters.get(2));
                }else{
                    g.fillRect(parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
                }
            }
        }

    }

    /**
     * When an Observer notifies Observers (and this View is an Observer),
     * this is the method that gets called.
     * 
     * @param obs Holds a reference to the object being observed.
     * @param arg If notifyObservers is given a parameter, it is received here.
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable obs, Object arg) {
//    	System.out.println(SwingUtilities.isEventDispatchThread());
        repaint();
    }
}