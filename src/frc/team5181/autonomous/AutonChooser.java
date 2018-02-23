package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.tasking.ParallelTask;
import frc.team5181.tasking.Task;

public class AutonChooser {

    private static SendableChooser<AutonMode> chooser = new SendableChooser<>();

    public static void addOption(String name,AutonMode obj) {
        chooser.addObject(name,obj);
    }

    public static void updateDashBoard() {
        SmartDashboard.putData("AUTON_MODES",chooser);
    }

    public static AutonMode getSelected() {
        return chooser.getSelected();
    }

    public static void run(AutonMode autonMode) {
        autonMode.init();
        autonMode.run();
    }

}