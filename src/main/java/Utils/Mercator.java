package Utils;

import DAO.DataBuffer;
import Model.Earchquake;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Mercator {
    private static double rad = 4;

    /**
     * Draw the mercator picture
     *
     * @param canvas
     */
    public static void Draw(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double originX = width / 2;
        double originY = height / 2;
        double increaseX = width / 360;
        double increaseY = height / 180;
        context.setStroke(Color.RED);
        ArrayList<Earchquake> earchquakes = DataBuffer.getData();
        for (Earchquake earchquake : earchquakes) {
            context.setLineWidth(getLineWidth(earchquake));
            context.strokeOval(getX(earchquake, originX, increaseX), getY(earchquake, originY, increaseY), getRadius(earchquake), getRadius(earchquake));
        }
    }

    /**
     * Get the Y coordinate
     *
     * @param earchquake
     * @param originY
     * @param increaseY
     * @return
     */
    private static double getY(Earchquake earchquake, double originY, double increaseY) {
        return -earchquake.getLatitude() * increaseY + originY;
    }

    /**
     * Get Radius by magnitude
     *
     * @param earchquake
     * @return
     */
    private static double getRadius(Earchquake earchquake) {
        return rad * earchquake.getMagnitude();
    }

    /**
     * Get X coordinate
     *
     * @param earchquake
     * @param originX
     * @param increaseX
     * @return
     */
    private static double getX(Earchquake earchquake, double originX, double increaseX) {
        double x = earchquake.getLongitude();
        if (x < 0) {
            return (180 + x) * increaseX + originX;
        } else {
            return -(180 - x) * increaseX + originX;
        }
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
