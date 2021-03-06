package event;
import main.*;
import enemy.*;
import player.PlayerData;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A spawn event is created based off the wave data
 * Spawns enemies at intervals
 */
public class SpawnEvent extends WaveEvent {
    private static final String SLICER = "slicer";
    private static final String SUPERSLICER = "superslicer";
    private static final String MEGASLICER = "megaslicer";
    private static final String APEXSLICER = "apexslicer";
    private int spawnNumber, delayTime, time, spawned;
    private String enemyType;
    private ArrayList<AbstractEnemy> enemyList = new ArrayList<>();

    /**
     * Constructor for the spawn event
     * Spawns specific enemies with a specified delay time
     * @param waveData takes in a string to create a spawn event
     */
    public SpawnEvent(String waveData[]){
        super(waveData);
        spawnNumber = Integer.parseInt(waveData[2]);
        enemyType = waveData[3];
        delayTime = Integer.parseInt(waveData[4]);
        time = -1;
    }

    /**
     * Starts the spawn event by spawning an enemy
     * Sets time, spawn number and progress
     * @param path level's path to follow
     */
    @Override
    public void startWaveEvent(List path) {
        time = 0;
        addEnemy(path);
        spawned = 1;
        super.waveInProgress();
    }

    /**
     * Updates the spawn wave event by checking if an enemy should be spawned
     * @param path level's path to follow
     */
    @Override
    public void updateWaveEvent(List path) {
        /*Updating speed and keeping track of time for spawning*/
        changeSpeed();
        if (time >= 0) {
            time+= PlayerData.getInstance().getTimescale();
        }

        /* Checks if there's enemies to spawn*/
        if(time >= (delayTime*ShadowDefend.FPS/ShadowDefend.TOSECONDS) && spawned < spawnNumber ){
            addEnemy(path);
            spawned++;
            time = 0;
        }

        /*Moves enemies and deletes them once they've reached the end*/
        ListIterator<AbstractEnemy> itr = enemyList.listIterator();
        while(itr.hasNext()) {
            AbstractEnemy e = itr.next();
            //Slicer is dead, will reward and spawn new enemies, removes dead enemy
            if(e.getHealth() <= 0) {
                e.enemyDeath(itr);
            }
            //Updates and draws if slicer is still on path
            else if(e.getIndex() + 1 < path.size()) {
                e.updatePos((Point) path.get(e.getIndex() + 1));
                e.drawImage();
            }
            //Reached end of map without death, will add penalty, removes enemies from map
            else {
                e.enemyPenalty();
                itr.remove();
            }
        }

        //Checks and updates the wave and event progress
        updateProgress();
    }

    //Checks the progress of a wave event
    private void updateProgress(){
        /*Ends wave if all enemies are dead or left the map*/
        if(enemyList.isEmpty() && spawned == spawnNumber) {
            super.waveOver();
        }
        /*Ends wave event if there is no more enemies to spawn*/
        else if (spawned == spawnNumber) {
            super.eventOver();
        }

        else {
            super.waveInProgress();
        }
    }

    /*Choosing a spawn type for the event*/
    private void addEnemy(List path){
        if(enemyType.equals(SLICER)) {
            enemyList.add(new Slicer((Point) path.get(0), 0));
        }

        else if (enemyType.equals(SUPERSLICER)) {
            enemyList.add(new SuperSlicer((Point) path.get(0),0));
        }

        else if(enemyType.equals(MEGASLICER)) {
            enemyList.add(new MegaSlicer((Point) path.get(0),0));
        }

        else if(enemyType.equals(APEXSLICER)) {
            enemyList.add(new ApexSlicer((Point) path.get(0),0));
        }
    }

    /* Updates the current enemy speeds based off current timescale */
    private void changeSpeed(){
        for(AbstractEnemy e: enemyList) {
            if (e == null) {
                break;
            }
            e.changeSpeed();
        }
    }

    //GETTERS
    /**
     * Gets and returns the current list of enemies
     * @return ArrayList of enemies
     */
    public ArrayList<AbstractEnemy> getEnemyList(){
        return enemyList;
    }

}
