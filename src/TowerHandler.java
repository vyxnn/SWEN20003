import PlayerPackage.*;
import TowerPackage.*;
import bagel.Image;
import bagel.Input;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.util.ArrayList;

public class TowerHandler {
    private String placing;
    private final static String BLOCKED = "blocked";
    private Image tankImage = new Image("res/images/tank.png");
    private Image superTankImage = new Image("res/images/supertank.png");
    private Image airImage = new Image("res/images/airsupport.png");
    private ArrayList<AbstractTank> tankList = new ArrayList();
    private ArrayList<Airplane> airplaneList = new ArrayList();

    public TowerHandler(){
        placing = "false";
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
        //check that mouse Pos is not out of bounds
        if (!map.hasProperty((int)mousePos.x, (int)mousePos.y, BLOCKED)){
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

    public void updateTowerList(){
        for(AbstractTank t: tankList){
            if (t == null) {
                break;
            }
            t.drawTank();
        }
    }
}
