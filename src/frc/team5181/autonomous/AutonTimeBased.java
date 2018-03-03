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
                MiddleMove = 7;
    };

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

        }

        try {
            AutonHelper.tankDrive(0,0.5,1000);
            AutonHelper.takeABreak();
            boolean isLeftSide = AutonHelper.isCorrectSwitchLeft();



            switch (this.mode) {
                case  Mode.Left:
                    AutonHelper.report("LEFT SIDE");
                    AutonHelper.tankDrive(0, 1, 500); //Go forward for passing the auto-line
                    AutonHelper.takeABreak();

                    if(isMoveOnly) break; //Stop Auton if it is AutoMove

                    if(isLeftSide) { //Shoot the cube in if it is the correct side
                        AutonHelper.report("CORRECT SWITCH");
                        AutonHelper.tankDrive(1, 0, 180); //Turn Right
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0, 1, 300); //Go forward
                        AutonHelper.takeABreak();
                        isCubeShootable = true;
                    }
                    break;
                case Mode.Middle:
                        AutonHelper.report("MIDDLE");
                        AutonHelper.tankDrive(isLeftSide? -1 : 1, 0, 180); //Turn Left/Right
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0,1, 300); //Go Forward
                        AutonHelper.takeABreak();

                        if(isMoveOnly) {
                            AutonHelper.tankDrive(0,1,300);
                            AutonHelper.takeABreak();
                        }

                        AutonHelper.tankDrive(isLeftSide? 1 : -1, 0,180); //Turn Right/Left
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0,1,300); //Final Forward
                        isCubeShootable = true;
                    break;
                case Mode.Right:
                    AutonHelper.report("RIGHT");
                    AutonHelper.tankDrive(0,1,500); //Go forward for passing the auto-line
                    AutonHelper.takeABreak();

                    if(isMoveOnly) break;

                    if(!isLeftSide) {
                        AutonHelper.report("CORRECT SWITCH");
                        AutonHelper.tankDrive(-1, 0, 180); //Turn Left
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0, 1, 300); //Go forward
                        AutonHelper.takeABreak();
                        isCubeShootable = true;
                    }
                    break;
                case Mode.LSCube:
                    AutonHelper.tankDrive(0,0.5,300);
                    AutonHelper.takeABreak();
                    if(isLeftSide) { isCubeShootable = true; }
                    break;

                case Mode.RSCube:
                    AutonHelper.tankDrive(0,0.5,300);
                    AutonHelper.takeABreak();
                    if(isLeftSide) { isCubeShootable = true; }
                    break;

            }

            if (isCubeShootable) {
                AutonHelper.shootCube(1); //Shoot the cube out
                AutonHelper.takeABreak();

            }
            AutonHelper.report("All done.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
