import bagel.Image;
import bagel.util.Point;

import java.util.Iterator;
import java.util.ListIterator;

public class ApexSlicer extends AbstractEnemy{
    private static final int DEATHSPAWN = 4;
    private Image apexSlicerImage;
    private double defaultSpeed = 0.75;
    private int health = 25, reward = 150, penalty = 16;

    //Constructor for slicer
    public ApexSlicer(Point point, int timescale) {
        super(point, timescale);
        setAttributes(defaultSpeed, timescale, penalty, health);
        apexSlicerImage = new Image("res/images/apexslicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(apexSlicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList, int timescale) {
        //Give money
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new MegaSlicer(super.getPoint(), timescale));
        }

        PlayerData.getInstance().addMoney(reward);
    }

}
