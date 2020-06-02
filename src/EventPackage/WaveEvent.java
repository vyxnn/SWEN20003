package EventPackage;
import EnemyPackage.AbstractEnemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a wave event based off the data read
 * Can be either a Spawn or Delay event
 */
public abstract class WaveEvent {
    private int waveNumber;
    private String eventType, waveProgress, eventProgress;

    /**
     * Creates a wave event based of data passed in
     * @param waveData Passed in a string with wave data
     */
    public WaveEvent(String[] waveData){
        waveNumber = Integer.parseInt(waveData[0]);
        eventType = waveData[1];
        waveProgress = "Awaiting Start";
        eventProgress = "Awaiting Start";
    }

    /**
     * Returns the wave progress
     * Will be printed to the status panel
     * @return wave progress as a string
     */
    public String getWaveProgress(){
        return waveProgress;
    }

    public String getEventType(){
        return eventType;
    }

    /**
     * Returns current event progress
     * Used by Level to check the status of a wave
     * @return event progress as a string
     */
    public String getEventProgress(){
        return eventProgress;
    }

    protected void waveInProgress(){
        waveProgress = "Wave in Progress";
    }

    protected void waveOver(){
        waveProgress = "Awaiting Start";
    }

    protected void eventOver(){
        eventProgress = "Event Over";
    }

    /**
     * Returns the wave number
     * Will be printed to the status panel
     * @return wave number as an int
     */
    public int getWaveNumber(){
        return waveNumber;
    }

    /**
     * Starts a wave event
     * @param path level's path to follow
     */
    public abstract void startWaveEvent(List path);

    /**
     * Updates a wave event
     * @param path level's path to follow
     */
    public abstract void updateWaveEvent(List path);

    public abstract ArrayList<AbstractEnemy> getEnemyList();
}
