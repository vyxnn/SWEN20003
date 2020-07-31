package main;
import enemy.*;
import player.*;
import tower.*;
import bagel.Image;
import bagel.Input;
import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Handles the towers for each level
 */
public class TowerHandler {
    private String placing;
    private final static String BLOCKED = "blocked";
    //Can't get this from the class as it hasn't been created yet
    private Image tankImage = new Image("res/images/tank.png");
    private Image superTankImage = new Image("res/images/supertank.png");
    private Image airImage = new Image("res/images/airsupport.png");
    private ArrayList<AbstractTank> tankList = new ArrayList<>();
    private ArrayList<Airplane> airplaneList = new ArrayList<>();

    /**
     * Constructor for the TowerHandler, mainly checks its status
     */
    public TowerHandler(){
        placing = ShadowDefend.FALSE;
    }

    //TOWER METHODS
    /**
     * Either renders an image of the tower, or places a tower
     * @param map Uses map to check the bounds
     * @param input Uses input to check if a tower can be placed where the cursor is
     * @param command Checks if a tower is being placed or view is being rendered
     */
    public void placeTower(TiledMap map, Input input, String command){
        Point mousePos = input.getMousePosition();
        //Checks that the mouse is not in a blocked tile, on a panel, and doesn't intersect with a tower
        if (!map.hasProperty((int)mousePos.x, (int)mousePos.y, BLOCKED) && mousePos.y > Level.BUYHEIGHT
                && mousePos.y < (map.getHeight()-Level.STATUSHEIGHT - ShadowDefend.ITEM_OFFSET/2)
                && !towerIntersect(mousePos)){
            //Either places or renders view depending on the string command passed in
            if (placing.equals(ShadowDefend.TANK)) {
                if(command.equals(ShadowDefend.PLACING)) {
                    //Adds tower to the list, and takes money from Player
                    tankList.add(new Tank(mousePos));
                    PlayerData.getInstance().loseMoney(ShadowDefend.TANKPRICE);
                    placing = ShadowDefend.FALSE;
                }
                else {
                    tankImage.draw(mousePos.x, mousePos.y);
                }
            } else if (placing.equals(ShadowDefend.SUPERTANK)) {
                if(command.equals(ShadowDefend.PLACING)) {
                    tankList.add(new SuperTank(mousePos));
                    PlayerData.getInstance().loseMoney(ShadowDefend.SUPERTANKPRICE);
                    placing = ShadowDefend.FALSE;
                }
                else {
                    superTankImage.draw(mousePos.x, mousePos.y);
                }
            } else if (placing.equals(ShadowDefend.AIRPLANE)) {
                if(command.equals(ShadowDefend.PLACING)) {
                    airplaneList.add(new Airplane(mousePos));
                    PlayerData.getInstance().loseMoney(ShadowDefend.AIRPLANEPRICE);
                    placing = ShadowDefend.FALSE;
                }
                else{
                    airImage.draw(mousePos.x, mousePos.y);
                }
            }
        }
    }

    /**
     * Draws the tower and updates the time (cooldown) for each frame
     */
    public void drawTower(){
        for(AbstractTank t: tankList){
            if (t == null) {
                break;
            }
            //Draws tower and updates time
            t.drawTank();
            t.updateTime();
        }
    }

    /**
     * Locates the first enemy in the list and targets if it is within the radius
     * @param enemyList Checks the list of enemies for a target
     */
    public void updateTankList(ArrayList<AbstractEnemy> enemyList){
        //Updates the tanks
        for(AbstractTank t: tankList){
            if (t == null) {
                break;
            }
            //From these calculations should get a rectangle with tank in the centre
            Rectangle tankRange = new Rectangle(t.getPos().x - t.getRadius()/2, t.getPos().y - t.getRadius()/2,
                    t.getRadius(), t.getRadius());
            //If the enemy intersects with a tank, will become new target if it can shoot
            for(AbstractEnemy e: enemyList) {
                if(e.getBounds().intersects(tankRange)){
                    t.updateTank(e);
                    break;
                }
            }
        }
    }

    /**
     * Updates the airplanes and their explosives
     * @param enemyList Checks enemies to see if they are within the radius of an explosive
     * @param map Uses map to remove once all explosives have detonated and airplane is out of map
     */
    public void updateAirplaneList(ArrayList<AbstractEnemy> enemyList, TiledMap map){
        //Updates airplanes
        ListIterator<Airplane> itr = airplaneList.listIterator();
        while (itr.hasNext()) {
            //Iterates over each airplane and updates it
            Airplane a = itr.next();
            Point position = a.getPos().asPoint();
            a.updateAirplane(enemyList);
            //If airplane is off map, stops dropping explosives
            if(position.x > map.getWidth() && position.y > map.getHeight()){
                a.removeAirplane();
            }
            //Once all the explosives have detonated and the airplane is off the map, will remove
            if(a.getExplosiveList().isEmpty() && position.x > map.getWidth() && position.y > map.getHeight()) {
                itr.remove();
            }
        }
    }

    //Returns true if mouse pos intersects with a tower
    private boolean towerIntersect(Point pos){
        for(AbstractTank t: tankList) {
            //Intersection
            if (t.getBounds().intersects(pos)) {
                return true;
            }
        }
        //Otherwise no intersection
        return false;
    }

    //GETTERS AND SETTERS
    /**
     * Called from ShadowDefend class if a tower is being placed
     * Updates its status to "Placing" instead of "False"
     */
    public void setPlacing(String placing){
        this.placing = placing;
    }

    /**
     * Returns the current status of the towers i.e if one is being placed or not
     * @return status of towers in a string
     */
    public String getPlacing(){
        return placing;
    }
}
