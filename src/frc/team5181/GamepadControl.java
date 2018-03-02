package frc.team5181;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.team5181.sensors.LSProfiler;

final public class GamepadControl { //Why final? Because then Java would try to final every method it can, which is faster.

    private LSProfiler p = new LSProfiler("Gamepad");
    private boolean isDebug = false;

    public enum Mode {
        GAMEPAD,
        JOYSTICK
    }

    public void setDebugMode(boolean value) {isDebug = value;}

     public class ValueContainer {
        //Gamepad
        public int      dPad = -1;
        public double  jLeftX = 0,
                       jLeftY = 0,
                       jRightX = 0,
                       jRightY = 0,
                       LT = 0,
                       RT = 0;

        public boolean jLeft = false,
                       jRight = false,
                       A = false,
                       B = false,
                       X = false,
                       Y = false,
                       LB = false,
                       RB = false,
                       back = false,
                       start = false,
                       dPadUp = false,
                       dPadDown = false,
                       dPadLeft = false,
                       dPadRight = false;


        //Joystick
        public boolean js_trigger = false;
        public double js_x = 0,
                      js_y = 0;

        //POV
        public int POV = -1;

        public void writeValue(ValueContainer another) {
            this.dPad = another.dPad;
            this.jLeftX = another.jLeftX;
            this.jLeftY = another.jLeftY;
            this.jRightX = another.jRightY;
            this.jRightY = another.jRightY;
            this.LT = another.LT;
            this.RT = another.RT;
    
            this.jLeft = another.jLeft;
            this.jRight = another.jRight;
            this.A = another.A;
            this.B = another.B;
            this.X = another.X;
            this.Y = another.Y;
            this.LB = another.LB;
            this.RB = another.RB;
            this.back = another.back;
            this.start = another.start;
            this.dPadUp = another.dPadUp;
            this.dPadDown = another.dPadDown;
            this.dPadLeft = another.dPadLeft;
            this.dPadRight = another.dPadRight;
    
            //Joystick
            this.js_trigger = another.js_trigger;
            this.js_x = another.js_x;
            this.js_y = another.js_y;
        }
    }

    //===================
    //Variables
    //===================

    public ValueContainer  previous = new ValueContainer(),
                                           current = new ValueContainer();
    private ValueContainer cleanContainer = new ValueContainer();

    private  Mode CONTROLLER_MODE;
    private  int portNumber,
                       xStickPort,
                       yStickPort,
                       triggerPort;

    private  XboxController xGP;
    private  Joystick jJS;

    //Gamepad
     public boolean jLeftX_state = false,
              jLeftY_state = false, 
              jRightX_state = false, 
              jRightY_state = false, 
              jLeft_state = false, 
              jRight_state = false, 
              dPad_state = false, 
              A_state = false, 
              B_state = false, 
              X_state = false, 
              Y_state = false, 
              LB_state = false, 
              RB_state = false, 
              LT_state = false, 
              RT_state = false, 
              back_state = false, 
              start_state = false, 

    //Joystick
              js_trigger_state = false, 
              js_x_state = false, 
              js_y_state = false,

    //POV
              POV_state = false,
              dPadUp_state = false,
              dPadDown_state = false,
              dPadLeft_state = false,
              dPadRight_state = false;

    
    //TODO redo mapping

    //=============================
    //Methods
    //=============================

     public GamepadControl(int xboxPort) {
        portNumber= xboxPort;
        CONTROLLER_MODE = Mode.GAMEPAD;
        xGP = new XboxController(portNumber);
    }
    
     public GamepadControl(int jStickPort, int xPort, int yPort, int trigPort) {

        CONTROLLER_MODE = Mode.JOYSTICK;
        
        portNumber = jStickPort;
        xStickPort = xPort;
        yStickPort = yPort;
        triggerPort = trigPort;
        
        jJS = new Joystick(portNumber);
    }

     public void reset() {
        current.writeValue(cleanContainer);
        previous.writeValue(cleanContainer);
        updateStatus();
    }

     public void updateStatus() {
        if(isDebug) p.start();
        //Get Data
        switch(CONTROLLER_MODE) {
            case GAMEPAD :
                current.jLeftX  = xGP.getX(Hand.kLeft);
                current.jLeftY  = xGP.getY(Hand.kLeft);
                current.jRightX = xGP.getX(Hand.kRight);
                current.jRightY = xGP.getY(Hand.kRight);
                current.A       = xGP.getAButton();
                current.B       = xGP.getBButton();
                current.X       = xGP.getXButton();
                current.Y       = xGP.getYButton();
                current.LB      = xGP.getBumper(Hand.kLeft);
                current.RB      = xGP.getBumper(Hand.kRight);
                current.back    = xGP.getBackButton();
                current.start   = xGP.getStartButton();
                current.jLeft   = xGP.getStickButton(Hand.kLeft);
                current.jRight  = xGP.getStickButton(Hand.kRight);
                current.LT      = xGP.getTriggerAxis(Hand.kLeft);
                current.RT      = xGP.getTriggerAxis(Hand.kRight);
                current.dPad    = xGP.getPOV();

                if (current.dPad >= 0) {
                    current.dPadRight = (current.dPad >= 45 && current.dPad <= 135);
                    current.dPadDown = (current.dPad >= 135 && current.dPad <= 225);
                    current.dPadLeft = (current.dPad >= 225 && current.dPad <= 315);
                    current.dPadUp = current.dPad == 315 || current.dPad <= 45;
                } else { //The DPAD was not even used
                    current.dPadUp = false;
                    current.dPadDown = false;
                    current.dPadLeft = false;
                    current.dPadRight = false;
                }
                break;
            case JOYSTICK :
                current.LT         = xGP.getTriggerAxis(Hand.kLeft);
                current.RT         = xGP.getTriggerAxis(Hand.kRight);
                current.js_trigger = jJS.getRawButton(triggerPort);
                current.js_x       = jJS.getRawAxis(xStickPort);
                current.js_y       = jJS.getRawAxis(yStickPort);
                current.POV        = jJS.getPOV();
                break;
            default : break; // tan90 boys
        }

        // Compare Data for state

        switch(CONTROLLER_MODE) {
            case GAMEPAD :
                jLeftX_state = current.jLeftX != previous.jLeftX;
                jLeftY_state = current.jLeftY != previous.jLeftY;
                jRightX_state = current.jRightX != previous.jRightX;
                jRightY_state = current.jRightY != previous.jRightY;
                jLeft_state = current.jLeft != previous.jLeft;
                jRight_state = current.jRight != previous.jRight;
                dPad_state =  current.dPad != previous.dPad;
                A_state = current.A != previous.A;
                B_state = current.B != previous.B;
                X_state = current.X != previous.X;
                Y_state = current.Y != previous.Y;
                LB_state = current.LB != previous.LB;
                RB_state = current.RB != previous.RB;
                LT_state = current.LT != previous.LT;
                RT_state = current.RT != previous.RT;
                back_state = current.back != previous.back;
                start_state = current.start != previous.start;
                break;
            case JOYSTICK :
                js_trigger_state = current.js_trigger != previous.js_trigger;
                js_x_state = current.js_x != previous.js_x;
                js_y_state = current.js_y != previous.js_y;
                POV_state = current.POV != previous.POV;
                break;
            default : break; // tan90 boys
        }
        previous.writeValue(current);
        if(isDebug) DriverStation.reportWarning(p.toString(),false);
    }

}
