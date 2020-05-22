package TowerPackage;

import bagel.Image;
import bagel.map.TiledMap;
import bagel.util.Point;

public abstract class AbstractTank {
    private int radius, cooldown, damage, time;
    private Point pPos;
    private Image image;

    public AbstractTank(Point point) {
        pPos = point;
    }

    protected void setAttributes(Image image){
        this.image = image;
    }

    public void renderIndicator(TiledMap map, Point mousePos){
        
    };

}
