import bagel.AbstractGame;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.util.List;

public class ShadowDefend <map> extends AbstractGame {
    //Constants
    private static final int SLICERMAX = 5; //Number of slicers per wave
    private static final int FIVESEC = 300; //5secs*60frames = 300 fps
    private static final int MINSPEED = 1; //Timescale multiplier can't go under 1
    //UPDATE SPEED HERE
    private static final int INITIALSPEED = 1;

    //Objects and variables
    private final TiledMap map;
    private final Image slicerImage;
    private final Image buyPanel;
    private final Image statusPanel;
    private Slicer[] slicerArr = new Slicer[SLICERMAX]; //This array can be modified to contain different enemies
    private final List path;
    private int time;
    private int globalSpeed = INITIALSPEED;

    /**
     * Entry point for Bagel game
     * <p>
     * Explore the capabilities of Bagel: https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/
     */
    public static void main(String[] args) {
        // Create new instance of game and run it
        new ShadowDefend<>().run();
    }

    /**
     * Setup the game
     */
    public ShadowDefend() {
        // Constructor
        map = new TiledMap("res/levels/1.tmx");
        path = map.getAllPolylines().get(0);
        slicerImage = new Image("res/levels/images/slicer.png");
        buyPanel = newImage()
    }

    /**
     * Updates the game state approximately 60 times a second, potentially reading from input.
     *
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    //Slicers move at a rate of 1fps
    @Override
    protected void update(Input input) {
        map.draw(0, 0, 0, 0, Window.getWidth(), Window.getHeight());

        //Starts a timer for a wave and spawns the first slicer if no wave has been started
        if (input.wasPressed(Keys.S) && slicerArr[0] == null) {
            newWave();
        }

        /*Checks if a wave has started, then will spawn a new enemy every 5 seconds (or equivalent based off speed)
          Continues until it has reached the max slicers */
        if((slicerArr[0]!=null) && (time >= FIVESEC*INITIALSPEED/globalSpeed)
                && (slicerArr[0].count < SLICERMAX)){
            spawnEnemy();
        }

        /*If the first slicer exists, then will start moving them*/
        if(slicerArr[0] != null) {
            time++;
            updateWave();
        }

        //Input functions
        /*Checks if speed is valid, then increase/decreases in slicer and
        keeps track of global speed in case there are no slicers spawned yet*/
        if(input.wasPressed(Keys.L)) {
            globalSpeed += 1;
            increaseSpeed();
        }

        if(input.wasPressed(Keys.K)) {
            if(globalSpeed > MINSPEED){
                globalSpeed -= 1;
                decreaseSpeed();
            }
        }

        //Ending the wave/window
        if (input.isDown(Keys.ESCAPE) || endWave()) {
            Window.close();
        }

    }

    //For the purpose of this assignment will assume that there's only one wave of a slicer at a time
    //Therefore can use the static count with slicer, otherwise needs to reset to 0 for other waves/maps

    /*Initializes the wave and spawns first slicer with regular speed*/
    public void newWave(){
        time = 0;
        slicerArr[0] = new Slicer((Point) path.get(0), globalSpeed);
    }

    /*Spawns the rest of the enemies until max, resets timer
      Spawns enemy at first point with current speed of the first slicer*/
    public void spawnEnemy() {
        slicerArr[slicerArr[0].count] = new Slicer((Point) path.get(0), globalSpeed);
        time = 0;
    }

    /*Updates the entire wave, increments time */
    public void updateWave() {
        //Iterates over the array of slicers, and moves/draws if a slicer exists
        for (Slicer slicer: slicerArr) {
            if (slicer == null) {
                break;
            }
            updateSlicer(slicer);
            slicer.draw(slicerImage);
        }
    }

    /*Updates the position of each slicer passed in*/
    public void updateSlicer(Slicer slicer) {
        //Checks if a slicer has a valid index and updates path for one frame if it does
        if(slicer.getIndex() + 1 < path.size()) {
            slicer.updatePos((Point) path.get(slicer.getIndex() + 1));
        }
    }

    /*Checks position of final slicer, if it's reached the final index then end game*/
    public boolean endWave() {
        Slicer lastSlicer = slicerArr[SLICERMAX -1];
        if(lastSlicer != null && (lastSlicer.getIndex() + 1 >= path.size())){
            return true;
        }
        return false;
    }

    /*Decreases or increases all existing enemy speeds*/
    public void increaseSpeed() {
        for (Slicer slicer: slicerArr) {
            if (slicer == null) {
                break;
            }
            slicer.increaseSpeed();
        }
    }

    public void decreaseSpeed() {
        for (Slicer slicer: slicerArr) {
            if (slicer == null) {
                break;
            }
            slicer.decreaseSpeed();
        }
    }
}
