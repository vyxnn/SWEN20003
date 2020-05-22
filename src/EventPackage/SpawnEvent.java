package EventPackage;
import EnemyPackage.*;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SpawnEvent extends WaveEvent {
    private final static int TOSECONDS = 1000;
    private final static int FPS = 60;

    private int spawnNumber, delayTime, time, spawned;
    private String enemyType;
    private ArrayList<AbstractEnemy> enemyList = new ArrayList<>();

    public SpawnEvent(String waveData[]){
        super(waveData);
        spawnNumber = Integer.parseInt(waveData[2]);
        enemyType = waveData[3];
        delayTime = Integer.parseInt(waveData[4]);
        time = -1;
    }

    @Override
    public void startWave(int timescale, List path) {
        time = 0;
        addEnemy(timescale, path);
        spawned = 1;
        super.waveInProgress();
    }

    @Override
    public void updateWave(int timescale, List path) {
        /*Updating speed and keeping track of time for spawning*/
        changeSpeed(timescale);
        if (time >= 0) {
            time+= timescale;
        }

        /* Checks if there's enemies to spawn*/
        if(time >= ((delayTime/TOSECONDS)*FPS) && spawned < spawnNumber ){
            addEnemy(timescale, path);
            spawned++;
            time = 0;
        }

        /*Moves enemies and deletes them once they've reached the end*/
        ListIterator<AbstractEnemy> itr = enemyList.listIterator();
        while(itr.hasNext()) {
            AbstractEnemy e = itr.next();
            //Updates and draws if slicer is still on path
            if(e.getIndex() + 1 < path.size()) {
                e.updatePos((Point) path.get(e.getIndex() + 1));
                e.drawImage();
            }
            //Slicer is dead, will reward and spawn new enemies, removes dead enemy
            else if (e.getHealth() < 0) {
                e.enemyDeath(itr, timescale);
                itr.remove();
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


    private void updateProgress(){
        /*Ends wave if all enemies are dead or left the map*/
        if(enemyList.isEmpty()) {
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
    private void addEnemy(int timescale, List path){
        if(enemyType.equals("slicer")) {
            enemyList.add(new Slicer((Point) path.get(0), timescale));
        }

        else if (enemyType.equals("superslicer")) {
            enemyList.add(new SuperSlicer((Point) path.get(0), timescale));
        }

        else if(enemyType.equals("megaslicer")) {
            enemyList.add(new MegaSlicer((Point) path.get(0), timescale));
        }

        else if(enemyType.equals("apexslicer")) {
            enemyList.add(new ApexSlicer((Point) path.get(0), timescale));
        }
        //Throws error?
        else {
            System.out.println("Invalid Enemy Type");
        }
    }

    /* Updates the current enemy speeds based off current timescale */
    private void changeSpeed(int timescale){
        for(AbstractEnemy e: enemyList) {
            if (e == null) {
                break;
            }
            e.changeSpeed(timescale);
        }
    }


}
