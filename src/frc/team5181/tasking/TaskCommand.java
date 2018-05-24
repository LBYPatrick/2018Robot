package frc.team5181.tasking;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * wrapper for task to command
 * Created by TylerLiu on 2017/03/17.
 */
public class TaskCommand extends Command {

    Task t;
    boolean exitFlag = false;

    public TaskCommand(Task... t){
        super();
        if (t.length == 1) this.t = t[0];
        else this.t = new ParallelTask(t);
    }

    @Override
    public void start() {
    }

    @Override
    public void execute() {
        if (!exitFlag) exitFlag = t.nextStep();
        String[] msgs = t.toString().split("\n");
        int index = 0;
        for (String msg : msgs) {
        	SmartDashboard.putString("State" + (index++), msg);
        }
    }

    @Override
    public boolean isFinished() {
        return exitFlag;
    }
}
