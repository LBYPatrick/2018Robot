package frc.team5181.pid;

import frc.team5181.Gamepad;
import frc.team5181.actuators.DriveTrain;
import frc.team5181.sensors.WheelEncoders;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Created by TylerLiu on 2017/02/27.
 * may be used in the future
 */


public class PIDTuning {
    private static PIDController controller;
    private static boolean POVPressed;
    private static double current;
    private static double lastConvergent;
    private static double lastDivergent;
    private static boolean isReverse;

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
                .withPIDConstant(0.5, 0, 0)
                .outputTo(DriveTrain.getTranslationalAdjuster(), error -> error / 100);
        current = 0.5;
        lastDivergent = -1;
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
                lastConvergent = current;
                if (lastDivergent < 0) current = lastConvergent * 2;
                else current = (lastConvergent + lastDivergent) / 2;
                DriverStation.reportWarning("Convergent,Current: " + current + ", LastConvergent: " + lastConvergent + "\n", false);
                DriveTrain.tankDrive(0,0);
                WheelEncoders.reset();
                controller.reset();
                controller.withPIDConstant(current, 0, 0);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                }
                break;
            case 180:
                if (POVPressed) break;
                POVPressed = true;
                isReverse = !isReverse;
                lastDivergent = current;
                current = (lastConvergent + lastDivergent) / 2;
                DriverStation.reportWarning("Divergent,Current: " + current + ",LastConvergent: " + lastConvergent + ",", false);
                DriveTrain.tankDrive(0,0);
                WheelEncoders.reset();
                controller.reset();
                controller.withPIDConstant(current, 0, 0);
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
                .neverEnd(true)
                .withAcceptableError(Math.PI / 20)
                .withPIDConstant(1, 0, 0)
                .outputTo(DriveTrain.getAngleAdjuster());
        current = 0.1;
        lastDivergent = -1;
    }

}