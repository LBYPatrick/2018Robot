package frc.team5181.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.team5181.pid.PIDSource;

/**
 * Represents a single IR sensor on the robot. Basically a wrapper around AnalogInput.
 * Designed for Sharp Sensor, GP2Y0A41SK0F
 */
public class IRSensor implements PIDSource {
	public final double cutOffVoltage = 2.5;
	private AnalogInput analogInput;

	public IRSensor(int channel) {
		this.analogInput = new AnalogInput(channel);
	}
	
	public double getRawVoltage() {
		return this.analogInput.getVoltage();
	}

	/**
	 * Get the reading in terms of distance of this IRSensor.
	 * @return The reading in terms of distance of this IRSensor.
	 */ 
	public double readDistance() {
		return voltageToDistance(this.analogInput.getAverageVoltage());
	}

	//TODO determine distance or not?
	public double voltageToDistance(double voltage){
		// Convert voltage data to Distance with a formula
        return 0;
	}

	public boolean isTargetDetected(){
		return this.analogInput.getAverageVoltage() >= cutOffVoltage;
	}

	@Override
	public double pidYield() {
		return this.readDistance();
	}

}
