
package frc.team5181.pid;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team5181.Gamepad;
import frc.team5181.actuators.DriveTrain;
import frc.team5181.sensors.WheelEncoders;

/**
 * Created by TylerLiu on 2017/03/01.
 * may be used in the future
 */

public class PIDTuning2 {

    private static PIDController controller;
    private static boolean POVPressed;
    private static double currentP;
    private static double currentD;
    private static double lastConvP;
    private static double lastconvD;
    private static boolean isReverse;

    private static double incFactor = 1.0;

    public static void translationalInit(){
        isReverse = false;
        WheelEncoders.reset();
        controller = new PIDController();
        POVPressed = false;
        controller
                .setSource(WheelEncoders.getDistanceReader())
                .useTranslator(reading -> (isReverse ? -1 : 1) * 100 - reading)
                .neverEnd(true)
                .withAcceptableError(1.0)
                .withPIDConstant(0.1, 0, 0)
                .outputTo(DriveTrain.getTranslationalAdjuster(), error -> error / 100);
        currentP = 0.1;
        currentD = 0;
    }

    public static void tuningPeriodic(){
        controller.update();
        switch (Gamepad.D_PAD_STATE) {
            case -1:
                POVPressed = false;
                break;
            case 0://convergent
                if (POVPressed) break;
                POVPressed = true;
                isReverse = !isReverse;
                lastConvP = currentP;
                lastconvD = currentD;
                currentP *= 1 + incFactor;
                DriverStation.reportWarning("Increase P Current: " + currentP + " LastConvergent: " + lastConvP + "", false);
                DriveTrain.tankDrive(0,0);
                WheelEncoders.reset();
                controller.reset();
                controller.withProportionalConstant(currentP);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                }
                break;
            case 90:
                if (POVPressed) break;
                POVPressed = true;
                isReverse = !isReverse;
                if (currentD == 0) currentD = 0.02;
                else currentD *= 1 + incFactor;
                DriverStation.reportWarning("Increase D Current: " + currentD + " LastConvD: " + lastconvD + "", false);
                DriveTrain.tankDrive(0,0);
                WheelEncoders.reset();
                controller.reset();
                controller.withPIDConstant(currentP, 0, 0);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                }
                break;
            case 180:
                if (POVPressed) break;
                POVPressed = true;
                isReverse = !isReverse;
                incFactor /= 2;
                currentP = lastConvP;
                currentD = lastconvD;
                currentP *= 1 + incFactor;
                DriverStation.reportWarning("Increase P Current: " + currentP + " LastConvergent: " + lastConvP + "", false);
                DriveTrain.tankDrive(0,0);
                WheelEncoders.reset();
                controller.reset();
                controller.withProportionalConstant(currentP);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                }
                break;

        }

    }

    public static void rotationalInit(){
        controller = new PIDController();
        POVPressed = false;
        controller
                .setSource(WheelEncoders.getTurnReader())
                .useTranslator(reading -> (isReverse ? -1 : 1) * Math.PI / 3 - reading)
                .neverEnd(true)
                .withAcceptableError(Math.PI / 20)
                .withPIDConstant(0.1, 0, 0)
                .outputTo(DriveTrain.getAngleAdjuster());
        currentP = 0.1;
        currentD = 0;
    }
}