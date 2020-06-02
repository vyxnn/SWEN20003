package EventPackage;
import EnemyPackage.AbstractEnemy;
import PlayerPackage.PlayerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a delay event that makes the game do nothing for a set amount of time
 */
public class DelayEvent extends WaveEvent {
    // COULD ADD TO SHADOW DEFEND CLASS? NOT GOING TO CHANGE THOUGH
    private final static int TOSECONDS = 1000;
    private final static int FPS = 60;
    private int delayTime, time;

    /**
     * Constructor for delay event
     * Reads in how long the event should delay for
     * @param waveData takes in a string to create a delay event
     */
    public DelayEvent(String waveData[]){
        super(waveData);
        delayTime = Integer.parseInt(waveData[2]);
        time = -1;
    }

    /**
     * Starts the event by setting time and wave progress
     * @param Path (required for spawn event but not used here)
     */
    @Override
    public void startWaveEvent(List Path) {
        time = 0;
        super.waveInProgress();
    }

    /**
     * Updates the time and ends the wave event when the time is over
     * @param path (required for spawn event but not used here)
     */
    @Override
    public void updateWaveEvent( List path) {
        if(time <= delayTime/TOSECONDS*FPS && time >= 0) {
            time+= PlayerData.getInstance().getTimescale();
            super.waveInProgress();
        }

        if (time >= delayTime/TOSECONDS*FPS) {
            super.eventOver();
            super.waveOver();
        }
    }

    @Override
    public ArrayList<AbstractEnemy> getEnemyList() {
        return null;
    }


}
