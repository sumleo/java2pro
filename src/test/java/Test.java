import java.util.Arrays;
import java.util.function.Function;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.sin;

public class Test {
    double x = 720;
    double ratio = 2.6530121641035085;

    private double getTheta(double latitude, double times) {
        double theta = latitude / 2 * Math.PI / 180;
        double delta;
        double constant = (2 + Math.PI / 2) * Math.sin(latitude * PI / 180);
        while (times-- >= 0) {
            delta = -((theta + sin(theta) * cos(theta) + 2 * sin(theta) - constant) / (2 * cos(theta) * (1 + cos(theta))));
            theta += delta;
        }
        return theta;
    }

    private double getX(double longitude, double phi) {
        longitude = longitude / 180 * PI;
        return longitude >= 0 ? (longitude - PI) * (1 + cos(phi)) * 0.42224 * x / 5.3 + x / 2 : (PI + longitude) * (1 + cos(phi)) * 0.42224 * x / 5.3 + x / 2;
    }

    private double getY(double lattitude, double phi) {
        return lattitude >= 0 ? -1.32650 * sin(phi) * x / 5.3 : 1.32650 * sin(phi) * x / 5.3 + x / 4;
    }

    @org.junit.Test
    public void test() {
        double latitude = -90;
        double longitude = -0.000001;
        double theta = getTheta(latitude, 50);
        System.out.println(">>>XXX:" + ((getX(longitude, theta))));
        System.out.println(">YYY:" + ((getY(latitude, theta))));
    }
}
