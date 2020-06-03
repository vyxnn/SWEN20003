package enemy;
import player.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ListIterator;

/**
 * Slicer class
 * Inherits from AbstractEnemy
 */
public class Slicer extends AbstractEnemy {
    private Image slicerImage;
    private double defaultSpeed = 2;
    private int health = 1, reward = 2, penalty = 1;

    /**
     * Constructor for a Slicer
     * @param point spawns a slicer at point
     * @param index spawns a slicer at index
     */
    public Slicer(Point point, int index) {
        super(point, index);
        setAttributes(defaultSpeed, penalty, health);
        slicerImage = new Image("res/images/slicer.png");
    }

    /**
     * Draws an image of a slicer
     */
    @Override
    public void drawImage() {
        super.drawImage(slicerImage);
    }

    /**
     * Removes slicer from enemyList
     * Rewards player
     * @param enemyList list of enemies
     */
    @Override
    public void enemyDeath(ListIterator enemyList) {
        PlayerData.getInstance().addMoney(reward);
        enemyList.remove();
    }

    /**
     * Returns bounds of a slicer
     * @return bounds as rectangle
     */
    @Override
    public Rectangle getBounds() {
        return slicerImage.getBoundingBoxAt(super.getPoint());
    }

}
