package frc.team5181.sensors;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDP extends PowerDistributionPanel {

    public PDP(int channel) {
        super(channel);
    }

    @Override
    public double getTotalCurrent() {return 0;}
}
