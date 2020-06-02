import EventPackage.*;
import PlayerPackage.*;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Starts a single level
 */
public class Level {
    //Better way? Don't want to redefine but not sure about another way around it
    private final static Image buyPanel = new Image("res/images/buypanel.png");
    private final static Image statusPanel = new Image("res/images/statuspanel.png");
    public final static double BUYHEIGHT = buyPanel.getHeight();
    public final static double STATUSHEIGHT = statusPanel.getHeight();
    //private final double BUYWIDTH = buyPanel.getWidth();
    //private final double STATUSWIDTH = statusPanel.getWidth();
    //Rewards for the wave
    private static final int BASEREWARD = 150;
    private static final int WAVEINCREMENT = 100;

    private int levelNum, waveNum, eventIndex;
    private TiledMap map;
    private List path;
    private ArrayList<Integer> eventIndexList = new ArrayList<>();
    private ArrayList<WaveEvent> waveEventList = new ArrayList<>();
    private TowerHandler towerHandler;

    /**
     * Constructs a level given a level number
     * @param levelNum the number of the level
     */
    public Level(int levelNum){
        this.levelNum = levelNum;
        waveNum = 0;
        eventIndex = -1;
        //Gets map for the specific level
        if (this.levelNum == 1) {
            map = new TiledMap("res/levels/1.tmx");
        }
        else {
            map = new TiledMap("res/levels/2.tmx");
        }

        //Scans in the wave data, path and sets the timescale to 1
        readData("res/levels/waves.txt");
        path = map.getAllPolylines().get(0);
        //Creates a new tower handler to deal with the placements of towers
        towerHandler = new TowerHandler();
    }

    /**
     * Not used in this project, but can customise and add your own level, map and wave data
     * @param levelNum pass in the level number
     * @param map pass in the map
     * @param file pass in the file data
     */
    public Level(int levelNum, TiledMap map, String file){
        //Follows same logic as the original constructor
        waveNum = 0;
        eventIndex = -1;
        this.levelNum = levelNum;
        this.map = map;
        readData(file);
        path = map.getAllPolylines().get(0);
    }

    /**
     * Draws the map of the level
     */
    public void drawMap(){
        map.draw(0, 0, 0, 0, Window.getWidth(), Window.getHeight());
    }

    /**
     * Returns the map of the level
     * @return TiledMap
     */
    public TiledMap getMap(){
        return map;
    }

