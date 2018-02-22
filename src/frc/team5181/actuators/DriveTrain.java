package frc.team5181.actuators;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.pid.PIDTarget;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by TylerLiu on 2018/01/22.
 */
public class DriveTrain {
    private static double speedLimit = 1.0;
    private static MotorControl LF_Motor;
    private static MotorControl LB_Motor;
    private static MotorControl RF_Motor;
    private static MotorControl RB_Motor;
    private static boolean is4WD;
    final private static MotorControl.Model DEFAULT_MODEL = MotorControl.Model.SPARK;

    /**
     * 4WD Constructor
     * @param LFMotorPort
     * @param LBMotorPort
     * @param RFMotorPort
     * @param RBMotorPort
     * @param modelList    list of motor models (From back to front, left to right)
     */
    public static void init(int LFMotorPort,
                            int LBMotorPort,
                            int RFMotorPort,
                            int RBMotorPort,
                            ArrayList<MotorControl.Model> modelList) {
        is4WD = true;
        LB_Motor = new MotorControl(LBMotorPort,modelList.get(0),false);
        RB_Motor = new MotorControl(RBMotorPort,modelList.get(1),true);
        LF_Motor = new MotorControl(LFMotorPort,modelList.get(2),false);
        RF_Motor = new MotorControl(RFMotorPort,modelList.get(3), true);
    }

    /**
     * Overloaded 4WD Constructor, uses DEFAULT_MODEL
     * @param LFMotorPort
     * @param LBMotorPort
     * @param RFMotorPort
     * @param RBMotorPort
     */
    public static void init(int LFMotorPort, int LBMotorPort,int RFMotorPort, int RBMotorPort) {

        init(LFMotorPort,
                LBMotorPort,
                RFMotorPort,
                RBMotorPort,
                new ArrayList<MotorControl.Model>(
                        Arrays.asList(DEFAULT_MODEL,DEFAULT_MODEL,DEFAULT_MODEL,DEFAULT_MODEL)
                )
        );
    }

    /**
     * 2WD Constructor
     * @param leftMotorPort
     * @param rightMotorPort
     * @param leftModel
     * @param rightModel
     */
    public static void init(int leftMotorPort, int rightMotorPort, MotorControl.Model leftModel, MotorControl.Model rightModel) {
        is4WD = false;
        LB_Motor = new MotorControl(leftMotorPort, leftModel,false);
        RB_Motor = new MotorControl(rightMotorPort, rightModel,true);
    }

    /**
     * Overloaded 2WD Constructor,uses DEFAULT_MODEL
     * @param leftMotorPort
     * @param rightMotorPort
     */
    public static void init(int leftMotorPort, int rightMotorPort) {
        is4WD = false;
        LB_Motor = new MotorControl(leftMotorPort, DEFAULT_MODEL,false);
        RB_Motor = new MotorControl(rightMotorPort, DEFAULT_MODEL,true);
    }

    /**
     * tankDrive method
     *
     * @param xVal  x value of the stick (For rotation)
     * @param yVal  y value of the stick (For forward & back)
     */
    public static void tankDrive(double xVal, double yVal) {
        xVal = -xVal; // FTC 2018 tuning (Probably Also for FRC 2018? LOL)

        //Calculate Adequate Power Level for motors
        final double leftPower  = clipValue(yVal + xVal, -1.0, 1.0);
        final double rightPower = clipValue(yVal - xVal, -1.0, 1.0);

        //Pass calculated power level to motors
        LB_Motor.move(getLimitedSpeed(leftPower));
        RB_Motor.move(getLimitedSpeed(rightPower));

        if(is4WD) {
            LF_Motor.move(getLimitedSpeed(leftPower));
            RF_Motor.move(getLimitedSpeed(rightPower));
        }
    }

    /**
     *
     * mecanumDrive method
     *
     * @param sideMove     value for sideMove, ranging from -1 to 1
     * @param forwardBack  value for moving forward and back, ranging also from -1 to 1
     * @param rotation     value for robot rotation, ranging from -1 to 1
     */
    public void mecanumDrive(double sideMove, double forwardBack, double rotation) {

        forwardBack = -forwardBack;
        sideMove = -sideMove;

        //A little Math from https://ftcforum.usfirst.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
        double r = Math.hypot(sideMove, forwardBack);
        double robotAngle = Math.atan2(forwardBack, sideMove) - Math.PI / 4;

        LF_Motor.move(getLimitedSpeed(r * Math.cos(robotAngle) + rotation));
        RF_Motor.move(getLimitedSpeed(r * Math.sin(robotAngle) - rotation));
        LB_Motor.move(getLimitedSpeed(r * Math.sin(robotAngle) + rotation));
        RB_Motor.move(getLimitedSpeed(r * Math.cos(robotAngle) - rotation));
    }

    /**
     *
     * @param   a     the original speed
     * @return  the limited speed
     */
    private static double getLimitedSpeed(double a) {
        return a*speedLimit;
    }

    /**
     * Updates the speed limit
     *
     * @param newSpeedLimit the new speed limit
     */
    public static void updateSpeedLimit(double newSpeedLimit) {
        speedLimit = newSpeedLimit;
    }

    /**
     *
     * @param value the original value
     * @param min   the minimum value allowed in the range
     * @param max   the maximum value allowed in the range
     * @return      the value within the range
     */
    private static double clipValue(double value, double min, double max) {
        if(value >= max) return max;
        else if (value <= min) return min;
        else return value;
    }

    private static double mapSpeed(double amount) {
        return (Math.abs(amount) < 0.001) ? 0 : (amount > 0 ? 1 : -1) * (Math.min(0.7, Math.abs(amount) * 0.4 + 0.4));
    }

    public static PIDTarget getTranslationalAdjuster() {
        return amount -> {
            double mapped = mapSpeed(amount);
            //System.out.println("Amount: " + amount + " & Mapped: " + mapped); // TODO: REMOVE THIS
            //drive.tankDrive(mapped, mapped);
            tankDrive(mapped, mapped /*+ 0.05*/);
        };
    }

    public static PIDTarget getAngleAdjuster() {// left as positive!
        return amount -> {
            double mapped = mapSpeed(amount);
            tankDrive(-mapped, mapped);
        };
    }

    public static PIDTarget getLeftAdjuster() {
        return amount -> tankDrive(-mapSpeed(amount), 0);
    }

    public static PIDTarget getRightAdjuster() {
        return amount -> tankDrive(0, mapSpeed(amount));
    }

    public static PIDTarget getOneSideAdjuster(boolean isLeft) {
        return isLeft ? getLeftAdjuster() : getRightAdjuster();
    }
}
