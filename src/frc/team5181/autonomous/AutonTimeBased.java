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

        boolean isAutoMove = false;

        if(this.mode == Mode.AutoMove) {
            isAutoMove = true;
            this.mode = AutonHelper.getLocation();
        }
        else if(this.mode == Mode.MiddleMove) {
            isAutoMove = true;
            this.mode = Mode.Middle;
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

                    if(isAutoMove) break; //Stop Auton if it is AutoMove

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

                        if(isAutoMove) {
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

                    if(isAutoMove) break;

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
                case Mode.RSCube:
                case Mode.LRAutoline:

                    AutonHelper.tankDrive(0,0.5,300);
                    AutonHelper.takeABreak();
                    if((this.mode == Mode.LSCube && isLeftSide) || (this.mode == Mode.RSCube && !isLeftSide)) {
                        isCubeShootable = true;
                    }
                    else if(this.mode == Mode.LRAutoline) {
                        AutonHelper.tankDrive(0,0.5,300);
                        AutonHelper.takeABreak();
                    }
                    break;

                default:
            }

            if (isCubeShootable && !isAutoMove) {
                AutonHelper.shootCube(1); //Shoot the cube out
                AutonHelper.takeABreak();

            }
            AutonHelper.report("All done.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
