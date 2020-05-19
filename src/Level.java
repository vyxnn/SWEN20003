import bagel.Window;
import bagel.map.TiledMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Level {
    //Rewards for the wave
    private static final int BASEREWARD = 150;
    private static final int WAVEINCREMENT = 100;

    private int timescale, levelNum, waveNum, eventIndex;
    private TiledMap map;
    //change events name
    private ArrayList<Integer> eventIndexList = new ArrayList<>();
    private ArrayList<WaveEvent> waveEventList = new ArrayList<>();
    private List path;

    public Level(int levelNum){
        this.levelNum = levelNum;
        waveNum = 0;
        eventIndex = -1;
        //Gets map and wave data for specific level
        if (this.levelNum == 1) {
            map = new TiledMap("res/levels/1.tmx");
        }
        else {
            map = new TiledMap("res/levels/2.tmx");
            //readData("res/levels/waves2.txt");
        }

        readData("res/levels/waves.txt");
        path = map.getAllPolylines().get(0);
        timescale = ShadowDefend.INITIALTIMESCALE;
    }

    //Insert your own level and map and file
    public Level(int levelNum, TiledMap map, String file){
        this.levelNum = levelNum;
        this.map = map;
        readData(file);
    }

    public void drawMap(){
        map.draw(0, 0, 0, 0, Window.getWidth(), Window.getHeight());
    }

    public List getPath(){
        return path;
    }

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

    public void increaseTimescale(){
        timescale++;
    }

    public void decreaseTimescale(){
        if (timescale>1) {
            timescale--;
        }
    }

    public int getTimescale(){
        return timescale;
    }

    public void startWave(){
        waveNum++;
        startWaveEvent();
    }

    private void startWaveEvent(){
        eventIndex++;
        eventIndexList.add(eventIndex);
        if(waveEventList.get(eventIndex).getWaveNumber() == waveNum) {
            waveEventList.get(eventIndex).startWave(timescale, path);
        }
    }

    // want to make it keep drawing for all active wave events
    public void updateLevel() {
        //Updates position for each active wave event
        for(Integer eIndex : eventIndexList) {
            if (waveEventList.get(eIndex).getWaveProgress().equals("Wave in Progress")) {
                waveEventList.get(eIndex).updateWave(timescale, path);
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

    public void endLevel(){

    }


    public int getWaveNum(){
        return waveNum;
    }

    public String getWaveProgress(){
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

}
