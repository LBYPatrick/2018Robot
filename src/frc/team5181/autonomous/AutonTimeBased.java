package frc.team5181.autonomous;

import frc.team5181.tasking.SyncTask;
import frc.team5181.tasking.Task;
import frc.team5181.tasking.TaskSequence;

public class AutonTimeBased implements Task {

    final public static int Left = 0, Middle = 1, Right = 2;
    private TaskSequence seq;

    private int position;

    public AutonTimeBased(int position) {
        this.position = position;
        boolean isLeftSide = AutonHelper.isCorrectSwitchLeft();
        seq = new TaskSequence().add(
                new AutonHelper.TimedDrive(0,1,500),
                new AutonHelper.TimedDrive());

        switch (this.position) {
            case 0: //Left
                seq.add(
                new SyncTask(() -> {AutonHelper.report("LEFT SIDE");}),
                new AutonHelper.TimedDrive(0, 1, 500), //Go forward for passing the auto-line
                new AutonHelper.TimedDrive());
                if(isLeftSide) { //Shoot the cube in if it is the correct side
                    seq.add(
                        new SyncTask(() -> {AutonHelper.report("CORRECT SWITCH");}),
                        new AutonHelper.TimedDrive(1, 0, 180), //Turn Right
                        new AutonHelper.TimedDrive(),
                        new AutonHelper.TimedDrive(0, 1, 300), //Go forward
                        new AutonHelper.TimedDrive()
                    );
                }
                break;
            case 1: // Middle
                seq.add(
                        new SyncTask(() -> {AutonHelper.report("MIDDLE");}),
                    new AutonHelper.TimedDrive(isLeftSide? -1 : 1, 0, 180),//Turn Left/Right
                    new AutonHelper.TimedDrive(),
                    new AutonHelper.TimedDrive(0,1, 300), //Go Forward
                    new AutonHelper.TimedDrive(),
                    new AutonHelper.TimedDrive(isLeftSide? 1 : -1, 0,180), //Turn Right/Left
                    new AutonHelper.TimedDrive(),
                    new AutonHelper.TimedDrive(0,1,300) //Final Forward
                );
                break;
            case 2: //Right
                seq.add(
                        new SyncTask(() -> {AutonHelper.report("RIGHT SIDE");}),
                        new AutonHelper.TimedDrive(0, 1, 500), //Go forward for passing the auto-line
                        new AutonHelper.TimedDrive());
                seq.add(
                        new SyncTask(() -> {AutonHelper.report("CORRECT SWITCH");}),
                        new AutonHelper.TimedDrive(-1, 0, 180), //Turn Right
                        new AutonHelper.TimedDrive(),
                        new AutonHelper.TimedDrive(0, 1, 300), //Go forward
                        new AutonHelper.TimedDrive()
                );
                break;
            default:
                AutonHelper.report("Go Forward only");
        }


        seq.add(new AutonHelper.shootCube(1)); //Shoot the cube out
        AutonHelper.report("All done.");
        
    }

    public boolean nextStep() {
        return seq.nextStep();
    }
}
