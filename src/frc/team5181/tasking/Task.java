package frc.team5181.tasking;

public interface Task {
  /**
   * Called each frame.
   * @return If the task is completed.
   */
  boolean nextStep();
}
