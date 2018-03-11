package frc.team5181;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.actuators.*;
import frc.team5181.tasking.Task;
import frc.team5181.sensors.*;
import frc.team5181.autonomous.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
final public class Robot extends IterativeRobot {

    //Software

    private static boolean          isLinearAutonOK = false;
	private static Task             autonCommand;
	private static int              rFactor = 1;
    private static LSProfiler       pdpProfiler;
	private static double           speedFactor = 1.0;
    private static boolean          isForceUpdateNeeded = false;
    private static boolean          isDriftMode = false;
	private static boolean          isRVSE = false; // Means "is Reverse Mode Triggered"
	private static boolean          isSNP = false;  // Means "is Sniping Mode Triggered"

    //Hardware
    private static boolean          isSolenoidForward = true;
	private static SolenoidControl  intakeSoleniod;
	private static MotorControl     intakeArmMotor;
	private static MotorControl     intakeRoller;
	private static MotorControl     indexs;
	private static MotorControl     shooters;
	private static PDP              pdp;
	private static IRSensor         irCage;
	private static UsbCamera        frontCam;
	private static GamepadControl   gp1;
	private static GamepadControl   gp2;
	
	private int speedSwitch = 0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		//frontCam = CameraServer.getInstance().startAutomaticCapture();
		this.pdp = new PDP(0);
		this.irCage = new IRSensor(Statics.CAGE_IR_SENSOR,0.8);
		//Hardware
        DriveTrain.init(Statics.DRIVE_LF,Statics.DRIVE_LB,Statics.DRIVE_RF,Statics.DRIVE_RB);

        gp1 = new GamepadControl(Statics.XBOX_CTRL_1);
        gp2 = new GamepadControl(Statics.XBOX_CTRL_2);

        if(!Statics.TEST_CHASSIS_MODE) {
			intakeSoleniod 	= new SolenoidControl	(Statics.INTAKE_SOLENOID_FORWARD, Statics.INTAKE_SOLENOID_REVERSE);
			intakeArmMotor 	= new MotorControl		(Statics.INTAKE_ARM_MOTORS,false);
			intakeRoller  	= new MotorControl		(Statics.INTAKE_ROLLER_MOTORS,false);

			indexs 			= new MotorControl		(Statics.INDEX_MOTORS, false);
			shooters 		= new MotorControl		(Statics.SHOOTER_MOTORS, true);

			intakeRoller.updateSpeedLimit(0.2);
		}

        if(Statics.DEBUG_MODE) {
        	pdpProfiler = new LSProfiler("PDP-SmartDashboard");
        	AutonHelper.isOutputEnabled = true;
        	gp1.setDebugMode(true);

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
		AutonHelper.fetchGameData();
		isLinearAutonOK = false;
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
			AutonChooser.getSelected().run();
			isLinearAutonOK = true;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopInit(){
		if(!Statics.DEBUG_MODE) {
			AutonHelper.isOutputEnabled = false;
		}
	}

	@Override
	public void teleopPeriodic() {
		gp1.updateStatus();
		gp2.updateStatus();
		this.speedControl();
		this.driveControl(true);
		this.shooterControl();
		isForceUpdateNeeded = false;
	}


	@Override
	public void testInit() {
		AutonHelper.isOutputEnabled = true;
	}
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
        gp1.updateStatus();
		gp2.updateStatus();
		this.speedControl();
	    this.driveControl(true);
	    this.shooterControl();
		if(Statics.DEBUG_MODE) {
			this.postSensorData();
			this.postPDPData();
		}
		isForceUpdateNeeded = false;
	}

