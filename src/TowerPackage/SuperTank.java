package TowerPackage;

import bagel.Image;
import bagel.util.Point;

public class SuperTank extends AbstractTank{
    private Image superTankImage, superProjImage;
    private final static int radius = 150, cooldown = 500, damage = 3;
    private Point pos;
    public SuperTank(Point point){
        super(point);
        pos = point;
        superTankImage = new Image("res/images/supertank.png");
        superProjImage = new Image("res/images/supertank_projectile.png");
        super.setAttributes(superTankImage, superProjImage, radius, cooldown, damage);
    }

}
