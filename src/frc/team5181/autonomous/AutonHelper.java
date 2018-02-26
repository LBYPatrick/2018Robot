package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team5181.actuators.DriveTrain;
import frc.team5181.actuators.MotorControl;
import frc.team5181.tasking.Task;


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
            default:
                report("UNKNOWN SWITCH");
                return false;
        }
    }

    public static class TimedDrive implements Task {
        double leftRight;
        double forwardBack;
        int millisecond;
        long startTime;

        public TimedDrive(double leftRight, double forwardBack, int millisecond){
            this.leftRight = leftRight;
            this.forwardBack = forwardBack;
            this.millisecond = millisecond;
            this.startTime = System.currentTimeMillis();
        }

        public TimedDrive(int millisecond){
            this(0, 0,millisecond);
        }

        public TimedDrive(){
            this(DEFAULT_BREAK_TIME);
        }

        public boolean nextStep(){
            if (System.currentTimeMillis() < startTime + millisecond) {
                DriveTrain.tankDrive(leftRight, -forwardBack);
                return false;
            } else {
                DriveTrain.tankDrive(0, 0);
                return true;
            }
        }

    }

    public static class shootCube implements Task{
        double speed;
        int millisecond;
        long startTime;

        public shootCube(double speed, int millisecond){
            this.speed = speed;
            this.millisecond = millisecond;
            this.startTime = System.currentTimeMillis();
        }

        public shootCube(double speed){
            this(speed, DEFAULT_BREAK_TIME);
        }

        public boolean nextStep(){
            if (System.currentTimeMillis() < startTime + millisecond) {
                shooter.move(speed);
                return false;
            } else {
                shooter.move(0);
                return true;
            }
        }

    }

    public static void report(String message) {
        if(isOutputEnabled) { DriverStation.reportWarning(message,false); }
    }
}
