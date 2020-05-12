import java.util.List;

public abstract class WaveEvent {
    private int waveNumber;
    private String eventType, waveProgress;

    public WaveEvent(String[] waveData){
        waveNumber = Integer.parseInt(waveData[0]);
        eventType = waveData[1];
        waveProgress = "Awaiting Start";
    }

    protected String getEventType(){
        return eventType;
    }

    protected String getWaveProgress(){
        return waveProgress;
    }

    protected void waveInProgress(){
        waveProgress = "Wave in Progress";
    }

    protected void waveOver(){
        waveProgress = "Awaiting Start";
    }

    public int getWaveNumber(){
        return waveNumber;
    }

    public abstract void startWave(int timescale, List path);
    public abstract void updateWave(int timescale, List path);
    public abstract void endWave();
    public abstract void increaseSpeed(int timescale);
    public abstract void decreaseSpeed(int timescale);
}
