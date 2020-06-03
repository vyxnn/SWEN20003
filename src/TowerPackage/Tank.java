package TowerPackage;

import bagel.Image;
import bagel.util.Point;

/**
 * Tank Class
 * Shoots at an enemy and does damage
 */
public class Tank extends AbstractTank{

    private Image tankImage, tankProjImage;
    private final static int radius = 100, cooldown = 1000, damage = 1;

    /**
     * Constructor for a tank
     * @param point creates a tank at a point
     */
    public Tank(Point point){
        super(point);
        tankImage = new Image("res/images/tank.png");
        tankProjImage = new Image("res/images/tank_projectile.png");
        super.setAttributes(tankImage, tankProjImage, radius, cooldown, damage);
    }


}
