package frc.team5181.actuators;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;

public class SolenoidControl {

    Compressor compressor;
    DoubleSolenoid solenoid;

    public SolenoidControl(int compressorPort, int soleniodForwardPort, int solenoidReversePort) {
        this.compressor = new Compressor(compressorPort);
        this.solenoid = new DoubleSolenoid(soleniodForwardPort, solenoidReversePort);
    }

    public void move(boolean forward, boolean reverse) {
        if((forward && reverse) || !(forward || reverse)) solenoid.set(DoubleSolenoid.Value.kOff);
        else if (forward) solenoid.set(DoubleSolenoid.Value.kForward);
        else solenoid.set(DoubleSolenoid.Value.kReverse);
    }
}
