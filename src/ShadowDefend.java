import bagel.*;
import bagel.util.Colour;
import PlayerPackage.*;

public class ShadowDefend <map> extends AbstractGame {

    //UPDATE SPEED HERE
    public static final int INITIALTIMESCALE = 1;

    //Objects and variables
    private final Image buyPanel, statusPanel;
    private final Image tankImage, superTankImage, airImage;
    private final Font statusFont, moneyFont, keyBindFont, towerFont;
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
        statusFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 10);
        moneyFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 48);
        keyBindFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 14);
        towerFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 20);
        //UNSURE IF I NEED TO DRAW THEM FROM HERE OR MIGHT BE ABLE TO DRAW FROM CALLING THE CLASS
        tankImage = new Image("res/images/tank.png");
        superTankImage = new Image("res/images/supertank.png");
        airImage = new Image("res/images/airsupport.png");
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
        drawBuyPanel();

        //Starts a wave if one is not in progress
        if (input.wasPressed(Keys.S) && level.getWaveProgress().equals("Awaiting Start")) {
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
    private void drawStatusPanel() {
        statusPanel.drawFromTopLeft(level.getMap().getWidth() - statusPanel.getWidth(),
                level.getMap().getHeight() - statusPanel.getHeight());

        //Timescale, default if 1, green otherwise
        if(level.getTimescale() == 1) {
            statusFont.drawString("Timescale: " + level.getTimescale(), 260, 752);
        }
        else {
            statusFont.drawString("Timescale: " + level.getTimescale(), 260, 752,
                    option.setBlendColour(Colour.GREEN));
        }
        //Other status data
        statusFont.drawString("Wave: " + level.getWaveNum(), 10, 752);
        statusFont.drawString("Status: " + level.getWaveProgress(), 510, 752);
        statusFont.drawString("Lives: " + PlayerData.getInstance().getLife(), 860, 752);
    }

    private void drawBuyPanel() {
        buyPanel.drawFromTopLeft(0,0);
        //Drawing money
        moneyFont.drawString("$" + PlayerData.getInstance().getMoney(),level.getMap().getWidth()-200,65);
        //Drawing key binds, again using random numbers I'm very sorry
        keyBindFont.drawString("Key binds:", 500, 20);
        keyBindFont.drawString("S - Start Wave:", 500, 45);
        keyBindFont.drawString("L - Increase Timescale:", 500, 60);
        keyBindFont.drawString("K - Decrease Timescale:", 500, 75);
        //Drawing towers and prices
        //CURRENTLY NUMBERS FOR NOW BUT CAN CHANGE TO GET PRICE ONCE I ADD THE CLASSES
        double mid = (buyPanel.getHeight()/2) - 10;
        tankImage.draw(64, mid);
        superTankImage.draw(184, mid);
        airImage.draw(304, mid);
        //EITHER DEFINE THESE NUMBERS OR GET IT FROM CLASS
        drawTowerCost();
    }

    private void drawTowerCost() {
        int currentMoney = PlayerData.getInstance().getMoney();
        if ( currentMoney >= 250) {
            towerFont.drawString("$250", 32, 90, option.setBlendColour(Colour.GREEN));
        } else {
            towerFont.drawString("$250", 32, 90, option.setBlendColour(Colour.RED));
        }

        if ( currentMoney >= 600) {
            towerFont.drawString("$600", 152, 90, option.setBlendColour(Colour.GREEN));
        } else {
            towerFont.drawString("$600", 152, 90, option.setBlendColour(Colour.RED));
        }

        if ( currentMoney >= 500) {
            towerFont.drawString("$500", 272, 90, option.setBlendColour(Colour.GREEN));
        } else {
            towerFont.drawString("$500", 272, 90, option.setBlendColour(Colour.RED));
        }

    }
}
