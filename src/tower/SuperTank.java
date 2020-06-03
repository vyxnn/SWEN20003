package tower;

import bagel.Image;
import bagel.util.Point;

/**
 * SuperTank Class
 * Shoots at an enemy within its radius and does damage
 */
public class SuperTank extends AbstractTank{
    private Image superTankImage, superProjImage;
    private final int radius = 150, cooldown = 500, damage = 3;

    /**
     * Constructor for a SuperTank
     * @param point creates a tank where the mouse is
     */
    public SuperTank(Point point){
        super(point);
        superTankImage = new Image("res/images/supertank.png");
        superProjImage = new Image("res/images/supertank_projectile.png");
        super.setAttributes(superTankImage, superProjImage, radius, cooldown, damage);
    }

}
