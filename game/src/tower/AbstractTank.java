package tower;
import main.*;
import enemy.*;
import player.PlayerData;
import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.ListIterator;
import static java.lang.Math.atan2;

/**
 * AbstractTank class, has two subclasses Tank and SuperTank
 * Mainly controls shooting and drawing tanks
 */
public abstract class AbstractTank {
    private int radius, cooldown, damage;
    private Point pPos;
    private Image tankImage , projImage;
    private double angle,time;
    private DrawOptions option = new DrawOptions();
    private ArrayList<Projectile> projectileList= new ArrayList<>();

    /**
     * Constructor for the tank
     * Creates a tank at a certain point
     * @param point where a tank is placed
     */
    public AbstractTank(Point point) {
        pPos = point;
    }

    //Used by child class to set its attributes
    protected void setAttributes(Image tankImage, Image projImage,  int radius, int cooldown, int damage){
        this.tankImage = tankImage;
        this.projImage = projImage;
        this.radius = radius;
        this.cooldown = cooldown;
        this.damage = damage;
        time = cooldown;
        angle = 0;
    }

    /**
     * Increments time for a tank
     * Used in calculating cooldown
     */
    public void updateTime(){
        time += PlayerData.getInstance().getTimescale();

    }

    /**
     * Updates a tank, checks its time to see it can shoot
     * Also updates the projectile list
     * @param enemy gets given an enemy to target
     */
    public void updateTank(AbstractEnemy enemy){
        //Adds new projectile and resets time
        if (time >= cooldown*ShadowDefend.FPS/ShadowDefend.TOSECONDS){
            drawTank(enemy);
            projectileList.add(new Projectile(projImage, damage, pPos, enemy));
            time = 0;
        }
        //Iterates over projectile list and updates pos or removes if the enemy is dead
        ListIterator<Projectile> itr = projectileList.listIterator();
        while(itr.hasNext()) {
            Projectile p = itr.next();
            //Updates projectile
            p.updateProjectile();
            //If projectile is dormant, removes from list
            if(p.getStatus().equals(ShadowDefend.DORMANT)) {
                itr.remove();
            }
        }
    }

    /**
     * Draws a tank with its previous angle
     */
    public void drawTank(){
        tankImage.draw(pPos.x, pPos.y, option.setRotation(angle));
    }

    /**
     * Moves a tank to face its enemy and draws it
     * @param enemy targeted by tank
     */
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
        drawTank();
    }

    //GETTERS

    /**
     * Returns the radius
     * @return radius
     */
    public int getRadius(){
        return radius;
    }

    /**
     * Returns its position as a point
     * @return position as Point
     */
    public Point getPos(){
        return pPos;
    }

    /**
     * Returns the bounds
     * @return bounds as Rectangle
     */
    public Rectangle getBounds(){
        return tankImage.getBoundingBoxAt(pPos);
    }
}
