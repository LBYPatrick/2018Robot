package frc.team5181.actuators;

import edu.wpi.first.wpilibj.Spark;
import frc.team5181.actuators.*;

public class RevRGB {

    MotorControl controller;
    final static double minColor = 0.57,
                    maxColor = 0.99;

    public static class Color {
        final public static double red = 0.61,
                                   blue= 0.87;
    }

    public RevRGB (int port) {
        controller = new MotorControl(port,MotorControl.Model.SPARK);
    }

    private static double truncate(double value,int decimals) {
        //Ugly code, but this works
        return ((double)((int)(value * Math.pow(10,decimals)))) / Math.pow(10,decimals);
    }

    public void setColor(int pos) {

        if(pos <= 0 || pos > 100) return;

        final double colorValue = ((pos == 0? 1 : pos) / 100)*maxColor;

        controller.move(truncate(colorValue,2));
    }

    public void setMode(double value) {
        controller.move(value);
    }
}
