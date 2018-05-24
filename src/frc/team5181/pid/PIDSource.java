package frc.team5181.pid;

@FunctionalInterface
public interface PIDSource {
	/**
	 * When called, this method should return the current reading of
	 * the associated sensor.
	 * @return Current reading of the associated sensor
	 */
    double pidYield();
}
