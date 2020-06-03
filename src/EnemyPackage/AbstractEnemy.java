/*Assumption that there is more than one type of enemy
 *Currently the majority of slicer operations seem generic enough to be included in an Enemy Class
 *If my assumptions are wrong and there is only one enemy for the whole game, then can easily move everything to Slicer
 */
package EnemyPackage;
import PlayerPackage.*;
import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.ListIterator;
import static java.lang.Math.atan2;

/**
 * Abstract class for AbstractEnemy
 * Used for moving enemies and checking if they're alive or not
 */
public abstract class AbstractEnemy {

    private DrawOptions option = new DrawOptions();
    private static final int THRESHOLD = 1;
    private Vector2 vPos;
    private int index, penalty, health;
    private double angle, speed, defaultSpeed;

    /**
     * Constructor for AbstractEnemy
     * @param point Point where enemy is
     * @param index Index of path where enemy is
     */
    public AbstractEnemy(Point point, int index){
        vPos = new Vector2(point.x, point.y);
        angle = atan2(point.y, point.x);
        this.index = index;
    }

    //Getters and Setters

    /**
     * Returns the index (of path)
     * @return index as int
     */
    public int getIndex(){
        return index;
    }

    /**
     * Returns the position of an enemy
     * @return position as Point
     */
    public Point getPoint(){
        return vPos.asPoint();
    }

    /**
     * Returns health of enemy
     * @return health as int
     */
    public int getHealth(){
        return health;
    }

    /**
     * Sets the attributes of an enemy from a child class
     * @param defaultSpeed default speed of a slicer
     * @param penalty penalty of a slicer
     * @param health initial health of a slicer
     */
    protected void setAttributes(double defaultSpeed, int penalty, int health){
        this.defaultSpeed = defaultSpeed;
        speed = defaultSpeed*PlayerData.getInstance().getTimescale();
        this.penalty = penalty;
        this.health = health;
    }

    /**
     * Updates position of a slicer
     * @param towards given a point in a path to move towards
     */
    public void updatePos(Point towards){
        Vector2 vTow = new Vector2(towards.x, towards.y);
        //Original vector is calculated from origin, subtracting changes it to be relevant to specific position
        vTow = vTow.sub(vPos);
        //Updates the angle it should face, and normalises the vector to travel at 1px per frame
        angle = atan2(vTow.y, vTow.x);
        vTow = vTow.normalised();
        //Changes how far it will move based on current speed, then updates position
        vTow = vTow.mul(speed);
        vPos = vPos.add(vTow);
        /*Checks if point has been reached, and increments index to next point if it has
          As long as the positions are within 1px*currentSpeed then will move to next point*/
        if((Math.abs(vPos.x - towards.x) < speed*THRESHOLD) && (Math.abs(vPos.y - towards.y) < speed*THRESHOLD) ) {
            index++;
        }
    }

    /**
     * Changes speed of an enemy
     */
    public void changeSpeed(){
        speed = defaultSpeed * PlayerData.getInstance().getTimescale();
    }

    //Used for drawing the enemy
    protected void drawImage(Image enemy){
        enemy.draw(vPos.x, vPos.y, option.setRotation(angle));
    }

    /**
     * Does damage to an enemy
     * @param damage from a projectile or explosive
     */
    public void hit(int damage){
        health -= damage;
    }

    /**
     * Penalises the player if the enemy reaches the end of the map without death
     */
    public void enemyPenalty(){
        PlayerData.getInstance().loseLife(penalty);
    }

    /**
     * Draws enemy image
     */
    public abstract void drawImage();

    /**
     * Death of an enemy
     * @param enemyList list of enemies
     */
    public abstract void enemyDeath(ListIterator enemyList);

    /**
     * Returns bound of enemy
     * @return enemy bounds as rectangle
     */
    public abstract Rectangle getBounds();
}
