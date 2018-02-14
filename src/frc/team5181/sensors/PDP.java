package frc.team5181.sensors;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.Statics;

/**
 * Created by TylerLiu on 2017/03/14.
 */
public class PDP {
    //TODO set CAN ID as 0
    //TODO the match current stat need to be check before competition for all the following year
    public static PowerDistributionPanel pdp;
    public static double[] currents = new double[4];
    public static double[] maxCurrents = new double[4];
    public static double timeStamp =  System.currentTimeMillis();
    public static double maxTotalCurrents = 0.0;

    public static void init(){
        pdp = new PowerDistributionPanel(Statics.CAN_PDP);
    }

    public static void outputCurrents(){
    	PDP.updateCurrents();
    	PDP.calcMaxCurrents();
        SmartDashboard.putNumber("TOTAL I", pdp.getTotalCurrent());
        SmartDashboard.putNumber("EMF", pdp.getVoltage());
        SmartDashboard.putNumber("MOTOR I (12)", PDP.currents[0]);
        SmartDashboard.putNumber("MOTOR I (13)", PDP.currents[1]);
        SmartDashboard.putNumber("MOTOR I (14)", PDP.currents[2]);
        SmartDashboard.putNumber("MOTOR I (15)", PDP.currents[3]);
        SmartDashboard.putNumber("MOTOR I (12) MAX AMP", PDP.maxCurrents[0]);
        SmartDashboard.putNumber("MOTOR I (13) MAX AMP", PDP.maxCurrents[1]);
        SmartDashboard.putNumber("MOTOR I (14) MAX AMP", PDP.maxCurrents[2]);
        SmartDashboard.putNumber("MOTOR I (15) MAX AMP", PDP.maxCurrents[3]);
        SmartDashboard.putNumber("MOTOR TOTAL MAX AMP", PDP.maxTotalCurrents);
    }
    
    public static void calcMaxCurrents(){
    	if (PDP.timeStamp <= 5.0){
    		for (int i = 0; i < 5 ;i ++ ){
    			if (PDP.currents[i]>PDP.maxCurrents[i]){
    				maxTotalCurrents += (PDP.currents[i] - PDP.maxCurrents[i]);
    				PDP.maxCurrents[i] = PDP.currents[i];
    			}
    		}
    	}else if (PDP.timeStamp > 5.0){
    		PDP.timeStamp = System.currentTimeMillis();
    		PDP.maxCurrents = new double[5];
    	}
    }
    
    
    
    public static void updateCurrents(){
    	currents[0] = pdp.getCurrent(Statics.PDP_Motor_RF);
    	currents[1] = pdp.getCurrent(Statics.PDP_Motor_RB);
    	currents[2] = pdp.getCurrent(Statics.PDP_Motor_LF);
    	currents[3] = pdp.getCurrent(Statics.PDP_Motor_LB);
    }
}