	public void speedControl() {
		if(gp1.Y_state && gp1.current.Y) {
			switch(speedSwitch) {
				case 0 : speedFactor = 0.1;
						 break;
				case 1 : speedFactor = 0.3;
						 break;
				case 2 : speedFactor = 0.5;
						 break;
				case 3 : speedFactor = 0.7;
						 break;
				case 4 : speedFactor = 0.9;
						 break;
				case 5:  speedFactor = 1.0;
						 break;		 	
				default: speedFactor = 0.1;
						 speedFactor = -1;	 	 		 		 		 
			}

			DriveTrain.     updateSpeedLimit(speedFactor);
			shooters.       updateSpeedLimit(speedFactor);
			intakeArmMotor. updateSpeedLimit(speedFactor);
			intakeRoller.   updateSpeedLimit(speedFactor);
			indexs.         updateSpeedLimit(speedFactor);

			speedSwitch += 1;
			isForceUpdateNeeded = true;
		}
	}

	public void driveControl(boolean isNFSControl) {

		/**
		 * Reverse Gear Trigger (Button Y, AKA "Triangle" in Dualshock 4)
		 */
		if(gp1.LB_state) {
			if(gp1.current.LB) isRVSE = !isRVSE;
			rFactor = isRVSE ? -1 : 1;
			isForceUpdateNeeded = true;
		}

		/**
		 *  Sniping Mode (First introduced for Team 11319 in FTC 2017 Relic Recovery)
		 *  Use Right Bumper for toggling
		 */
		if(gp1.RB_state) {
			if(gp2.current.RB) isSNP = !isSNP;
			speedFactor = isSNP ? Statics.LOW_SPD_FACTOR : Statics.FULL_SPD_FACTOR;

			DriveTrain.     updateSpeedLimit(speedFactor);
			shooters.       updateSpeedLimit(speedFactor);
			intakeArmMotor. updateSpeedLimit(speedFactor);
			intakeRoller.   updateSpeedLimit(speedFactor);
			indexs.         updateSpeedLimit(speedFactor);
			isForceUpdateNeeded = true;
		}

		/**
		 *  RC Drive Control
		 */

		//Let us drive an AE86 today, not the robot
		if (gp1.X_state && gp1.current.X) {
			DriveTrain.tankDrive(0,0);
			isDriftMode = true;
		}
		else if(!isNFSControl && (gp1.jLeftY_state || gp1.jRightX_state || isForceUpdateNeeded || isDriftMode)) {
			DriveTrain.tankDrive(gp1.current.jRightX,rFactor*(gp1.current.jLeftY));
			isDriftMode = false;
		}
		/**
		 * NFS Drive Control (Might improve driving experience + less likely wearing out the gearboxes due to rapid speed change)
		 */
		else if(gp1.RT_state || gp1.LT_state || gp1.jLeftX_state || isForceUpdateNeeded || isDriftMode) {
			DriveTrain.tankDrive(gp1.current.jLeftX*0.5, -rFactor*(gp1.current.RT-gp1.current.LT));
			isDriftMode = false;
		}
		
	}

	public void shooterControl() {
		if(gp2.RT_state || gp2.LT_state || isForceUpdateNeeded) {

		    final double forwardValue = gp2.current.RT - gp2.current.LT;
			intakeRoller.move(forwardValue > 0,forwardValue < 0);
			intakeArmMotor.move(forwardValue > 0, forwardValue < 0);
		}

		if(gp2.dPadUp_state || gp2.dPadDown_state || isForceUpdateNeeded) {
			intakeSoleniod.move(gp2.current.dPadUp,gp2.current.dPadDown);
		}

		if(gp2.jLeftY_state || isForceUpdateNeeded) {
			indexs.move(gp2.current.jLeftY > 0, gp2.current.jLeftY < 0);
		}

		if(gp2.jRightY_state || isForceUpdateNeeded) {
            shooters.move(gp2.current.jRightY > 0, gp2.current.jRightY < 0);
        }

	}

	public void postSensorData() {
		SmartDashboard.putBoolean("Is cube in cage", this.irCage.isTargetDetected());
	}
	public void postPDPData() {
		pdpProfiler.start();
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
		DriverStation.reportWarning(pdpProfiler.toString(), false);
    }
}

