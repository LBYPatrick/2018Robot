package frc.team5181.sensors;

public class LSProfiler {
    private long startTime = 0;
    private long endTime = 0;
    private String name;
    private boolean isManualStopped = false;

    public LSProfiler(String name) {
        this.name = name;
        this.startTime = System.currentTimeMillis();
    }

    public void start() { this.startTime = System.currentTimeMillis(); }

    public void stop() {
        this.endTime = System.currentTimeMillis();
        this.isManualStopped = true;
    }

    public String toString() {
        if(!this.isManualStopped) { stop();this.isManualStopped = false; }

        return "["+this.name+"] Time elapsed: " + (this.endTime-this.startTime) + "ms";
    }

}
