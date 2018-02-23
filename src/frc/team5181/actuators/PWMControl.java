package frc.team5181.actuators;

import edu.wpi.first.wpilibj.PWM;

final public class PWMControl {
    private PWM controlObj;

    public PWMControl(int portNumber) {
        this.controlObj = new PWM(portNumber);
    }

    public void moveBySpeed(double speed) {
        this.controlObj.setSpeed(speed);
    }

    public void moveByPosition(double position) {
        this.controlObj.setPosition(position);
    }
}
