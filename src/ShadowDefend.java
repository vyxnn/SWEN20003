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
    public static final int INITIALTIMESCALE = 1;

    //Objects and variables
    private final Image buyPanel;
    private final Image statusPanel;
    private Slicer[] slicerArr = new Slicer[SLICERMAX]; //This array can be modified to contain different enemies
    private final List path;
    private Level level;
    private int time;
    private int globalSpeed = INITIALTIMESCALE;

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
        level = new Level(1);
        path = level.getPath();
        buyPanel = new Image("res/images/buypanel.png");
        statusPanel = new Image("res/images/statuspanel.png");

    }

    /**
     * Updates the game state approximately 60 times a second, potentially reading from input.
     *
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    //Slicers move at a rate of 1fps
    @Override
    protected void update(Input input) {
        //Drawing maps and panels
        level.drawMap();
        buyPanel.drawFromTopLeft(0,0);
        statusPanel.drawFromTopLeft(level.getMap().getWidth() - statusPanel.getWidth(),
                level.getMap().getHeight() - statusPanel.getHeight());

        //Starts a timer for a wave and spawns the first slicer if no wave has been started
        if (input.wasPressed(Keys.S)) {
            level.startWave();
        }

        level.updateLevel();
        //Input functions
        /*Checks if speed is valid, then increase/decreases in slicer and
        keeps track of global speed in case there are no slicers spawned yet*/
        if(input.wasPressed(Keys.L)) {
            level.increaseTimescale();
        }

        if(input.wasPressed(Keys.K)) {
            level.decreaseTimescale();
        }

        //Ending the wave/window
        if (input.isDown(Keys.ESCAPE)) {
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
            slicer.drawImage();
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

}
