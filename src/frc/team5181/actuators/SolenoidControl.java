package frc.team5181.actuators;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team5181.autonomous.AutonHelper;
import edu.wpi.first.wpilibj.Compressor;

public class SolenoidControl {

    Compressor compressor;
    DoubleSolenoid solenoid;

    public SolenoidControl(int soleniodForwardPort, int solenoidReversePort) {
        this.compressor = new Compressor();
        this.solenoid = new DoubleSolenoid(soleniodForwardPort, solenoidReversePort);
    }

    public void move(boolean forward, boolean reverse) {
        if(forward == reverse) {return;}
        else if (forward) {solenoid.set(DoubleSolenoid.Value.kForward); AutonHelper.report("Solenoid Forward");}
        else {solenoid.set(DoubleSolenoid.Value.kReverse);AutonHelper.report("Soleniod Reverse");}
    }

    @Override
    public String toString() {
        return "Pressure Status: " + (this.compressor.getPressureSwitchValue() ? "Good":"Too High")
                + "\tCurrent: " + this.compressor.getCompressorCurrent() + "A";
    }
}
