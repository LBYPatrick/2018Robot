package frc.team5181.pid;

/**
 * When anyone tries to use PIDController in an unexpected manner,
 * throw this Exception to his/her face.
 */
public class PIDControllerException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public PIDControllerException(String message) {
		super(message);
	}
	
}
