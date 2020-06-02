package EnemyPackage;
import PlayerPackage.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ListIterator;

public class Slicer extends AbstractEnemy {
    private Image slicerImage;
    private double defaultSpeed = 2;
    private int health = 1, reward = 2, penalty = 1;

    //Constructor for slicer
    public Slicer(Point point, int timescale, int index) {
        super(point, timescale, index);
        setAttributes(defaultSpeed, timescale, penalty, health);
        slicerImage = new Image("res/images/slicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(slicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList) {
        PlayerData.getInstance().addMoney(reward);
        enemyList.remove();
    }

    @Override
    public Rectangle getBounds() {
        return slicerImage.getBoundingBoxAt(super.getPoint());
    }

}
