package frc.team5181.gamepad;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.team5181.sensors.LSProfiler;

final public class GamepadB { //Why final? Because then Java would try to final every method it can, which is faster.

    final private static int NUM_PRECISE_KEY = 6;
    final private static int NUM_NORMAL_KEY = 14;
    private LSProfiler p = new LSProfiler("GamepadB");
    private boolean isDebug = false;

    final public static int jLeftX = 0,
                jLeftY = 1,
                jRightX = 2,
                jRightY = 3,
                LT = 4,
                RT = 5,
                jLeftDown = 6,
                jRightDown = 7,
                A = 8,
                B = 9,
                X = 10,
                Y = 11,
                LB = 12,
                RB = 13,
                back = 14,
                start = 15,
                dPadUp = 16,
                dPadDown = 17,
                dPadLeft = 18,
                dPadRight = 19;

    private ValueContainer previous = new ValueContainer();
    private int portNumber;
    private XboxController xGP;
    public boolean[] state = new boolean[NUM_NORMAL_KEY + NUM_PRECISE_KEY];


    public void setDebugMode(boolean value) {
        isDebug = value;
    }
    public class ValueContainer {

        //Gamepad
        public int dPad = -1;

        public double[] preciseKey = new double[NUM_PRECISE_KEY];
        public boolean[] normalkey = new boolean[NUM_NORMAL_KEY];


        public ValueContainer() {
            for (int i = 0; i < NUM_PRECISE_KEY; ++i) { preciseKey[i] = 0; }
            for (int i = 0; i < NUM_NORMAL_KEY; ++i) { normalkey[i] = false; }
        }
    }

    public GamepadB(int xboxPort) {
        portNumber = xboxPort;
        xGP = new XboxController(portNumber);
        for(int i = 0; i < state.length; ++i) { state[i] = false; }
    }

    public boolean isKeyChanged(int key) {
        return state[key];
    }

    public boolean isKeyHeld(int key) {
        return getValue(key) > 0;
    }

    public boolean isGamepadChanged() {
        for(boolean i : state) {
            if(i) return true;
        }
        return false;
    }

    public double getValue(int key) {
        if(key < NUM_PRECISE_KEY) return previous.preciseKey[key];
        else return previous.normalkey[key-NUM_PRECISE_KEY]? 1 : 0;
    }

    public boolean isKeyToggled(int key) {
        return isKeyChanged(key) && (getValue(key) > 0);
    }

    public void updateStatus() {

        ValueContainer current = new ValueContainer();

        if (isDebug) p.start();

        //data Collection
        current.preciseKey[jLeftX] = xGP.getX(Hand.kLeft);
        current.preciseKey[jLeftY] = xGP.getY(Hand.kLeft);
        current.preciseKey[jRightX] = xGP.getX(Hand.kRight);
        current.preciseKey[jRightY] = xGP.getY(Hand.kRight);
        current.preciseKey[LT] = xGP.getTriggerAxis(Hand.kLeft);
        current.preciseKey[RT] = xGP.getTriggerAxis(Hand.kRight);
        current.normalkey[A - NUM_PRECISE_KEY] = xGP.getAButton();
        current.normalkey[B - NUM_PRECISE_KEY] = xGP.getBButton();
        current.normalkey[X - NUM_PRECISE_KEY] = xGP.getXButton();
        current.normalkey[Y - NUM_PRECISE_KEY] = xGP.getYButton();
        current.normalkey[LB - NUM_PRECISE_KEY] = xGP.getBumper(Hand.kLeft);
        current.normalkey[RB - NUM_PRECISE_KEY] = xGP.getBumper(Hand.kRight);
        current.normalkey[back - NUM_PRECISE_KEY] = xGP.getBackButton();
        current.normalkey[start - NUM_PRECISE_KEY] = xGP.getStartButton();
        current.normalkey[jLeftDown - NUM_PRECISE_KEY] = xGP.getStickButton(Hand.kLeft);
        current.normalkey[jRightDown - NUM_PRECISE_KEY] = xGP.getStickButton(Hand.kRight);
        current.dPad = xGP.getPOV();

        if (current.dPad >= 0) {
            current.normalkey[dPadRight - NUM_PRECISE_KEY] = (current.dPad >= 45 && current.dPad <= 135);
            current.normalkey[dPadDown - NUM_PRECISE_KEY] = (current.dPad >= 135 && current.dPad <= 225);
            current.normalkey[dPadLeft - NUM_PRECISE_KEY] = (current.dPad >= 225 && current.dPad <= 315);
            current.normalkey[dPadUp - NUM_PRECISE_KEY] = current.dPad == 315 || current.dPad <= 45;
        } else { //The DPAD was not even used
            current.normalkey[dPadUp - NUM_PRECISE_KEY] = false;
            current.normalkey[dPadDown - NUM_PRECISE_KEY] = false;
            current.normalkey[dPadLeft - NUM_PRECISE_KEY] = false;
            current.normalkey[dPadRight - NUM_PRECISE_KEY] = false;
        }

        //State Comparison

        for(int i = 0; i < NUM_PRECISE_KEY; ++i) {
            state[i] = current.preciseKey[i] != previous.preciseKey[i];
        }

        for(int i = 0; i < NUM_NORMAL_KEY; ++i) {
            state[i + NUM_PRECISE_KEY] = current.normalkey[i] != previous.normalkey[i];
        }
        
        previous = current;
        if (isDebug) DriverStation.reportWarning(p.toString(), false);
    }
}
