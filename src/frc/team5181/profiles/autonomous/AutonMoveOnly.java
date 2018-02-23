package frc.team5181.profiles.autonomous;

import frc.team5181.autonomous.AutonMode;
import frc.team5181.actuators.*;

public class AutonMoveOnly implements AutonMode{

    private int startPostion = 1;

    public AutonMoveOnly(int position) {

        //Out-of-range
        if(!(position > 3 || position < 1)) this.startPostion = position;
    }

    public void init() {}

    public void run()  {
        try {

            //Go straight
            DriveTrain.tankDrive(0, 1);
            Thread.sleep(2000);
            DriveTrain.tankDrive(0,0);

            //Turn
            switch(this.startPostion) {
                case 1 : DriveTrain.tankDrive(1,0); break;
                case 2 : return;
                case 3 : DriveTrain.tankDrive(-1,0); break;
                default : return;
            }
            Thread.sleep(500);
            DriveTrain.tankDrive(0,0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
