package TowerPackage;

import EnemyPackage.*;
import PlayerPackage.PlayerData;
import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.ListIterator;
import static java.lang.Math.atan2;

public abstract class AbstractTank {
    private static final int TOSECONDS = 1000;
    private static final int FPS = 60;
    private int radius, cooldown, damage;
    private Point pPos;
    private Image tankImage , projImage;
    private double angle,time;
    private DrawOptions option = new DrawOptions();
    private ArrayList<Projectile> projectileList= new ArrayList();

    public AbstractTank(Point point) {
        pPos = point;
    }

    protected void setAttributes(Image tankImage, Image projImage,  int radius, int cooldown, int damage){
        this.tankImage = tankImage;
        this.projImage = projImage;
        this.radius = radius;
        this.cooldown = cooldown;
        this.damage = damage;
        time = cooldown;
        angle = 0;
    }

    public void updateTime(){
        time += PlayerData.getInstance().getTimescale();
    }

    public void updateTank(AbstractEnemy enemy){
        //Adds new projectile and resets time
        if (time >= cooldown*FPS/TOSECONDS){
            drawTank(enemy);
            projectileList.add(new Projectile(projImage, damage, pPos, enemy));
            time = 0;
        }

        //Iterates over projectile list and updates pos or removes if the enemy is dead
        ListIterator<Projectile> itr = projectileList.listIterator();
        while(itr.hasNext()) {
            Projectile p = itr.next();
            if(p.getStatus().equals("dormant")) {
                itr.remove();
            }
            p.updateProjectile();
        }
    }

    //Draws tank with last angle value
    public void drawTank(){
        tankImage.draw(pPos.x, pPos.y, option.setRotation(angle));
    }

    //Draws tank if moving to face an enemy
    public void drawTank(AbstractEnemy enemy){
        //Vector calculations to face the enemy
        Point ePos = enemy.getPoint();
        Vector2 vEnemy = new Vector2(ePos.x, ePos.y);
        Vector2 vPos = new Vector2(pPos.x, pPos.y);
        vPos = vPos.sub(vEnemy);
        //Ended up calculating angle from enemy to tank rather than other way around
        angle = atan2(vPos.y, vPos.x);
        //Angle adjustment from my weird vector calculations
        angle += 3*Math.PI/2;
        //Draws the tank in the direction of the tank facing enemy
        tankImage.draw(pPos.x, pPos.y, option.setRotation(angle));
    }

    public int getRadius(){
        return radius;
    }

    public Point getPos(){
        return pPos;
    }
}
