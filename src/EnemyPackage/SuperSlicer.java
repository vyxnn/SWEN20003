package EnemyPackage;
import PlayerPackage.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ListIterator;

/**
 * SuperSlicer class
 * Inherits for AbstractEnemy
 */
public class SuperSlicer extends AbstractEnemy {
    private static final int DEATHSPAWN = 2;
    private Image superSlicerImage;
    private double defaultSpeed = 1.5;
    private int health = 1, reward = 15, penalty = 2;

    /**
     * Constructor for SuperSlicer
     * @param point spawns a SuperSlicer at point
     * @param index spawns a SuperSlicer at index
     */
    public SuperSlicer(Point point, int index) {
        super(point, index);
        setAttributes(defaultSpeed, penalty, health);
        superSlicerImage = new Image("res/images/superslicer.png");
    }

    /**
     * Draws a SuperSlicer
     */
    @Override
    public void drawImage() {
        super.drawImage(superSlicerImage);
    }

    /**
     * Removes itself from the enemyList, and spawns more slicers
     * Rewards player
     * @param enemyList list of enemies
     */
    @Override
    public void enemyDeath(ListIterator enemyList) {
        enemyList.remove();
        //Give money
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new Slicer(super.getPoint(), super.getIndex()));
        }

        PlayerData.getInstance().addMoney(reward);
    }

    /**
     * Returns the bounds of a SuperSlicer
     * @return bounds as Rectangle
     */
    @Override
    public Rectangle getBounds() {
        return superSlicerImage.getBoundingBoxAt(super.getPoint());
    }


}
