import java.util.List;

public class DelayEvent extends WaveEvent {

    private int  delayTime, time;

    public DelayEvent(String waveData[]){
        super(waveData);
        delayTime = Integer.parseInt(waveData[2]);
        time = -1;
    }

    @Override
    public void startWave(int timescale, List Path) {
        time = 0;
        super.waveInProgress();
    }

    /*Ends wave when time is up*/
    @Override
    public void updateWave(int timescale, List path) {
        if(time <= delayTime && time >= 0) {
            time+= timescale;
            super.waveInProgress();
        }

        if (time >= delayTime) {
            super.eventOver();
            super.waveOver();
        }
    }


}
