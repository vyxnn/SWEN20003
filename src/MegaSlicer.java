import bagel.Image;
import bagel.util.Point;

import java.util.ListIterator;

public class MegaSlicer extends AbstractEnemy {
    private static final int DEATHSPAWN = 2;
    private Image megaSlicerImage;
    private double defaultSpeed = 1.5;
    private int health = 2, reward = 10, penalty = 4;

    //Constructor for slicer
    public MegaSlicer(Point point, int timescale) {
        super(point, timescale);
        setAttributes(defaultSpeed, timescale, health, reward, penalty);
        megaSlicerImage = new Image("res/images/megaslicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(megaSlicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList, int timescale) {
        //Spawns required number of slicers upon death
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new SuperSlicer(super.getPoint(), timescale));
        }
        //Gives money
        PlayerData.getInstance().addMoney(reward);

    }

    @Override
    public void enemyPenalty(){
        PlayerData.getInstance().loseLife(penalty);
    }
}
