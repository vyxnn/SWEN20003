package TowerPackage;

import bagel.Image;
import bagel.util.Point;

public class SuperTank extends AbstractTank{
    private Image superTankImage, superProjImage;
    private final int radius = 150, cooldown = 500, damage = 3;

    public SuperTank(Point point){
        super(point);
        superTankImage = new Image("res/images/supertank.png");
        superProjImage = new Image("res/images/supertank_projectile.png");
        super.setAttributes(superTankImage, superProjImage, radius, cooldown, damage);
    }

}
