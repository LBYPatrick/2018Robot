package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.actuators.DriveTrain;
import frc.team5181.actuators.MotorControl;


public class AutonHelper {

    private static MotorControl shooter;
    private static MotorControl index;
    private static String gameData;
    final public static int DEFAULT_BREAK_TIME = 1700;
    public static boolean isOutputEnabled = false;

    private static int location;

    public static void init(MotorControl shooterMotor, MotorControl indexMotor) {
        shooter = shooterMotor;
        index = indexMotor;
    }

    public static void fetchGameData() {

        //gamedata
        String dataBuffer;
        while(true) {
            dataBuffer = DriverStation.getInstance().getGameSpecificMessage();
            if(dataBuffer.length() > 2) break;
        }

        gameData = dataBuffer;
        report("GameData: " + gameData);

        //Robot Location
        location = DriverStation.getInstance().getLocation() - 1;

    }
    public static boolean isBlueAlliance() {
        return DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue;
    }

    public static int getLocation() {
        return location;
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
            DriveTrain.tankDrive(leftRight, forwardBack);
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
        DriveTrain.tankDrive(0,0.5);
        takeABreak(200); //Make sure we are touching the switch
        shooter.move(speed);
        index.move(speed);
        takeABreak();
        DriveTrain.tankDrive(0,0);
        shooter.move(0);
        index.move(0);

    }

    public static void report(String message) {
        if(isOutputEnabled) { DriverStation.reportWarning(message,false); }
    }
}
