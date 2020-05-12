import bagel.Window;
import bagel.map.TiledMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private int timescale, levelNum, waveNum, eventIndex;
    private TiledMap map;
    private ArrayList<WaveEvent> events = new ArrayList<>();
    private List path;

    public Level(int levelNum){
        this.levelNum = levelNum;
        waveNum = 0;
        eventIndex = 0;
        //Gets map and wave data for specific level
        if (this.levelNum == 1) {
            map = new TiledMap("res/levels/1.tmx");
            readData("res/levels/waves.txt");
        }
        else {
            map = new TiledMap("res/levels/2.tmx");
            //readData("res/levels/waves2.txt");
        }

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
                events.add(new DelayEvent(tmp));
            } else if (tmp[1].equals("spawn")) {
                events.add(new SpawnEvent(tmp));
            }

            //Do the throws/exceptions when reading
            else {
                System.out.println("error");
            }
        }
    }

    public void increaseTimescale(){
        timescale++;
        events.get(eventIndex).increaseSpeed(timescale);
    }

    public void decreaseTimescale(){
        if (timescale>1) {
            timescale--;
            events.get(eventIndex).decreaseSpeed(timescale);
        }
    }

    public int getTimescale(){
        return timescale;
    }

    public void startWave(){
        waveNum++;
        if(events.get(eventIndex).getWaveNumber() == waveNum) {
            events.get(eventIndex).startWave(timescale, path);
        }
    }

    public void updateLevel(){
        if(events.get(eventIndex).getWaveProgress().equals("Wave in Progress")) {
            events.get(eventIndex).updateWave(timescale, path);
        }

        if(events.get(eventIndex).getWaveProgress().equals("Awaiting Start")
                && events.get(eventIndex + 1).getWaveNumber() == waveNum) {
            eventIndex++;
            startWave();
        }
    }

    public void endLevel(){

    }

}
