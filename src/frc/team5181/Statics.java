package frc.team5181;

public class Statics {
    final static public String DEFAULT_AUTO = "Default";
    final static public String CUSTOM_AUTO  = "My Auto";

    //PWM
    final static public int DRIVE_LF = 0;
    final static public int DRIVE_LB = 1;
    final static public int DRIVE_RF = 2;
    final static public int DRIVE_RB = 3;
    final static public int INTAKE_ARM_MOTORS = 4;
    final static public int INTAKE_ROLLER_MOTORS = 5;
    final static public int INDEX_MOTORS = 6;
    final static public int SHOOTER_MOTORS = 7;
    final static public int INTAKE_SOLENOID_FORWARD = 0;
    final static public int INTAKE_SOLENOID_REVERSE = 1;

    //Analog
    final static public int CAGE_IR_SENSOR = 0;

    //PDP
    public static final int PDP_Motor_LF = 0;
    public static final int PDP_Motor_LB = 1;
    public static final int PDP_Motor_RF = 15;
    public static final int PDP_Motor_RB = 14;
    public static final int PDP_INTAKE_LEFT = 4;
    public static final int PDP_INTAKE_RIGHT = 5;
    public static final int PDP_ROLLER = 9;
    public static final int PDP_INDEX_LEFT = 6;
    public static final int PDP_INDEX_RIGHT = 7;
    final static public int PDP_SHOOTER_LEFT = 12;
    final static public int PDP_SHOOTER_RIGHT = 13;

    final static public int PDP_LED = 8;
    final static public int PDP_VRM2= 10;
    final static public int PDP_VRM3= 11;

    final static public int XBOX_CTRL = 0;

    final static public double LOW_SPD_FACTOR = 0.4;

    //Making this number greater than 1 = overclocking
    final static public double FULL_SPD_FACTOR = 1.0;

    //Will disable everything except drive wheels if setting this to true
    final static public boolean TEST_CHASSIS_MODE = false;

    final static public boolean DEBUG_MODE = false;
}