package frc.team5181.pid;

public enum PIDControllerState {
	
	/** This controller is still in construction. */
	CONSTRUCTING,
	
	/** Once .update() is called, the controller's state will be switched into
	 * RUNNING which will prevent any further change. */
	RUNNING,
	
	/** Once the error is acceptable, the controller's state will be switched
	 * ENDED which will disable functionality of any further call to .update().
	 * Use .revive() to set back to RUNNING mode or use .neverEnd(true) to
	 * prevent entering this state. */
	ENDED

}