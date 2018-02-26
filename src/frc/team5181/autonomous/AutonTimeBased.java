package frc.team5181.autonomous;

public class AutonTimeBased implements Runnable {

    final public static int Left = 0, Middle = 1, Right = 2;

    private int position;

    public AutonTimeBased(int position) {
        this.position = position;
    }

    public void run() {
        try {
            AutonHelper.tankDrive(0,1,500);
            AutonHelper.takeABreak();
            boolean isLeftSide = AutonHelper.isCorrectSwitchLeft();


            switch (this.position) {
                case 0: //Left
                    AutonHelper.report("LEFT SIDE");
                    AutonHelper.tankDrive(0, 1, 500); //Go forward for passing the auto-line
                    AutonHelper.takeABreak();
                    if(isLeftSide) { //Shoot the cube in if it is the correct side
                        AutonHelper.report("CORRECT SWITCH");
                        AutonHelper.tankDrive(1, 0, 180); //Turn Right
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0, 1, 300); //Go forward
                        AutonHelper.takeABreak();
                    }
                    break;
                case 1: // Middle
                        AutonHelper.report("MIDDLE");
                        AutonHelper.tankDrive(isLeftSide? -1 : 1, 0, 180); //Turn Left/Right
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0,1, 300); //Go Forward
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(isLeftSide? 1 : -1, 0,180); //Turn Right/Left
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0,1,300); //Final Forward
                    break;
                case 2: //Right
                    AutonHelper.report("RIGHT SIDE");
                    AutonHelper.tankDrive(0,1,500); //Go forward for passing the auto-line
                    AutonHelper.takeABreak();

                    if(!isLeftSide) {
                        AutonHelper.report("CORRECT SWITCH");
                        AutonHelper.tankDrive(-1, 0, 180); //Turn Left
                        AutonHelper.takeABreak();
                        AutonHelper.tankDrive(0, 1, 300); //Go forward
                        AutonHelper.takeABreak();
                    }
                    break;
                default:
                    AutonHelper.report("Go Forward only");
            }

            AutonHelper.shootCube(1); //Shoot the cube out
            AutonHelper.report("All done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
