/**
 * This is an example of the basic "Bouncing Ball" animation, making
 * use of the Model-View-Controller design pattern and the Timer and
 * Observer/Observable classes.
 *
 * Edited by Qi He to set up the kaleidoscope
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The Controller sets up the GUI and handles all the controls (buttons,
 * menu items, etc.)
 * 
 * @author David Matuszek, modified by swapneel
 */
public class Controller {
	private JFrame frame;
    private JPanel buttonPanel;
    private JButton runButton;
    private JButton stopButton;
    private JButton reflectionButton;

    private JButton colorButton;
    private JButton restartButton;
    private JSlider speedSlider;

    private ArrayList<Model> modelCollection;

    private Random rn = new Random();
    
    /** The View object displays what is happening in the Model. */
    private View view;
    
    /**
     * Runs the bouncing ball/kaleidoscope program.
     * @param args Ignored.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Controller c = new Controller();
                c.init();
                c.display();
            }
        });
        
    }

    /**
     * Sets up communication between the components.
     */
    private void init() {
        /* The Model is the object that does all the computations. It is
      completely independent of the Controller and View objects. */
        Model ballModel = new Model();
        Model ballModelTwo = new Model();

        // Set rectangle model
        Model rectangleModel = new Model();
        rectangleModel.setShape(1);

        Model crossModel = new Model();
        crossModel.setShape(3);

        // Set square model
        Model squareModel = new Model();
        squareModel.setShape(2);

        Model squareModelTwo = new Model();
        squareModelTwo.setShape(2);

        // Make sure colors are all different
        ballModel.setColor();
        ballModelTwo.offsetColorByOne(ballModel.getColor());
        rectangleModel.offsetColorByOne(ballModelTwo.getColor());
        crossModel.offsetColorByOne(rectangleModel.getColor());
        squareModel.offsetColorByOne(crossModel.getColor());
        squareModelTwo.offsetColorByOne(squareModel.getColor());

        modelCollection = new ArrayList<>();

        modelCollection.add(ballModel);
        modelCollection.add(rectangleModel);
        modelCollection.add(squareModel);
        modelCollection.add(ballModelTwo);
        modelCollection.add(crossModel);
        modelCollection.add(squareModelTwo);

        view = new View(modelCollection);  // The view needs to know what ballModel to look at

        for (Model model:modelCollection){
            model.addObserver(view); // The model needs to give permission to be observed
        }

    }

    /**
     * Displays the GUI.
     */
    private void display() {
        layOutComponents();
        attachListenersToComponents();
        frame.setSize(800, 800);
        frame.setVisible(true);

        // Randomly change the location of x
        for (Model model:modelCollection){
            model.setX(rn.nextInt(view.getWidth() / 2));
            model.setY(rn.nextInt(view.getHeight() / 2));
        }
    }
    
    /**
     * Arranges the various components in the GUI.
     */
    private void layOutComponents() {
    	frame = new JFrame("Kaleidoscope");
    	buttonPanel = new JPanel();

        //Set em buttons
        runButton = new JButton("Run");
        stopButton = new JButton("Stop");
        colorButton = new JButton("Change Color");
        reflectionButton = new JButton("Change Reflection");
        restartButton = new JButton("Clear/Restart");

        // Slider stuff per official doc
        int min = 0;
        int max = 100;
        int mid = 50;

        speedSlider = new JSlider(JSlider.HORIZONTAL, min, max, mid);
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setPaintTicks(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(mid, new JLabel("Speed") );
        speedSlider.setLabelTable(labelTable);
        speedSlider.setPaintLabels(true);

        buttonPanel.add(runButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(colorButton);
        buttonPanel.add(reflectionButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(speedSlider);

        stopButton.setEnabled(false);
        
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(BorderLayout.SOUTH, buttonPanel);
        frame.add(BorderLayout.CENTER, view);

    }
    
    /**
     * Attaches listeners to the components, and schedules a Timer.
     */
    private void attachListenersToComponents() {
        // The Run button tells the Model to start
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                runButton.setEnabled(false);
                stopButton.setEnabled(true);

                modelCollection.forEach(Model::start);
            }
        });
        // The Stop button tells the Model to pause
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                runButton.setEnabled(true);
                stopButton.setEnabled(false);

                modelCollection.forEach(Model::pause);
            }
        });
        // When the window is resized, the Model is given the new limits
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {

                for (Model model:modelCollection){
                    model.setLimits(view.getWidth(), view.getHeight());
                }
            }
        });

        // When slider changes values
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int speed = (int)source.getValue();

                    for (Model model:modelCollection){
                        model.setSpeed(speed);
                    }
                }
            }
        });

        //When color button is pressed
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                modelCollection.forEach(Model::setColor);

            }
        });

        // Reset
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Not sure how to delete other things other than these...
                frame.remove(buttonPanel);
                frame.dispose();
                System.gc();
                init();
                display();

            }
        });

        // Change reflection
        reflectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                modelCollection.forEach(Model::setReflections);

            }
        });


    }



}