package EnemyPackage;
import PlayerPackage.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ListIterator;

public class SuperSlicer extends AbstractEnemy {
    private static final int DEATHSPAWN = 2;
    private Image superSlicerImage;
    private double defaultSpeed = 1.5;
    private int health = 1, reward = 15, penalty = 2;

    //Constructor for slicer
    public SuperSlicer(Point point, int timescale, int index) {
        super(point, timescale, index);
        setAttributes(defaultSpeed, timescale, penalty, health);
        superSlicerImage = new Image("res/images/superslicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(superSlicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList) {
        enemyList.remove();
        //Give money
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new Slicer(super.getPoint(), PlayerData.getInstance().getTimescale(), super.getIndex()));
        }

        PlayerData.getInstance().addMoney(reward);
    }

    @Override
    public Rectangle getBounds() {
        return superSlicerImage.getBoundingBoxAt(super.getPoint());
    }


}
