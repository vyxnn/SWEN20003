import bagel.util.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnEvent extends WaveEvent {
    private final static int TOSECONDS = 1000;
    private final static int FPS = 60;

    private int spawnNumber, delayTime, time, spawned;
    private String enemyType;
    private ArrayList<Enemy> enemies = new ArrayList<>();

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
        //if(enemyType.equals("slicer")) {
            enemies.add(new Slicer((Point) path.get(0), timescale));
            spawned = 1;
        //}
        super.waveInProgress();
    }

    @Override
    public void updateWave(int timescale, List path) {
        changeSpeed(timescale);

        if (time >= 0) {
            time+= timescale;
        }
        /* Checks if there's enemies to spawn*/
        if(time >= ((delayTime/TOSECONDS)*FPS) && spawned < spawnNumber ){
            enemies.add(new Slicer((Point) path.get(0), timescale));
            spawned++;
            time = 0;
        }

        /*Moves enemies and deletes them once they've reached the end*/
        Iterator<Enemy> itr = enemies.iterator();
        while(itr.hasNext()) {
            Enemy e = itr.next();

            if(e.getIndex() + 1 < path.size()) {
                e.updatePos((Point) path.get(e.getIndex() + 1));
                e.drawImage();
            }
            else {
                itr.remove();
            }
        }

        /*Ends wave if no more enemies left or has spawned waves*/
        if(enemies.isEmpty()) {
            super.waveOver();
        }

        else if (spawned == spawnNumber) {
            super.eventOver();
        }

        else {
            super.waveInProgress();
        }
    }

    private void changeSpeed(int timescale){
        for(Enemy e: enemies) {
            if (e == null) {
                break;
            }
            e.changeSpeed(timescale);
        }
    }


}
