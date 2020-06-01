package TowerPackage;

import bagel.Image;
import bagel.util.Point;

public class Tank extends AbstractTank{

    private Image tankImage, tankProjImage;
    private final static int radius = 100, cooldown = 1000, damage = 1;
    private Point pos;

    public Tank(Point point){
        super(point);
        pos = point;
        tankImage = new Image("res/images/tank.png");
        tankProjImage = new Image("res/images/tank_projectile.png");
        super.setAttributes(tankImage, radius, cooldown, damage);
    }

    @Override
    public void drawTank() {
        tankImage.draw(pos.x, pos.y);
    }


}
