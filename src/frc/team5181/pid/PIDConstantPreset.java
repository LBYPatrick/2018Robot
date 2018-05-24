package frc.team5181.pid;

/**
 * Created by TylerLiu on 2017/03/14.
 */
public enum PIDConstantPreset {
    DriveTranslational  (2.025, 0, 0.2278),
    DriveRotational     (1.6, 0, 0.02),
    DriveOneside        (1.6, 0, 0.02),
    ZieglerTrans        (0.3375, 0.675, 0.1125),
    ZieglerRot          (0.32, 0.64, 0.1067);
    double P, I, D;

    PIDConstantPreset (double P, double I, double D){
        this.P = P;
        this.I = I;
        this.D = D;
    }


    public double getP() {
        return P;
    }

    public double getI() {
        return I;
    }

    public double getD() {
        return D;
    }
}
