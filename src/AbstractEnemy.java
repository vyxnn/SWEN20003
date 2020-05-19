/*Assumption that there is more than one type of enemy
 *Currently the majority of slicer operations seem generic enough to be included in an Enemy Class
 *If my assumptions are wrong and there is only one enemy for the whole game, then can easily move everything to Slicer
 */

import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Iterator;
import java.util.ListIterator;

import static java.lang.Math.atan2;

abstract class AbstractEnemy {

    DrawOptions option = new DrawOptions();
    private static final int THRESHOLD = 1;
    private Vector2 vPos;
    private int index;
    private double angle, speed;

    //Part 2
    private int health, reward, penalty;
    private double defaultSpeed;

    public AbstractEnemy(Point point, int timescale){
        vPos = new Vector2(point.x, point.y);
        angle = atan2(point.y, point.x);
        index = 0;
    }

    //Getters and Setters
    public int getIndex(){
        return index;
    }
    public int getHealth(){
        return health;
    }
    public Point getPoint(){
        return vPos.asPoint();
    }

    protected void setAttributes(double defaultSpeed, int timescale, int health, int reward, int penalty){
        this.defaultSpeed = defaultSpeed;
        speed = defaultSpeed*timescale;
        this.health = health;
        this.reward = reward;
        this.penalty = penalty;
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

    public abstract void drawImage();
    public abstract void enemyDeath(ListIterator enemyList, int timescale);
    public abstract void enemyPenalty();
}
