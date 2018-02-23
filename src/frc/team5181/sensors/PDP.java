package frc.team5181.sensors;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.Statics;

/**
 * Created by TylerLiu on 2017/03/14.
 */
public class PDP {
    //TODO the match current stat need to be check before competition for all the following year
    public static PowerDistributionPanel pdp;
    public static double[] currents = new double[4];
    public static double[] maxCurrents = new double[4];
    public static double timeStamp =  System.currentTimeMillis();
    public static double maxTotalCurrents = 0.0;

    final static ArrayList<Integer> portList = new ArrayList<>(Arrays.asList(
        Statics.PDP_INDEX_MOTORS,
        Statics.PDP_INTAKE_ARM_MOTORS,
        Statics.PDP_INTAKE_ROLLER_MOTORS,
        Statics.PDP_LED,
        Statics.PDP_Motor_LB,
        Statics.PDP_Motor_LF,
        Statics.PDP_Motor_RB,
        Statics.PDP_Motor_RF,
        Statics.PDP_SHOOTER_MOTORS,
        Statics.PDP_VRM2,
        Statics.PDP_VRM3
    ));

    final static ArrayList<String> deviceList = new ArrayList<>(Arrays.asList(
        "Index Motors",
        "Intake Arm Motors",
        "Intake Roller Motors",
        "LED",
        "LB Drive Motor",
        "LF Drive Motor",
        "RB Drive Motor",
        "RF Drive Motor",
        "Shooter Motors",
        "VRM2",
        "VRM3"
    ));

    public static void init(){
        pdp = new PowerDistributionPanel();
    }

    public static void outputCurrents(){
    	PDP.updateCurrents();
    	PDP.calcMaxCurrents();

        SmartDashboard.putNumber("TOTAL I", pdp.getTotalCurrent());
        SmartDashboard.putNumber("EMF", pdp.getVoltage());

        for(int i = 0; i < portList.size(); ++i) {
            SmartDashboard.putNumber(deviceList.get(i) + " [" + portList.get(i) + "] :", PDP.currents[i]);
        }

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
