package EventPackage;
import java.util.List;

public abstract class WaveEvent {
    private int waveNumber;
    private String eventType, waveProgress, eventProgress;

    public WaveEvent(String[] waveData){
        waveNumber = Integer.parseInt(waveData[0]);
        eventType = waveData[1];
        waveProgress = "Awaiting Start";
        eventProgress = "Awaiting Start";
    }

    public String getEventType(){
        return eventType;
    }

    public String getWaveProgress(){
        return waveProgress;
    }

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

    public int getWaveNumber(){
        return waveNumber;
    }

    public abstract void startWave(int timescale, List path);
    public abstract void updateWave(int timescale, List path);
}
