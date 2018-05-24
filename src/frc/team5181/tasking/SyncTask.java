package frc.team5181.tasking;

public class SyncTask implements Task {

  private Runnable runnable;

  public SyncTask(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public boolean nextStep() {
    this.runnable.run();
    return true;
  }
}
