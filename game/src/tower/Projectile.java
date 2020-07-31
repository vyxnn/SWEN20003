package tower;

import enemy.AbstractEnemy;
import main.ShadowDefend;
import player.PlayerData;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * Projectile class
 * Does damage to a single enemy on collision
 */
public class Projectile {

    private static int SPEED = 10;
    private Vector2 vPos;
    private int damage;
    private Image image;
    private AbstractEnemy enemy;
    private String status;

    /**
     * Constructor for a projectile
     * @param image projectile image is passed in from a tank class
     * @param damage damage from tank class
     * @param pos position from tank class
     * @param enemy given an AbstractEnemy to target
     */
    public Projectile(Image image, int damage, Point pos, AbstractEnemy enemy){
        status = ShadowDefend.ACTIVE;
        vPos = pos.asVector();
        this.image = image;
        this.damage = damage;
        this.enemy = enemy;
    }

    /**
     * Updates the position of a projectile
     * Checks if it has collided with its enemy and does damage accordingly
     */
    public void updateProjectile(){
        //If enemy is already killed, then projectile will be removed
        if(enemy == null) {
            status = ShadowDefend.DORMANT;
            return;
        }
        //Otherwise updates to target enemy
        Vector2 vTow = enemy.getVPos();
        vTow = vTow.sub(vPos);
        vTow = vTow.normalised();
        //Multiplies by speed then timescale
        vTow = vTow.mul(SPEED*PlayerData.getInstance().getTimescale());
        vPos = vPos.add(vTow);
        drawProjectile();

        //Checks if the projectile point is within the bounds of the enemy
        //Modified to check if point is within bounds rather than both the bounding boxes for realism
        //Hits enemy and sets projectile to "dormant"
        if(enemy.getBounds().intersects(vPos.asPoint())) {
            enemy.hit(damage);
            status = ShadowDefend.DORMANT;
        }
    }

    //Draws the projectile
    private void drawProjectile(){
        image.draw(vPos.x, vPos.y);
    }

    /**
     * Checks if a projectile is dormant or active
     * @return status as String
     */
    public String getStatus(){
        return status;
    }
}
