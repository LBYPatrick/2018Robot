package frc.team5181.sensors;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDP extends PowerDistributionPanel {

    final private static int DEFAULT_CHANEL = 0;

    public PDP(int channel) {
        super(channel);
    }
    public PDP() {
        super(DEFAULT_CHANEL);
    }
  
    @Override
    public double getTotalCurrent() {
        double returnBuffer = 0;
        for(int i = 0; i < 16; ++i) { //16 channels in total I guess ?!
            returnBuffer += this.getCurrent(i);
        }
        return returnBuffer;
    }
}
