import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;
import java.util.List;

public class ShadowDefend <map> extends AbstractGame {

    //UPDATE SPEED HERE
    public static final int INITIALTIMESCALE = 1;

    //Objects and variables
    private final Image buyPanel;
    private final Image statusPanel;
    private final Font font;
    DrawOptions option = new DrawOptions();
    private Level level;

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
        buyPanel = new Image("res/images/buypanel.png");
        statusPanel = new Image("res/images/statuspanel.png");
        font = new Font("res/fonts/DejaVuSans-Bold.ttf", 10);

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
        drawStatusPanel();
        buyPanel.drawFromTopLeft(0,0);

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

    /* Drawing the Status Panel, sorry for the random numbers
    they were found by trial and error and defining them would make the code messier than just leaving them*/
    public void drawStatusPanel() {

        statusPanel.drawFromTopLeft(level.getMap().getWidth() - statusPanel.getWidth(),
                level.getMap().getHeight() - statusPanel.getHeight());
        font.drawString("Wave: " + level.getWaveNum(), 10, 752);

        if(level.getTimescale() == 1) {
            font.drawString("Timescale: " + level.getTimescale(), 260, 752);
        }

        else {
            font.drawString("Timescale: " + level.getTimescale(), 260, 752,
                    option.setBlendColour(Colour.GREEN));
        }

        font.drawString("Status: " + level.getWaveProgress(), 510, 752);

        //Need to draw lives
        font.drawString("Lives: " , 860, 752);
    }
}
