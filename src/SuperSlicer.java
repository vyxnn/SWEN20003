import bagel.Image;
import bagel.util.Point;

import java.util.ListIterator;

public class SuperSlicer extends AbstractEnemy {
    private static final int DEATHSPAWN = 2;
    private Image superSlicerImage;
    private double defaultSpeed = 1.5;
    private int health = 1, reward = 15, penalty = 2;

    //Constructor for slicer
    public SuperSlicer(Point point, int timescale) {
        super(point, timescale);
        setAttributes(defaultSpeed, timescale, health, reward, penalty);
        superSlicerImage = new Image("res/images/superslicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(superSlicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList, int timescale) {
        //Give money
        for(int i = 0; i < DEATHSPAWN; i++) {
            enemyList.add(new Slicer(super.getPoint(), timescale));
        }

        PlayerData.getInstance().addMoney(reward);
    }

    @Override
    public void enemyPenalty(){
        PlayerData.getInstance().loseLife(penalty);
    }


}
