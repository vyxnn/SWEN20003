package TowerPackage;

import bagel.Image;
import bagel.map.TiledMap;
import bagel.util.Point;

public abstract class AbstractTank {
    private int radius, cooldown, damage, time;
    private Point pPos;
    private Image tankImage;

    public AbstractTank(Point point) {
        pPos = point;
    }

    protected void setAttributes(Image image, int radius, int cooldown, int damage){
        tankImage = image;
        this.radius = radius;
        this.cooldown = cooldown;
        this.damage = damage;
    }

    public abstract void drawTank();

}
