import bagel.util.Point;

public class Slicer extends Enemy {

    public static int count=0;

    //Constructor for slicer
    public Slicer(Point point, int speed) {
        super(point, speed);
        count++;
    }

}
