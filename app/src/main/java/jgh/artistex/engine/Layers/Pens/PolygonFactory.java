package jgh.artistex.engine.Layers.Pens;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Polygon Factory class generates lists of vertices making regular polygons and regular stars.
 */
public class PolygonFactory {


    /**
     * Creates a List of <code>PointF</code>s forming a regular polygon.
     *
     * @param vertex number of vertices in the polygon
     * @param x      center x
     * @param y      center y
     * @param radius radius from center to apex point.
     * @return <code>ArrayList</code> of points
     */
    public static ArrayList<PointF> createPolygon(int vertex, float x, float y, float radius) {
        if (vertex < 3) {
            throw new IllegalArgumentException("You must have 3 or more vertices.");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("You need a positive radius.");
        }
        ArrayList<PointF> pointsArray = new ArrayList<PointF>();
        float degrees = 2 * (float) Math.PI / vertex;

        pointsArray.add(new PointF(x, y + radius));// first vertex is bottom
        // point.
        // Polygon will not have flat base.

        for (int i = 1; i < vertex; i++) {
            float nextX;
            float nextY;

            if (i <= ((float) vertex) / 4) {// first quadrant, anticlockwise
                // from bottom.
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y + radius * Math.cos(degrees * i));
            } else if (i <= ((float) vertex) / 2) {// second quadrant
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y + radius * Math.cos(degrees * i));
            } else if (i <= ((float) vertex / 2 * 3)) {// third quadrant
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y + radius * Math.cos(degrees * i));
            } else {// fourth quadrant
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y - radius * Math.cos(degrees * i));
            }
            pointsArray.add(new PointF(nextX, nextY));

        }
        return pointsArray;
    }


    /**
     * Creates a List of <code>PointF</code>s forming a regular star.
     *
     * @param vertex number of vertices in the polygon
     * @param x      center x
     * @param y      center y
     * @param radius radius from center to apex point.
     * @return <code>ArrayList</code> of points
     */
    public static ArrayList<PointF> createShapeStar(int vertex, float x, float y, float radius) {
        if (vertex < 3) {
            throw new IllegalArgumentException("You must have 3 or more vertices.");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("You need a positive radius.");
        }

        ArrayList<PointF> pointsArray = new ArrayList<PointF>();
        float degrees = 2 * (float) Math.PI / vertex;

        pointsArray.add(new PointF(x, y + radius));// first vertex is bottom
        // point.
        // Polygon will not have flat base.

        // get List of vertices in smaller polygon.
        ArrayList<PointF> InnerP = getInnerVertices(vertex, x, y, radius);

        for (int i = 1; i < vertex; i++) {
            float nextX;
            float nextY;
            if (i <= ((float) vertex) / 4) {// first quadrant, anticlockwise
                // from bottom.
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y + radius * Math.cos(degrees * i));
            } else if (i <= ((float) vertex) / 2) {// second quadrant
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y + radius * Math.cos(degrees * i));
            } else if (i <= ((float) vertex / 2 * 3)) {// third quadrant
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y + radius * Math.cos(degrees * i));
            } else {// fourth quadrant
                nextX = (float) (x + radius * Math.sin(degrees * i));
                nextY = (float) (y - radius * Math.cos(degrees * i));
            }

            pointsArray.add(InnerP.get(2 * i - 1));
            pointsArray.add(new PointF(nextX, nextY));

        }
        pointsArray.add(InnerP.get(InnerP.size() - 1));

        return pointsArray;
    }

    /**
     * Private method, used by createShapeStar()
     *
     * @param startPoints
     * @param x
     * @param y
     * @param line
     * @return
     */
    private static ArrayList<PointF> getInnerVertices(int startPoints, float x, float y, float line) {
        int vertex = startPoints * 2;
        float degrees = 2 * (float) Math.PI / vertex;
        line = line / 2;
        ArrayList<PointF> InnerPoints = new ArrayList<PointF>();
        InnerPoints.add(new PointF(x, y + line));

        for (int i = 1; i < vertex; i++) {
            float nextX;
            float nextY;
            if (i <= ((float) vertex) / 4) {// first quadrant, anticlockwise
                // from bottom.
                nextX = (float) (x + line * Math.sin(degrees * i));
                nextY = (float) (y + line * Math.cos(degrees * i));
            } else if (i <= ((float) vertex) / 2) {// second quadrant
                nextX = (float) (x + line * Math.sin(degrees * i));
                nextY = (float) (y + line * Math.cos(degrees * i));
            } else if (i <= ((float) vertex / 2 * 3)) {// third quadrant
                nextX = (float) (x + line * Math.sin(degrees * i));
                nextY = (float) (y + line * Math.cos(degrees * i));
            } else {// fourth quadrant
                nextX = (float) (x + line * Math.sin(degrees * i));
                nextY = (float) (y - line * Math.cos(degrees * i));
            }
            InnerPoints.add(new PointF(nextX, nextY));
        }
        return InnerPoints;
    }
}
