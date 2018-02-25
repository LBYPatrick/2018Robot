package frc.team5181.autonomous;

import frc.team5181.actuators.DriveTrain;
import sun.plugin2.message.Message;

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
            boolean isLeftSide = AutonHelper.isLeftSideOwned();

            //Stop moving if the side is not what we want for dropping the cube in & Asked to move only
            if((this.position == 0 && !isLeftSide)
                    || (this.position == 2 && isLeftSide) || this.position > 2) {

                if(this.position == 1) return;

                AutonHelper.tankDrive(0,1,500);

                return;
            }


            switch (this.position) {
                case 0: //Left
                    AutonHelper.tankDrive(0,1,500); //Go forward
                    AutonHelper.takeABreak();

                    AutonHelper.tankDrive(1,0,180); //Turn Right
                    AutonHelper.takeABreak();

                    AutonHelper.tankDrive(0,1,300); //Go forward
                    AutonHelper.takeABreak();
                    break;
                case 1: // Middle
                    if(isLeftSide) {
                        AutonHelper.tankDrive(-1, 0, 180); //Turn Left
                        AutonHelper.takeABreak();

                        AutonHelper.tankDrive(0,1, 300); //Go Forward
                        AutonHelper.takeABreak();

                        AutonHelper.tankDrive(1, 0,180); //Turn Right
                        AutonHelper.takeABreak();

                        AutonHelper.tankDrive(0,1,300); //Final Forward
                    }
                    break;
                case 2: //Right
                    AutonHelper.tankDrive(0,1,500); //Go forward
                    AutonHelper.takeABreak();

                    AutonHelper.tankDrive(-1,0,180); //Turn Left
                    AutonHelper.takeABreak();

                    AutonHelper.tankDrive(0,1,300); //Go forward
                    AutonHelper.takeABreak();
                    break;
            }

            AutonHelper.shootCube(1); //Shoot the cube out

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
