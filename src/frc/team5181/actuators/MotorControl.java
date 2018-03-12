package frc.team5181.actuators;

import edu.wpi.first.wpilibj.*;

public class MotorControl {
    public enum Model {
        TALON_SRX,
        VICTOR_SP,
        SPARK,
        VICTOR,
    }
    private PWMSpeedController motor;
    private double speedLimit = 1.0;
    private Model model;
    public static Model DEFAULT_MODEL = Model.VICTOR_SP;
    public static boolean isAnQuanMoShi = true;

    public MotorControl (int port) {
        this(port, DEFAULT_MODEL);
    }

    public MotorControl (int port, Model model) {
        this(port , model, false);
    }

    public MotorControl (int port, boolean isReverse) {
        this(port, DEFAULT_MODEL, isReverse);
    }

    public MotorControl (int port, Model motorModel,boolean isReverse) {
        this.model = motorModel;
        switch(motorModel) {
            case TALON_SRX:
                this.motor = new Talon(port);
                break;
            case VICTOR:
                this.motor = new Victor(port);
                break;
            case VICTOR_SP:
                this.motor = new VictorSP(port);
                break;
            case SPARK:
                this.motor = new Spark(port);
            default : break;
        }
        this.motor.setInverted(isReverse);
    }

    public void updateSpeedLimit(double newSpeedLimit) {
        this.speedLimit = newSpeedLimit;
    }


    public void move(double value) {
        if(!isAnQuanMoShi) this.motor.set(value*speedLimit);
    }

    public void move(boolean forward, boolean reverse) {
        if (forward == reverse) this.move(0);
        else if(forward) this.move(1);
        else this.move(-1);
    }
}
