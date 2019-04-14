package frc.robot;

public class Auto_Align_Algorithm {
    private double measuredTotalBarLength = 0; // See Document:"Auto Align Algorithm", Page 3 Text in Purple
    private double x_o = 0;                    // See Document:"Auto Align Algorithm", Page 4 Text in Purple
    private double y_o = 0;                    // See Document:"Auto Align Algorithm", Page 4 Text in Purple
    public Auto_Align_Algorithm()
    {
        // Constructor Empty
    }
    public double getMotorPercentage(double kInput, double bInput, double visibleBarLengthInput)
    {
        // warning ! : All length units inside this function are all in centimeters.
        // Warning ! : All angle  units inside this function are all in radians.
        boolean approachFromLeft = true;
        if(kInput < 0)
        {
            approachFromLeft = false;
            kInput = -kInput;
            bInput = 2 * y_o - bInput;
        }
        double sin = kInput / Math.sqrt(1 + Math.pow(kInput,2));
        double cos = 1 / Math.sqrt(1 + Math.pow(kInput,2));
        double l1 = measuredTotalBarLength + y_o * sin + (x_o + 65.04) * cos - bInput * sin - visibleBarLengthInput;
        double l2 = bInput * cos - y_o * cos + (x_o + 65.04) * sin;
        double y = l1;
        double n = (16 / Math.pow(kInput,2) * Math.pow(l2,2) - 2 * Math.pow(l2,2) * Math.pow(l2,2) +
                Math.sqrt(Math.pow((2 * Math.pow(l1,2) * l2 - 16 / Math.pow(kInput,2) * Math.pow(l2,3)),2) -
                (64 / Math.pow(kInput,2) - 4) * Math.pow(l2,2) * (4 / Math.pow(kInput,2) * Math.pow(l2,4) -
                Math.pow(l1,2) * Math.pow(l2,2)))) / ((32 / Math.pow(kInput,2) - 2) * Math.pow(l2,2));
        double a = Math.sqrt(Math.pow(l1,2) / (2 * n * l2 - Math.pow(l2,2)));
        double x1_deri = (-2 * y) / (Math.pow(a,2) * Math.sqrt(Math.pow(n,2) - Math.pow((y / a),2)));
        double y_new = y + 0.01;
        double x2_deri = (-2 * y_new) / (Math.pow(a,2) * Math.sqrt(Math.pow(n,2) - Math.pow((y_new / a),2)));
        double theta_delta = Math.atan(x2_deri) - Math.atan(x1_deri);
        double l_delta = Math.sqrt(Math.pow((Math.sqrt(Math.pow(n,2) -Math.pow((y_new / a),2)) -
                Math.sqrt(Math.pow(n,2) - Math.pow((y / a),2))),2)+ 0.0001);
        double s_delta = theta_delta * 33.27785;
        double ratio;
        if(approachFromLeft)
        {
            ratio = (l_delta - s_delta) / (l_delta + s_delta);
        }
        else
        {
            ratio = 1 / ((l_delta - s_delta) / (l_delta + s_delta));
        }
        return ratio;
    }
    // 24.4 cm from center of the cam pic to robot front beam.
}
