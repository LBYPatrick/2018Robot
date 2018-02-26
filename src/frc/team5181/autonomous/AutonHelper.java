package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team5181.actuators.DriveTrain;
import frc.team5181.actuators.MotorControl;


public class AutonHelper {

    private static MotorControl shooter;
    private static String gameData;
    final public static int DEFAULT_BREAK_TIME = 2000;
    public static boolean isOutputEnabled = true;

    public static void init(MotorControl shooterMotor) {
        shooter = shooterMotor;
    }

    public static void fetchGameData() {
        String dataBuffer;
        while(true) {
            dataBuffer = DriverStation.getInstance().getGameSpecificMessage();
            if(dataBuffer.length() > 2) break;
        }

        gameData = dataBuffer;
        report("GameData: " + gameData);
    }

    /**
     * A simplified method for detecting correct switch position
     * @return whether the correct switch is on left
     */

    public static boolean isCorrectSwitchLeft() {


        switch(gameData.charAt(0)) { //SWITCH_NEAR
            case 'L' :
                report("LEFT SWITCH");
                return true;
            case 'R' :
                report("RIGHT SWITCH");
                return false;
            default  :
                report("UNKNOWN SWTICH");
                return false;
        }
    }

    public static void tankDrive(double leftRight, double forwardBack, int millisecond) {
        try {
            report("Moving at speed of (" + leftRight + ", " + forwardBack + ") for" + millisecond + " ms");
            DriveTrain.tankDrive(leftRight, -forwardBack);
            Thread.sleep(millisecond);
            DriveTrain.tankDrive(0,0);
            report("Moving done");

        }catch(Exception e) { e.printStackTrace(); }
    }

    public static void takeABreak(){ takeABreak(DEFAULT_BREAK_TIME); }

    public static void takeABreak(int millisecond) {
        try {
            report("Idle for " + millisecond + " ms");
            Thread.sleep(millisecond);
            report("Woke up");

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void shootCube(double speed) {
        report("Shooting cube at speed of " + speed);
        shooter.move(speed);
        takeABreak();
        shooter.move(0);

    }

    public static void report(String message) {
        if(isOutputEnabled) { DriverStation.reportWarning(message,false); }
    }
}