    //Reads from a file into an ArrayList of Strings
    private void readData(String file){
        ArrayList<String> eventString = new ArrayList<>();
        try(BufferedReader br =
                new BufferedReader(new FileReader(file))) {
            String text = null;
            while ((text = br.readLine()) != null) {
                eventString.add(text);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        createList(eventString);
    }

    //Creates an ArrayList of events
    private void createList(ArrayList<String> eventString){
        for(String s: eventString) {
            String tmp[] = s.split(",");
            if (tmp[1].equals("delay")) {
                waveEventList.add(new DelayEvent(tmp));
            } else if (tmp[1].equals("spawn")) {
                waveEventList.add(new SpawnEvent(tmp));
            }
            //Do the throws/exceptions when reading
            else {
                System.out.println("error");
            }
        }
    }

    /**
     * Starts a Wave (after S is pressed in the ShadowDefend class)
     */
    public void startWave(){
        waveNum++;
        startWaveEvent();
    }

    //Starts a wave event
    private void startWaveEvent(){
        eventIndex++;
        eventIndexList.add(eventIndex);
        if(waveEventList.get(eventIndex).getWaveNumber() == waveNum) {
            waveEventList.get(eventIndex).startWaveEvent(path);
        }
    }

    /**
     * Updates the level 60 times per second
     * Called by the update method in ShadowDefend Class
     */
    public void updateLevel() {
        //Draws tower and updates time
        towerHandler.updateTower();
        //Updates position for each active wave event
        for(Integer eIndex : eventIndexList) {
            if (waveEventList.get(eIndex).getWaveProgress().equals("Wave in Progress")) {
                waveEventList.get(eIndex).updateWaveEvent(path);
            }
            //For each spawn event will check the list of enemies and target them
            if(waveEventList.get(eIndex).getEventType().equals("spawn")) {
                towerHandler.updateTowerList(waveEventList.get(eIndex).getEnemyList());
            }
        }

        //Check if the last event is over, and adds new event if it is
        if(eventIndexList.size() >= 1) {
            Integer lastIndex = eventIndexList.get(eventIndexList.size() - 1);
            if (lastIndex < waveEventList.size() -1
                    && waveEventList.get(lastIndex).getEventProgress().equals("Event Over")
                    && waveEventList.get(lastIndex + 1).getWaveNumber() == waveNum) {
                startWaveEvent();
            }
        }

        //Checks if entire wave is over by checking that every wave event has ended
        int count = 0;
        for(Integer eIndex : eventIndexList){
            if(!waveEventList.get(eIndex).getWaveProgress().equals("Awaiting Start")) {
                break;
            }
            count++;
        }

        //Removes all current indexes and rewards money if it is
        if(count == eventIndexList.size() && !eventIndexList.isEmpty()) {
            eventIndexList.removeAll(eventIndexList);
            PlayerData.getInstance().addMoney(BASEREWARD + waveNum*WAVEINCREMENT);
        }

    }

    /**
     * Ends the level?
     */
    public void endLevel(){

    }

    /**
     * Returns the current wave number for printing in status panel
     * @return wave number
     */
    public int getWaveNum(){
        return waveNum;
    }

    /**
     * Returns the wave progress of current wave for status panel
     * @return wave progress
     */
    public String getWaveProgress(){
        //Checks if a tower is being placed
        if(!towerHandler.getPlacing().equals(ShadowDefend.FALSE)){
            return "Placing";
        }
        //If there is an active wave event, returns said wave progress
        if(eventIndex >= 0 && !eventIndexList.isEmpty()) {
            Integer lastIndex = eventIndexList.get(eventIndexList.size() - 1);
            return waveEventList.get(lastIndex).getWaveProgress();
        }

        //Default
        else {
            return "Awaiting Start";
        }
    }

    public String getTowerProgress(){
        return towerHandler.getPlacing();
    }

    public void setTowerProgress(String progress){
        towerHandler.setPlacing(progress);
    }

    public void drawTowerView(Input input){
        towerHandler.drawTowerView(map, input);
    }

    public void placeTower(Input input){
        towerHandler.placeTower(map, input);
    }

    //Checks where the position of the mouse is, and creates a tank if valid
    public void checkMouse(Input input) {
        //Check that mouse position is valid
        Point mousePos = input.getMousePosition();
        //If the mouse is in the tank area, and has enough funds will place
        if(mousePos.x > 0 && mousePos.x <(ShadowDefend.ITEM_OFFSET + ShadowDefend.ITEM_GAP/2 ) && mousePos.y > 0
                && mousePos.y < BUYHEIGHT && PlayerData.getInstance().getMoney() >= ShadowDefend.TANKPRICE){
            towerHandler.setPlacing(ShadowDefend.TANK);
        }
        //If the mouse in the the supertank area
        else if (mousePos.x > (ShadowDefend.ITEM_OFFSET + ShadowDefend.ITEM_GAP/2)
                && mousePos.x <(ShadowDefend.ITEM_OFFSET + ShadowDefend.ITEM_GAP/2 + ShadowDefend.ITEM_GAP)
                && mousePos.y > 0 && mousePos.y < BUYHEIGHT
                && PlayerData.getInstance().getMoney() >= ShadowDefend.SUPERTANKPRICE){
            towerHandler.setPlacing(ShadowDefend.SUPERTANK);
        }
        //If the mouse is in the airplane area
        else if (mousePos.x >(ShadowDefend.ITEM_OFFSET + ShadowDefend.ITEM_GAP/2 + ShadowDefend.ITEM_GAP)
                && mousePos.x <(ShadowDefend.ITEM_OFFSET + ShadowDefend.ITEM_GAP/2 + 2* ShadowDefend.ITEM_GAP)
                && mousePos.y > 0 && mousePos.y < BUYHEIGHT
                && PlayerData.getInstance().getMoney() >= ShadowDefend.AIRPLANEPRICE) {
            towerHandler.setPlacing(ShadowDefend.AIRPLANE);
        }
    }
}
