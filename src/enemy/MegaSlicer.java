package enemy;
import player.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ListIterator;

/**
 * MegaSlicer class
 * Inherits from AbstractEnemy
 */
public class MegaSlicer extends AbstractEnemy {
    private static final int DEATHSPAWN = 2;
    private Image megaSlicerImage;
    private double defaultSpeed = 1.5;
    private int health = 2, reward = 10, penalty = 4;

    /**
     * Constructor for a MegaSlicer
     * @param point spawns a MegaSlicer at a point
     * @param index spawns at an index
     */
    public MegaSlicer(Point point, int index) {
        super(point, index);
        setAttributes(defaultSpeed, penalty, health);
        megaSlicerImage = new Image("res/images/megaslicer.png");
    }

    /**
     * Draws a MegaSlicer
     */
    @Override
    public void drawImage() {
        super.drawImage(megaSlicerImage);
    }

    /**
     * Removes itself from a list of enemy and spawns new slicers
     * Rewards player
     * @param enemyList list of enemies
     */
    @Override
    public void enemyDeath(ListIterator enemyList) {
        enemyList.remove();
        //Spawns required number of slicers upon death
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new SuperSlicer(super.getPoint(), super.getIndex()));
        }
        //Gives money
        PlayerData.getInstance().addMoney(reward);
    }

    /**
     * Returns bounds of MegaSlicer
     * @return bounds as Rectangle
     */
    @Override
    public Rectangle getBounds() {
        return megaSlicerImage.getBoundingBoxAt(super.getPoint());
    }

}
