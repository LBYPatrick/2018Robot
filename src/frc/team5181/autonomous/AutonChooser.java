package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.tasking.ParallelTask;
import frc.team5181.tasking.SyncTask;
import frc.team5181.tasking.Task;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<String> chooser;
    public static Task autonChoice;

    private static void chooserAdd(String commandName){
        chooser.addObject(commandName,commandName);
    }
    
    public static void chooserInit(){
        chooser = new SendableChooser<>();
        chooserAdd("L");
        chooserAdd("M");
        chooserAdd("R");
        chooserAdd("N");
        SmartDashboard.putData("Auton Chooser", chooser);
    }
    
    public static Task getAutonCommand(){
        DriverStation.reportWarning(chooser.getSelected(), false);
        switch (chooser.getSelected()) {
            case "L" :
                return new AutonTimeBased(0); //Do Nothing
            case "M" :
                return new AutonTimeBased(1);
            case "R" :
                return new AutonTimeBased(2);
            case "N":
                return new AutonTimeBased(3);
            default:
                throw new RuntimeException();
        }
    }
    
    public static Task getSelected() {
    	DriverStation.reportWarning(chooser.getSelected(),false);
    	switch(chooser.getSelected()) {
    	case "L" : return new AutonTimeBased(0);
    	case "M" : return new AutonTimeBased(1);
    	case "R" : return new AutonTimeBased(2);
    	case "N" : return new AutonTimeBased(3);
    	default : DriverStation.reportWarning("No mode picked", false);
    		return null;
    	}
    	
    }
}