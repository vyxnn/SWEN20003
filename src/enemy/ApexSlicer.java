package enemy;
import player.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ListIterator;

/**
 * ApexSlicer class
 * Inherits from AbstractEnemy
 */
public class ApexSlicer extends AbstractEnemy{
    private static final int DEATHSPAWN = 4;
    private Image apexSlicerImage;
    private double defaultSpeed = 0.75;
    private int health = 25, reward = 150, penalty = 16;

    /**
     * Constructor for an ApexSlicer
     * @param point spawns at a given point
     * @param index spawns at a given index
     */
    public ApexSlicer(Point point, int index) {
        super(point, index);
        setAttributes(defaultSpeed, penalty, health);
        apexSlicerImage = new Image("res/images/apexslicer.png");
    }

    /**
     * Draws image of a slicer
     */
    @Override
    public void drawImage() {
        super.drawImage(apexSlicerImage);
    }

    /**
     * Removes itself from the enemyList, spawns more slicers at its point of death
     * Rewards player
     * @param enemyList list of enemies
     */
    @Override
    public void enemyDeath(ListIterator enemyList) {
        enemyList.remove();
        //Give money
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new MegaSlicer(super.getPoint(), super.getIndex()));
        }
        PlayerData.getInstance().addMoney(reward);
    }

    /**
     * Returns bound of ApexSlicer
     * @return bounds as Rectangle
     */
    @Override
    public Rectangle getBounds() {
        return apexSlicerImage.getBoundingBoxAt(super.getPoint());
    }

}
