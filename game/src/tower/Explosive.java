package tower;
import enemy.*;
import main.*;
import player.PlayerData;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Explosive class
 * Will detonate after a set amount of time, AOE damage to surrounding enemies
 */
public class Explosive {
    private final int radius = 150, cooldown = 2000, damage = 500;
    private int time;
    private Image projectileImage;
    private Point pPos;

    /**
     * Constructor for an explosive
     * Starts timer
     * @param point where the explosive is dropped
     */
    public Explosive(Point point){
        pPos = point;
        time = 0;
    }

    /**
     * Updates the explosive and detonates if necessary
     * @param explosiveList a list of explosives for a single airplane
     * @param enemyList a list of active enemies, will calculate the ones within its radius
     */
    public void updateExplosive(ListIterator explosiveList, ArrayList<AbstractEnemy> enemyList){
        time += PlayerData.getInstance().getTimescale();
        drawExplosive();
        //If it's time for the explosive to explode, will take out surrounding enemies and remove itself
        if(time >= cooldown*ShadowDefend.FPS/ShadowDefend.TOSECONDS) {
            if(enemyList != null) {
                explode(enemyList);
            }
            explosiveList.remove();
        }
    }

    //Calculates if surrounding enemies are within it's radius
    private void explode(ArrayList<AbstractEnemy> enemyList){
        Rectangle explosiveRange = new Rectangle(pPos.x - radius/2, pPos.y - radius/2, radius, radius);
        for(AbstractEnemy e: enemyList) {
            if(e.getBounds().intersects(explosiveRange)){
                e.hit(damage);
            }
        }
    }

    /**
     * Draws the projectile explosive
     */
    public void drawExplosive(){
        projectileImage = new Image("res/images/explosive.png");
        projectileImage.draw(pPos.x, pPos.y);
    }
}
