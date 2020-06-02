import EnemyPackage.AbstractEnemy;
import PlayerPackage.*;
import TowerPackage.*;
import bagel.Image;
import bagel.Input;
import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * Handles the towers for each level
 */
public class TowerHandler {
    private String placing;
    private final static String BLOCKED = "blocked";
    private Image tankImage = new Image("res/images/tank.png");
    private Image superTankImage = new Image("res/images/supertank.png");
    private Image airImage = new Image("res/images/airsupport.png");
    private ArrayList<AbstractTank> tankList = new ArrayList();
    private ArrayList<Airplane> airplaneList = new ArrayList();

    public TowerHandler(){
        placing = ShadowDefend.FALSE;
    }

    public void setPlacing(String placing){
        this.placing = placing;
    }

    public String getPlacing(){
        return placing;
    }

    public void drawTowerView(TiledMap map, Input input) {
        Point mousePos = input.getMousePosition();
        //check that mouse Pos is not out of bounds
        if (!map.hasProperty((int)mousePos.x, (int)mousePos.y, BLOCKED)){
            if (placing.equals(ShadowDefend.TANK)) {
                tankImage.draw(mousePos.x, mousePos.y);
            } else if (placing.equals(ShadowDefend.SUPERTANK)) {
                superTankImage.draw(mousePos.x, mousePos.y);
            } else if (placing.equals(ShadowDefend.AIRPLANE)) {
                airImage.draw(mousePos.x, mousePos.y);
            }
        }
    }

    public void placeTower(TiledMap map, Input input){
        Point mousePos = input.getMousePosition();
        //check that mouse Pos is not out of bounds and not intersected with a panel
        if (!map.hasProperty((int)mousePos.x, (int)mousePos.y, BLOCKED) && mousePos.y > Level.BUYHEIGHT
                && mousePos.y < (map.getHeight()-Level.STATUSHEIGHT - ShadowDefend.ITEM_OFFSET/2)){
            if (placing.equals(ShadowDefend.TANK)) {
                tankList.add(new Tank(mousePos));
                PlayerData.getInstance().loseMoney(ShadowDefend.TANKPRICE);
                placing = ShadowDefend.FALSE;
            } else if (placing.equals(ShadowDefend.SUPERTANK)) {
                tankList.add(new SuperTank(mousePos));
                PlayerData.getInstance().loseMoney(ShadowDefend.SUPERTANKPRICE);
                placing = ShadowDefend.FALSE;
            } else if (placing.equals(ShadowDefend.AIRPLANE)) {
                airplaneList.add(new Airplane(mousePos));
                PlayerData.getInstance().loseMoney(ShadowDefend.AIRPLANEPRICE);
                placing = ShadowDefend.FALSE;
            }
        }
    }

    /**
     * Draws the tower and updates the time (cooldown) for each frame
     */
    public void drawTower(){
        for(AbstractTank t: tankList){
            if (t == null) {
                break;
            }
            t.drawTank();
            t.updateTime();
        }
    }

    /**
     * Locates the first enemy in the list and targets if it is within the radius
     * @param enemyList
     */
    public void updateTowerList(ArrayList<AbstractEnemy> enemyList){
        for(AbstractTank t: tankList){
            if (t == null) {
                break;
            }
            //From these calculations should get a rectangle with tank in the centre
            Rectangle tankRange = new Rectangle(t.getPos().x - t.getRadius()/2, t.getPos().y - t.getRadius()/2,
                    t.getRadius(), t.getRadius());
            //If the enemy intersects with a tank, will update and check it's range
            for(AbstractEnemy e: enemyList) {
                if(e.getBounds().intersects(tankRange)){
                    t.updateTank(e);
                    break;
                }
            }
        }
    }

}
