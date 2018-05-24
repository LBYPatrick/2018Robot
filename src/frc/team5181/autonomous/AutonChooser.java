package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<Runnable> chooser;

    public static void chooserInit(){
        chooser = new SendableChooser<>();
        chooser.addObject("Left",                           new AutonTimeBased(AutonTimeBased.Mode.Left));
        chooser.addObject("Middle",                         new AutonTimeBased(AutonTimeBased.Mode.Middle));
        chooser.addObject("Right",                          new AutonTimeBased(AutonTimeBased.Mode.Right));
        chooser.addObject("Left Straight+Cube",             new AutonTimeBased(AutonTimeBased.Mode.LSCube));
        chooser.addObject("Right Straight+Cube",            new AutonTimeBased(AutonTimeBased.Mode.RSCube));
        chooser.addObject("L/R Move Only",                  new AutonTimeBased(AutonTimeBased.Mode.LRAutoline));
        chooser.addObject("Middle Move Only",               new AutonTimeBased(AutonTimeBased.Mode.MiddleMove));
        chooser.addObject("Automatic Move",                 new AutonTimeBased(AutonTimeBased.Mode.AutoMove));
        chooser.addObject("Automatic Cube",                 new AutonTimeBased(AutonTimeBased.Mode.FullAuto));
        SmartDashboard.putData("Auton Chooser", chooser);
    }

    public static Runnable getSelected() {
        return chooser.getSelected();
    }
}