package EnemyPackage;
import PlayerPackage.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.ListIterator;

public class MegaSlicer extends AbstractEnemy {
    private static final int DEATHSPAWN = 2;
    private Image megaSlicerImage;
    private double defaultSpeed = 1.5;
    private int health = 2, reward = 10, penalty = 4;

    //Constructor for slicer
    public MegaSlicer(Point point, int timescale, int index) {
        super(point, timescale, index);
        setAttributes(defaultSpeed, timescale, penalty, health);
        megaSlicerImage = new Image("res/images/megaslicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(megaSlicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList) {
        enemyList.remove();
        //Spawns required number of slicers upon death
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new SuperSlicer(super.getPoint(), PlayerData.getInstance().getTimescale(), super.getIndex()));
        }
        //Gives money
        PlayerData.getInstance().addMoney(reward);
    }

    @Override
    public Rectangle getBounds() {
        return megaSlicerImage.getBoundingBoxAt(super.getPoint());
    }

}
