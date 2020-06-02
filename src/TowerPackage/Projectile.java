package TowerPackage;

import EnemyPackage.AbstractEnemy;
import PlayerPackage.PlayerData;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import static java.lang.Math.atan2;

public class Projectile {

    private static int SPEED = 10;
    private Vector2 vPos;
    private int damage;
    private Image image;
    private AbstractEnemy enemy;
    private String status;

    public Projectile(Image image, int damage, Point pos, AbstractEnemy enemy){
        status = "active";
        vPos = pos.asVector();
        this.image = image;
        this.damage = damage;
        this.enemy = enemy;
    }

    public void updateProjectile(){
        if(enemy == null) {
            status = "dormant";
            return;
        }
        Point enemyPos = enemy.getPoint();
        Vector2 vTow = new Vector2(enemyPos.x, enemyPos.y);
        vTow = vTow.sub(vPos);
        vTow = vTow.normalised();
        //Multiplies by speed then timescale
        vTow = vTow.mul(SPEED*PlayerData.getInstance().getTimescale());
        vPos = vPos.add(vTow);
        drawProjectile();

        //Checks if the projectile is within the bounds of the enemy
        if(image.getBoundingBoxAt(vPos.asPoint()).intersects(enemy.getBounds())){
            enemy.hit(damage);
            status = "dormant";
        }
    }

    public void drawProjectile(){
        image.draw(vPos.x, vPos.y);
    }

    public String getStatus(){
        return status;
    }
}
