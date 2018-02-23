package frc.team5181.sensors;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by TylerLiu on 2017/03/14.
 */
public class PDP {
    //TODO the match current stat need to be check before competition for all the following year
    public static PowerDistributionPanel pdp;

    public static void init(){
        pdp = new PowerDistributionPanel();
    }

    public static void outputCurrents(){
    	SmartDashboard.putData(pdp);
    }
}
