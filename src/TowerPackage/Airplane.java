package TowerPackage;

import EnemyPackage.*;
import PlayerPackage.PlayerData;
import MainPackage.*;
import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class Airplane {
    private static final int SPEED = 5;
    private static final int LAUNCH = 4;
    private int time, cooldown, airNo;
    private String waiting;
    private static int count=0;
    private Vector2 vPos;
    private Point pPos;
    private DrawOptions option = new DrawOptions();
    private Image airplaneImage;
    private ArrayList<Explosive> explosiveList = new ArrayList();

    public Airplane(Point point){
        waiting = "YES";
        pPos = point;
        count++;
        airNo = count;
        time = 0;
        cooldown = 0;
        airplaneImage = new Image("res/images/airsupport.png");
        spawnAirplane();
    }

    private void spawnAirplane(){
        //Vertical if even, takes the y coordinate from where it was placed
        if(airNo%2 == 0) {
            vPos = new Vector2( 0, pPos.y);
        }
        //Horizontal if odd
        else {
            vPos = new Vector2(pPos.x , 0);
        }
    }

    private void drawAirplane(){
        if(airNo%2 == 0) {
            airplaneImage.draw(vPos.x, vPos.y, option.setRotation(Math.PI/2));
        }
        else {
            airplaneImage.draw(vPos.x, vPos.y, option.setRotation(Math.PI));
        }
    }


    public void updateAirplane(ArrayList<AbstractEnemy> enemyList){
        //Updates time
        time += PlayerData.getInstance().getTimescale();
        updatePos();
        drawAirplane();
        //Check if there's an active cooldown, if not assigns a new one
        if (waiting.equals("YES")) {
            Random random = new Random();
            cooldown = random.nextInt(LAUNCH);
            waiting = "NO";
        }
        //Adds explosive if cooldown is over, resets cooldown
        if(time >= cooldown*ShadowDefend.FPS && waiting.equals("NO")) {
            explosiveList.add(new Explosive(vPos.asPoint()));
            cooldown = 0;
            time = 0;
            waiting = "YES";
        }
        //Iterates over explosive list and updates time or explodes
        ListIterator<Explosive> itr = explosiveList.listIterator();
        while (itr.hasNext()) {
            Explosive exp = itr.next();
            exp.updateExplosive(itr, enemyList);
        }
    }

    private void updatePos(){
        //Adds the unit vector in the direction of X
        if(airNo%2 == 0) {
            Vector2 unitX = new Vector2(1,0);
            unitX = unitX.mul(SPEED*PlayerData.getInstance().getTimescale());
            vPos = vPos.add(unitX);
        }
        //Adds the unit vector in the direction of Y
        else {
            Vector2 unitY = new Vector2(0,1);
            unitY = unitY.mul(SPEED*PlayerData.getInstance().getTimescale());
            vPos = vPos.add(unitY);
        }
    }

    public Vector2 getPos(){
        return vPos;
    }

    public ArrayList<Explosive> getExplosiveList(){
        return explosiveList;
    }
}
