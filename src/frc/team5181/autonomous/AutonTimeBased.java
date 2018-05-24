package frc.team5181.autonomous;

public class AutonTimeBased implements Runnable {

    final public static class Mode {
        final public static int Left = 0,
                Middle = 1,
                Right = 2,
                LSCube = 3,
                RSCube = 4,
                LRAutoline = 5,
                AutoMove = 6,
                MiddleMove = 7,
                FullAuto  = 8;
    };

    //time
    final public static int TIME_SIDE_AUTOLINE             = 1700,
                            TIME_MIDDLE_HALF_AUTOLINE      = 350,
                            TIME_TURN_90                   = 200,
                            TIME_LRCUBE_FORWARD     	   = 3000,
                            TIME_AFTER_TURN                = 800;

    //speed
    final public static double SPEED_SIDE_DRIVE_SWITCH      = 0.5,
    						   SPEED_MIDDLE_DRIVE_SWITCH    = 0.5,
    						   SPEED_IMPACT                 = 0.35;

    private int mode;

    public AutonTimeBased(int mode) {
        this.mode = mode;
    }

    public void run() {

        boolean isCubeShootable = false;
        boolean isMoveOnly = false;

        //Process assisted modes
        switch(this.mode) {
            case Mode.LRAutoline:

                isMoveOnly = true;
                this.mode = Mode.Left;
                break;

            case Mode.AutoMove:

                isMoveOnly = true;
                this.mode = AutonHelper.getLocation();
                break;

            case Mode.MiddleMove:

                isMoveOnly = true;
                this.mode = Mode.Middle;
                break;
            case Mode.FullAuto:
                this.mode = AutonHelper.getLocation();

        }

        try {
            final boolean isSwitchLeft = AutonHelper.isCorrectSwitchLeft();
            final boolean isStartFromLeft = (mode == Mode.Left); //Just to make things clear



            switch (mode) {
                case  Mode.Left:
                case  Mode.Right: //Side
                    AutonHelper.report(isStartFromLeft ? "LEFT SIDE" : "RIGHT SIDE");
                    AutonHelper.tankDrive(0,0.5,TIME_SIDE_AUTOLINE);
                    AutonHelper.takeABreak();

                    if(isMoveOnly) break; //Stop Auton if it is AutoMove

                    if(isStartFromLeft == isSwitchLeft) { //Shoot the cube in if it is the correct side
                        AutonHelper.report("CORRECT SWITCH");
                        AutonHelper.tankDrive(isStartFromLeft ? 1 : -1, 0, TIME_TURN_90); //Turn Right/Left
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0, SPEED_SIDE_DRIVE_SWITCH, TIME_AFTER_TURN*3/2); //Go forward
                        isCubeShootable = true;
                    }
                    break;
                case Mode.Middle:

                        AutonHelper.tankDrive(0,0.5,TIME_MIDDLE_HALF_AUTOLINE);
                        AutonHelper.report("MIDDLE");
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(isSwitchLeft? -1 : 1, 0, TIME_TURN_90); //Turn Left/Right
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0,0.4, isSwitchLeft? TIME_AFTER_TURN: TIME_AFTER_TURN * 8 / 10); //Go Forward
                        AutonHelper.takeABreak();

                        if(isMoveOnly) {
                            AutonHelper.tankDrive(0,0.5,300);
                       }

                        AutonHelper.tankDrive(isSwitchLeft? 1 : -1, 0,TIME_TURN_90); //Turn Right/Left
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0,SPEED_MIDDLE_DRIVE_SWITCH,1200); //Final Forward
                        isCubeShootable = true;
                    break;
                case Mode.LSCube:
                case Mode.RSCube:
                    AutonHelper.tankDrive(0,SPEED_IMPACT,TIME_LRCUBE_FORWARD);
                    //AutonHelper.takeABreak();
                    if(isSwitchLeft == isStartFromLeft) { isCubeShootable = true; }
                    break;
            }

            if (isCubeShootable) {
                AutonHelper.takeABreak();
                AutonHelper.shootCube(0.75); //Shoot the cube out
                AutonHelper.takeABreak();

            }
            AutonHelper.report("All done.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
