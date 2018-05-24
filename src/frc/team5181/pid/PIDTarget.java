package frc.team5181.pid;

@FunctionalInterface
public interface PIDTarget {
	/**
	 * When called, the associated actuator should use the amount this
	 * method provided as a parameter to adjust to the desired value.
	 * @param amount The amount which associated actuator should use in
	 * order to adjust to the desired value.
	 */
    void pidAdjust(double amount);
}
