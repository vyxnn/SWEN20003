import bagel.Image;
import bagel.util.Point;

import java.util.Iterator;
import java.util.ListIterator;

public class Slicer extends AbstractEnemy {

    private Image slicerImage;
    private double defaultSpeed = 2;
    private int health = 1, reward = 2, penalty = 1;

    //Constructor for slicer
    public Slicer(Point point, int timescale) {
        super(point, timescale);
        setAttributes(defaultSpeed, timescale, health, reward, penalty);
        slicerImage = new Image("res/images/slicer.png");
    }

    //Draws slicer with image
    @Override
    public void drawImage() {
        super.drawImage(slicerImage);
    }

    @Override
    public void enemyDeath(ListIterator enemyList, int timescale ){
        //Gives money
        PlayerData.getInstance().addMoney(reward);
    }

    @Override
    public void enemyPenalty(){
        PlayerData.getInstance().loseLife(penalty);
    }

}
