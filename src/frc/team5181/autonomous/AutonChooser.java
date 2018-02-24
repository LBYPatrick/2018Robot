package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.tasking.ParallelTask;
import frc.team5181.tasking.Task;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<String> chooser;

    private static void chooserAdd(String commandName){
        chooser.addObject(commandName,commandName);
    }
    
    public static void chooserInit(){
        chooser = new SendableChooser<>();
        chooserAdd("Left");
        chooserAdd("Middle");
        chooserAdd("Right");
        SmartDashboard.putData("Auton Chooser", chooser);
    }
    
    public static Task getAutonCommand(){
        DriverStation.reportWarning(chooser.getSelected(), false);
        switch (chooser.getSelected()) {
            case "Left" :
                return new ParallelTask(); //Do Nothing
            case "Middle" :
                return null;
            case "Right" :
                return null;
            default:
                throw new RuntimeException();
        }
    }
}