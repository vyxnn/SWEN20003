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

    @Override
    public void updateWave(int timescale, List path) {
        while(time <= delayTime/timescale && time >= 0) {
            time++;
            super.waveInProgress();
        }

        if (time >= delayTime/timescale) {
            endWave();
        }
    }

    @Override
    public void endWave() {
        super.waveOver();
    }

    @Override
    public void changeSpeed(int timescale) {

    }

}
