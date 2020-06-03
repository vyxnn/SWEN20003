package tower;

import enemy.*;
import player.PlayerData;
import main.*;
import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Airplane class
 * Flies in a straight line (horizontal or vertical) across the map
 * Drops explosives
 */
public class Airplane {
    private static final int SPEED = 5;
    private static final int LAUNCH = 4; //0-3 seconds inclusive
    private int time, cooldown, airNo;
    private String waiting, status;
    private static int count=0;
    private Vector2 vPos;
    private Point pPos;
    private DrawOptions option = new DrawOptions();
    private Image airplaneImage;
    private ArrayList<Explosive> explosiveList = new ArrayList();

    /**
     * Constructor for the airplane
     * @param point Saves the point where it was placed
     */
    public Airplane(Point point){
        status = ShadowDefend.ACTIVE;
        waiting = ShadowDefend.TRUE;
        pPos = point;
        count++;
        airNo = count;
        time = 0;
        cooldown = 0;
        airplaneImage = new Image("res/images/airsupport.png");
        spawnAirplane();
    }

    //Spawns airplane just outside the map taking the coordinate from where it was placed
    private void spawnAirplane(){
        //Vertical if even, takes the y coordinate from where it was placed
        if(airNo%2 == 0) {
            vPos = new Vector2( 0, pPos.y);
        }
        //Horizontal if odd
        else {
            vPos = new Vector2(pPos.x , 0);
        }
    }

    //Draws the airplane
    private void drawAirplane(){
        //Rotates vertically
        if(airNo%2 == 0) {
            airplaneImage.draw(vPos.x, vPos.y, option.setRotation(Math.PI/2));
        }
        //Rotates horizontally
        else {
            airplaneImage.draw(vPos.x, vPos.y, option.setRotation(Math.PI));
        }
    }

    /**
     * Updates the location of the airplane, and drops an explosive if the cooldown is over
     * Also updates explosive
     * @param enemyList checks if an enemy is near an explosive
     */
    public void updateAirplane(ArrayList<AbstractEnemy> enemyList){
        //Updates time
        time += PlayerData.getInstance().getTimescale();
        updatePos();
        drawAirplane();
        //Check if there's an active cooldown, if not assigns a new one
        if (waiting.equals(ShadowDefend.TRUE)) {
            Random random = new Random();
            cooldown = random.nextInt(LAUNCH);
            waiting = ShadowDefend.FALSE;
        }
        //Adds explosive if cooldown is over, resets cooldown
        if(time >= cooldown*ShadowDefend.FPS && waiting.equals(ShadowDefend.FALSE)
                && status.equals(ShadowDefend.ACTIVE)) {
            explosiveList.add(new Explosive(vPos.asPoint()));
            cooldown = 0;
            time = 0;
            waiting = ShadowDefend.TRUE;
        }
        //Iterates over explosive list and updates time or explodes
        ListIterator<Explosive> itr = explosiveList.listIterator();
        while (itr.hasNext()) {
            Explosive exp = itr.next();
            exp.updateExplosive(itr, enemyList);
        }
    }

    //Updates the position of the airplane
    private void updatePos(){
        //Adds the unit vector in the direction of X
        if(airNo%2 == 0) {
            Vector2 unitX = new Vector2(1,0);
            unitX = unitX.mul(SPEED*PlayerData.getInstance().getTimescale());
            vPos = vPos.add(unitX);
        }
        //Adds the unit vector in the direction of Y
        else {
            Vector2 unitY = new Vector2(0,1);
            unitY = unitY.mul(SPEED*PlayerData.getInstance().getTimescale());
            vPos = vPos.add(unitY);
        }
    }

    //GETTERS

    /**
     * Returns the position of the airplane as a vector
     * @return position as Vector2
     */
    public Vector2 getPos(){
        return vPos;
    }

    /**
     * Returns the list of active explosives
     * @return ArrayList of explosives
     */
    public ArrayList<Explosive> getExplosiveList(){
        return explosiveList;
    }

    /**
     * Stops airplane from dropping bombs
     */
    public void removeAirplane(){
        status = ShadowDefend.DORMANT;
    }
}
