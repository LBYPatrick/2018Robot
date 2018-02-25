package frc.team5181.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.actuators.DriveTrain;
import frc.team5181.actuators.MotorControl;

/**
 * Thank Jaci Brunning for writing the getOwnedSide() method
 */

public class AutonHelper {


    private static MotorControl shooter;
    /**
     * Game features including the SWITCH and SCALE. Near denotes close
     * to the alliance wall and FAR denotes furthest from the alliance
     * wall
     */
    public enum GameFeature {
        SWITCH_NEAR, SCALE, SWITCH_FAR
    }

    /**
     * OwnedSide defines which side (from the perspective of the alliance
     * station) is owned by the alliance during the match. This is one of
     * LEFT, RIGHT or UNKNOWN (the latter in the case where game data
     * is not yet made available by the FMS or DS)
     */
    public enum OwnedSide {
        UNKNOWN, LEFT, RIGHT, NOT_MATCH
    }

    public static void init(MotorControl shooterMotor) {
        shooter = shooterMotor;
    }

    /**
     * Determine the OwnedSide of any given GameFeature. Use this method to
     * determine which PLATE of each feature (SCALE or SWITCH) is OWNED by your
     * ALLIANCE. Perspectives are referenced relative to your ALLIANCE STATION.
     *
     * @param feature The feature to get the owned side for. See GameFeature.
     * @return  The Owned Side (PLATE) of the feature. See OwnedSide. Make sure to
     *          check for UNKNOWN.
     */
    public static OwnedSide getOwnedSide(GameFeature feature) {
        if (!DriverStation.getInstance().isFMSAttached()) return OwnedSide.NOT_MATCH;
        String gsm = DriverStation.getInstance().getGameSpecificMessage();

        // If the length is less than 3, it's not valid. Longer than 3 is permitted, but only
        // the first 3 characters are taken.
        
        DriverStation.reportWarning(gsm,false);
        if (gsm == null || gsm.length() < 3) return OwnedSide.UNKNOWN;

        char gd = gsm.charAt(feature.ordinal());
        switch (gd) {
            case 'L':
            case 'l': return OwnedSide.LEFT;
            case 'R':
            case 'r': return OwnedSide.RIGHT;
            default: return OwnedSide.UNKNOWN;
        }
    }


    /**
     * Pat, stop messing with Tyler's great code. I won't even touch them
     * @return whether the Left side in the NEAR switch is for us
     */
    public static boolean isLeftSideOwned() {
        return getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT;
    }

    /**
     *
     * @param leftRight   xVal ( > 0 ==> clockwise || < 0 ==> reverse-clockwise)
     * @param forwardBack yVal ( > 0 ==> forward   || < 0 ==> back)
     * @param millisecond duration
     */
    final public static int DEFAULT_BREAK_TIME = 2000;

    public static void tankDrive(double leftRight, double forwardBack, int millisecond) {
        try {

            DriveTrain.tankDrive(leftRight, -forwardBack);
            Thread.sleep(millisecond);
            DriveTrain.tankDrive(0,0);

        }catch(Exception e) { e.printStackTrace(); }
    }

    public static void takeABreak(){ takeABreak(DEFAULT_BREAK_TIME); }

    public static void takeABreak(int millisecond) {
        try {

            Thread.sleep(millisecond);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void shootCube(double speed) {
        shooter.move(speed);
        takeABreak(1000);
        shooter.move(0);
    }
}
