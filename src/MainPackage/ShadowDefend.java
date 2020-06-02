package MainPackage;
import bagel.*;
import bagel.util.Colour;
import PlayerPackage.*;
import bagel.util.Point;

/**
 *
 * @param <map>
 */
public class ShadowDefend <map> extends AbstractGame {
    //
    public static final int TOSECONDS = 1000;
    public static final int FPS = 60;
    //Definitions for all the panel placements
    private final static int TIMESCALE_X = 260;
    private final static int STATUS_PANEL_Y = 752;
    private final static int WAVE_X = 10;
    private final static int LIFE_X = 860;
    private final static int STATUS_X = 510;
    private final static int KEY_BIND_X = 500;
    private final static int KEY_BIND_Y = 20;
    private final static int S_BIND_Y = 45;
    private final static int L_BIND_Y = 60;
    private final static int K_BIND_Y = 75;
    private final static int BUY_OFFSET = 10;
    private final static int TOWER_Y = 90;
    private final static int TOWER_X = 32;
    /**
     * Public final attributes to be accessed from multiple classes
     * Chosen to be public so it is only required to be changed in one place rather than redefining
     */
    //Item placements
    public final static int ITEM_OFFSET = 64;
    public final static int ITEM_GAP = 120;
    //Tank prices
    public final static int TANKPRICE = 250;
    public final static int SUPERTANKPRICE = 600;
    public final static int AIRPLANEPRICE = 500;
    //Keywords for the status of placing
    public final static String TANK = "tank";
    public final static String SUPERTANK = "supertank";
    public final static String AIRPLANE = "airplane";
    public final static String FALSE = "false";
    public final static String AWAITING = "Awaiting Start";
    //Objects and variables
    private final Image buyPanel;
    private final Image statusPanel;
    private final Image tankImage, superTankImage, airImage;
    private final Font statusFont, moneyFont, keyBindFont, towerFont;
    private DrawOptions option = new DrawOptions();
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
        level.updateLevel();

        //Checks if there is an item being placed, and renders it where the mouse is
        if (!level.getTowerProgress().equals(false)) {
            if(checkMousePos(input)) {
                level.drawTowerView(input);
            }
        }

        //Starts a wave if one is not in progress
        if (input.wasPressed(Keys.S) && level.getWaveProgress().equals("Awaiting Start")) {
            level.startWave();
        }

        //Input functions
        //Increasing/Decreasing global timescale
        if(input.wasPressed(Keys.L)) {
            PlayerData.getInstance().increaseTimescale();
        }

        if(input.wasPressed(Keys.K)) {
            PlayerData.getInstance().decreaseTimescale();
        }
        //If left mouse button was pressed, and there is a tower being placed, will place the item
        if (input.wasPressed(MouseButtons.LEFT) && !level.getTowerProgress().equals(FALSE)) {
           if(checkMousePos(input)) {
                level.placeTower(input);
            }
        }

        //If left mouse button was pressed, and there is no current tower being placed, create new tower to place
        if (input.wasPressed(MouseButtons.LEFT) && level.getTowerProgress().equals(FALSE)) {
            level.checkMouse(input);
        }

        //If right mouse button was pressed and there a tower being place, undoes the view
        if (input.wasPressed(MouseButtons.RIGHT) && !level.getTowerProgress().equals(FALSE)) {
            level.setTowerProgress(FALSE);
        }

        //Ending the wave/window
        if (input.isDown(Keys.ESCAPE)) {
            Window.close();
        }

    }

    //Draws the status panel and its features
    private void drawStatusPanel() {
        statusPanel.drawFromTopLeft(level.getMap().getWidth() - statusPanel.getWidth(),
                level.getMap().getHeight() - statusPanel.getHeight());

        //Timescale, default if 1, green otherwise
        if(PlayerData.getInstance().getTimescale() == 1) {
            statusFont.drawString("Timescale: " + PlayerData.getInstance().getTimescale(),
                    TIMESCALE_X, STATUS_PANEL_Y);
        }
        else {
            statusFont.drawString("Timescale: " + PlayerData.getInstance().getTimescale(),
                    TIMESCALE_X, STATUS_PANEL_Y, option.setBlendColour(Colour.GREEN));
        }
        //Other status data
        statusFont.drawString("Wave: " + level.getWaveNum(), WAVE_X, STATUS_PANEL_Y);
        statusFont.drawString("Status: " + level.getWaveProgress(), STATUS_X, STATUS_PANEL_Y);
        statusFont.drawString("Lives: " + PlayerData.getInstance().getLife(), LIFE_X, STATUS_PANEL_Y);
    }

    //Draws the buy panel and it's features
    private void drawBuyPanel() {
        buyPanel.drawFromTopLeft(0,0);
        //Drawing money
        moneyFont.drawString("$" + PlayerData.getInstance().getMoney(),level.getMap().getWidth()-200,65);
        keyBindFont.drawString("Key binds:", KEY_BIND_X, KEY_BIND_Y);
        keyBindFont.drawString("S - Start Wave:", KEY_BIND_X, S_BIND_Y);
        keyBindFont.drawString("L - Increase Timescale:", KEY_BIND_X, L_BIND_Y);
        keyBindFont.drawString("K - Decrease Timescale:", KEY_BIND_X, K_BIND_Y);
        //Drawing towers and prices
        double mid = (buyPanel.getHeight()/2) - BUY_OFFSET;
        tankImage.draw(ITEM_OFFSET, mid);
        superTankImage.draw(ITEM_GAP+ ITEM_OFFSET, mid);
        airImage.draw(ITEM_GAP + ITEM_GAP+ ITEM_OFFSET, mid);
        drawTowerCost();
    }

    //Draws the costs of each tower and marks it red or green depending on the funds
    private void drawTowerCost() {
        int currentMoney = PlayerData.getInstance().getMoney();
        if ( currentMoney >= TANKPRICE) {
            towerFont.drawString("$" + TANKPRICE, TOWER_X, TOWER_Y, option.setBlendColour(Colour.GREEN));
        } else {
            towerFont.drawString("$" + TANKPRICE, TOWER_X, TOWER_Y, option.setBlendColour(Colour.RED));
        }

        if ( currentMoney >= SUPERTANKPRICE) {
            towerFont.drawString("$" + SUPERTANKPRICE, TOWER_X + ITEM_GAP, TOWER_Y,
                    option.setBlendColour(Colour.GREEN));
        } else {
            towerFont.drawString("$" + SUPERTANKPRICE, TOWER_X + ITEM_GAP, TOWER_Y,
                    option.setBlendColour(Colour.RED));
        }

        if ( currentMoney >= AIRPLANEPRICE) {
            towerFont.drawString("$" + AIRPLANEPRICE, TOWER_X + ITEM_GAP + ITEM_GAP,
                    TOWER_Y, option.setBlendColour(Colour.GREEN));
        } else {
            towerFont.drawString("$" + AIRPLANEPRICE, TOWER_X + ITEM_GAP + ITEM_GAP,
                    TOWER_Y, option.setBlendColour(Colour.RED));
        }

    }

    //Only places/renders towers if the mouse position is within the map
    private boolean checkMousePos(Input input){
        Point mousePos = input.getMousePosition();
        if (mousePos.y > level.getMap().getHeight() || mousePos.y < 0 ||
                mousePos.x < 0 || mousePos.x > level.getMap().getWidth()) {
            return false;
        }
        return true;
    }
}
