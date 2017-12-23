package Utils;

import DAO.DataBuffer;
import Model.Earchquake;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static java.lang.Math.*;
import static java.lang.StrictMath.sin;

public class Eckert {
    private static double rad = 4;
    private static double figureWidth = 0;
    private static double figureHeight = 0;
    //The normalize ratio
    private static double ratio = 2.6530121641035085 * 2;

    /**
     * Draw eckert iv projection map
     *
     * @param canvas
     */
    public static void Draw(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double originX = width / 2;
        double originY = height / 2;
        figureWidth = width;
        figureHeight = height;
        context.setStroke(Color.RED);
        ArrayList<Earchquake> earchquakes = DataBuffer.getData();
        double theta;
        double radius;
        for (Earchquake earchquake : earchquakes) {
            context.setLineWidth(getLineWidth(earchquake));
            theta = getTheta(earchquake.getLatitude(), 20);
            radius = getRadius(earchquake);
            context.strokeOval(getX(earchquake.getLongitude(), theta), getY(earchquake.getLatitude(), theta), radius, radius);
        }
    }

    /**
     * Use newton method to get theta
     *
     * @param latitude
     * @param times
     * @return
     */
    private static double getTheta(double latitude, double times) {
        double theta = latitude / 2 * Math.PI / 180;
        double delta;
        double constant = (2 + Math.PI / 2) * Math.sin(latitude * PI / 180);
        while (times-- >= 0) {
            delta = -((theta + Math.sin(theta) * Math.cos(theta) + 2 * Math.sin(theta) - constant) / (2 * Math.cos(theta) * (1 + Math.cos(theta))));
            theta += delta;
        }
        return theta;
    }

    /**
     * Get X
     *
     * @param longitude
     * @param phi
     * @return
     */
    private static double getX(double longitude, double phi) {
        longitude = longitude / 180 * PI;
        return longitude >= 0 ? (longitude - PI) * (1 + cos(phi)) * 0.4222382 * figureWidth / ratio + figureWidth / 2 : (PI + longitude) * (1 + cos(phi)) * 0.4222382 * figureWidth / ratio + figureWidth / 2;
    }

    /**
     * Get Y
     *
     * @param lattitude
     * @param phi
     * @return
     */
    private static double getY(double lattitude, double phi) {
        return lattitude >= 0 ? -1.3265004 * sin(phi) * figureWidth / ratio + figureHeight / 2 : -1.3265004 * sin(phi) * figureWidth / ratio + figureHeight / 2;
    }

    /**
     * Get Radius
     *
     * @param earchquake
     * @return
     */
    private static double getRadius(Earchquake earchquake) {
        return rad * earchquake.getMagnitude();
    }

    /**
     * Get the line width by the earthquake's magnitude
     *
     * @param earchquake
     * @return
     */
    private static double getLineWidth(Earchquake earchquake) {
        return (earchquake.getMagnitude() / 8) * 3;
    }
}
