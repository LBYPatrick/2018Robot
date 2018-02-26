package frc.team5181;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.actuators.*;
import frc.team5181.tasking.Task;
import frc.team5181.sensors.IRSensor;
import frc.team5181.autonomous.*;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
final public class Robot extends IterativeRobot {
	private boolean isLinearAutonOK = false;
	private Task autonCommand;
	private static int rFactor = 1;
	private static double speedFactor = 1.0;
	private static boolean isRVSE = false; // Means "is Reverse Mode Triggered"
	private static boolean isSNP = false;  // Means "is Sniping Mode Triggered"
	private static boolean isSolenoidForward = true;
	private SolenoidControl intakeSoleniod;
	private MotorControl intakeArmMotor;
	private MotorControl intakeRollers;
	private MotorControl indexs;
	private MotorControl shooters;
	private PowerDistributionPanel pdp;
	private IRSensor irCage;
	private boolean isForceUpdateNeeded = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		//this.pdp = new PowerDistributionPanel(0);
		this.irCage = new IRSensor(Statics.CAGE_IR_SENSOR,0.8);
		//Hardware
        DriveTrain.init(Statics.DRIVE_LF,Statics.DRIVE_LB,Statics.DRIVE_RF,Statics.DRIVE_RB);
        Gamepad.init(Statics.XBOX_CTRL);

        if(!Statics.TEST_CHASSIS_MODE) {
			intakeSoleniod = new SolenoidControl(Statics.INTAKE_SOLENOID_FORWARD, Statics.INTAKE_SOLENOID_REVERSE);
			intakeArmMotor = new MotorControl(Statics.INTAKE_ARM_MOTORS, false);
			intakeRollers  = new MotorControl(Statics.INTAKE_ROLLER_MOTORS,false);
			indexs = new MotorControl(Statics.INDEX_MOTORS, false);
			shooters = new MotorControl(Statics.SHOOTER_MOTORS, true);
		}

		AutonHelper.init(shooters); //Pass shooter to AutonHelper
	}

	/**
	 *
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
        AutonChooser.autonChoice = AutonChooser.getAutonCommand();
		AutonHelper.fetchGameData(); // This is a blocking function so...
	}

	@Override
	public void disabledPeriodic() {
		AutonChooser.chooserInit();
	}
	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if (!isLinearAutonOK) {
			isLinearAutonOK = AutonChooser.autonChoice.nextStep();
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		this.teleopControl(true);
		this.postSensorData();
		this.postPDPData();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		this.teleopControl(true);
		this.postSensorData();
		this.postPDPData();
	}

	public void teleopControl(boolean isNFSControl) {
		Gamepad.updateStatus();

		/**
		 * Reverse Gear Trigger (Button Y, AKA "Triangle" in Dualshock 4)
		 */
		if(Gamepad.LB_state) {
			if(Gamepad.current.LB) isRVSE = !isRVSE;
			rFactor = isRVSE ? -1 : 1;
			isForceUpdateNeeded = true;
		}

		/**
		 *  Sniping Mode (First introduced for Team 11319 in FTC 2017 Relic Recovery)
		 *  Use Right Bumper for toggling
		 */
		if(Gamepad.RB_state) {
			if(Gamepad.current.RB) isSNP = !isSNP;
			speedFactor = isSNP ? Statics.LOW_SPD_FACTOR : Statics.FULL_SPD_FACTOR;

			DriveTrain.updateSpeedLimit(speedFactor);
			shooters.updateSpeedLimit(speedFactor);
			intakeArmMotor.updateSpeedLimit(speedFactor);
			intakeRollers.updateSpeedLimit(speedFactor);
			isForceUpdateNeeded = true;
		}
		/**
		 * Soleniod Control using "A" button
		 */
		if(!Statics.TEST_CHASSIS_MODE) {
			if (Gamepad.B_state || isForceUpdateNeeded) {
				if (Gamepad.current.B) isSolenoidForward = !isSolenoidForward;

				intakeSoleniod.move(isSolenoidForward, !isSolenoidForward);
			}

			if (Gamepad.dPad_state || isForceUpdateNeeded) {

				intakeArmMotor.move(Gamepad.current.dPadDown, Gamepad.current.dPadUp);
				intakeRollers.move(Gamepad.current.dPadDown, Gamepad.current.dPadUp);
			}
			if (Gamepad.X_state || isForceUpdateNeeded) {
				indexs.move(Gamepad.current.X, false);
			}

			if (Gamepad.A_state || isForceUpdateNeeded) {
				shooters.move(Gamepad.current.A, false);
			}
		}

		/**
		 *  RC Drive Control
		 */
		if(!isNFSControl && (Gamepad.jLeftY_state || Gamepad.jRightX_state || isForceUpdateNeeded)) {
			DriveTrain.tankDrive(Gamepad.current.jRightX,rFactor*(Gamepad.current.jLeftY));
		}
		/**
		 * NFS Drive Control (Might improve driving experience + less likely wearing out the gearboxes due to rapid speed change)
		 */
		else if(Gamepad.RT_state || Gamepad.LT_state || Gamepad.jLeftX_state || isForceUpdateNeeded) {
			DriveTrain.tankDrive(Gamepad.current.jLeftX*0.5, -rFactor*(Gamepad.current.RT-Gamepad.current.LT));
		}
		
		isForceUpdateNeeded = false;
	}

	public void postSensorData() {
		SmartDashboard.putBoolean("Is cube in cage", this.irCage.isTargetDetected());
	}
	public void postPDPData() {
	    SmartDashboard.putNumber("LF Wheel", pdp.getCurrent(Statics.PDP_Motor_LF));
        SmartDashboard.putNumber("LB Wheel", pdp.getCurrent(Statics.PDP_Motor_LB));
        SmartDashboard.putNumber("RF Wheel", pdp.getCurrent(Statics.PDP_Motor_RF));
        SmartDashboard.putNumber("RB Wheel", pdp.getCurrent(Statics.PDP_Motor_RB));
        SmartDashboard.putNumber("Intake Left", pdp.getCurrent(Statics.PDP_INTAKE_LEFT));
        SmartDashboard.putNumber("Intake Right", pdp.getCurrent(Statics.PDP_INTAKE_RIGHT));
        SmartDashboard.putNumber("Roller",pdp.getCurrent(Statics.PDP_ROLLER));
        SmartDashboard.putNumber("Index Left", pdp.getCurrent(Statics.PDP_INDEX_LEFT));
        SmartDashboard.putNumber("Index Right", pdp.getCurrent(Statics.PDP_INDEX_RIGHT));
        SmartDashboard.putNumber("Shooter Left", pdp.getCurrent(Statics.PDP_SHOOTER_LEFT));
        SmartDashboard.putNumber("Shooter Right", pdp.getCurrent(Statics.PDP_SHOOTER_RIGHT));
        SmartDashboard.putNumber("LED", pdp.getCurrent(Statics.PDP_LED));
        SmartDashboard.putNumber("VRM2", pdp.getCurrent(Statics.PDP_VRM2));
        SmartDashboard.putNumber("VRM3", pdp.getCurrent(Statics.PDP_VRM3));
    }
}

