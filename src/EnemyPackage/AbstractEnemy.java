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

public abstract class AbstractEnemy {

    private DrawOptions option = new DrawOptions();
    private static final int THRESHOLD = 1;
    private Vector2 vPos;
    private int index;
    private double angle, speed;

    //Part 2
    private int penalty, health;
    private double defaultSpeed;

    public AbstractEnemy(Point point, int timescale, int index){
        vPos = new Vector2(point.x, point.y);
        angle = atan2(point.y, point.x);
        this.index = index;
    }

    //Getters and Setters
    public int getIndex(){
        return index;
    }
    public Point getPoint(){
        return vPos.asPoint();
    }
    public int getHealth(){
        return health;
    }

    //Don't actually need any of the details aside from speed in here
    protected void setAttributes(double defaultSpeed, int timescale, int penalty, int health){
        this.defaultSpeed = defaultSpeed;
        speed = defaultSpeed*timescale;
        this.penalty = penalty;
        this.health = health;
    }

    //Updates position of each enemy
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

    //Updates speed of each enemy
    public void changeSpeed(int timescale){
        speed = defaultSpeed * timescale;
    }

    //Used for drawing the enemy
    protected void drawImage(Image enemy){
        enemy.draw(vPos.x, vPos.y, option.setRotation(angle));
    }

    public void hit(int damage){
        health -= damage;
    }

    public void enemyPenalty(){
        PlayerData.getInstance().loseLife(penalty);
    }
    public abstract void drawImage();
    public abstract void enemyDeath(ListIterator enemyList);
    public abstract Rectangle getBounds();
}
