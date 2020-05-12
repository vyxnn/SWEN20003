import bagel.Image;
import bagel.util.Point;

public class Slicer extends Enemy {

    public static int count=0;
    private Image slicerImage;
    private int defaultSpeed = 1;

    //Constructor for slicer
    public Slicer(Point point, int timescale) {
        super(point, timescale);
        setSpeed(defaultSpeed, timescale);
        count++;
        slicerImage = new Image("res/levels/images/slicer.png");
    }

    @Override
    public void drawImage() {
        super.drawImage(this.slicerImage);
    }


}
