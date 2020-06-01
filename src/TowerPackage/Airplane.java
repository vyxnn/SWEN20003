package TowerPackage;

import bagel.util.Point;

public class Airplane {
    private static final int SPEED = 5;
    private int time, cooldown;
    private static int count=0;
    private Point pos;

    public Airplane(Point point){
        pos = point;
    }
}
