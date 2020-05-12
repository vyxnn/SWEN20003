/*Assumption that there is more than one type of enemy
 *Currently the majority of slicer operations seem generic enough to be included in an Enemy Class
 *If my assumptions are wrong and there is only one enemy for the whole game, then can easily move everything to Slicer
 */

import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import static java.lang.Math.atan2;

abstract class Enemy {

    DrawOptions option = new DrawOptions();
    private static final int THRESHOLD = 1;

    private Vector2 vPos;
    private int speed, index;
    private double angle;

    public Enemy(Point point, int speed){
        vPos = new Vector2(point.x, point.y);
        angle = atan2(point.y, point.x);
        this.speed = speed;
        index = 0;
    }

    public int getIndex(){
        return index;
    }


    public void draw(Image enemy){
        enemy.draw(vPos.x, vPos.y, option.setRotation(angle));
    }

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

    /*Will change current speed by 1*/
    public void increaseSpeed(){
        speed += 1;
    }

    public void decreaseSpeed(){
        speed -= 1;
    }
}
