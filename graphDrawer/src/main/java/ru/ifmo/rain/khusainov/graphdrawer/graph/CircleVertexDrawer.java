package ru.ifmo.rain.khusainov.graphdrawer.graph;

import ru.ifmo.rain.khusainov.graphdrawer.api.DrawingApi;
import ru.ifmo.rain.khusainov.graphdrawer.primitives.Point;

import java.util.HashMap;
import java.util.Map;

public class CircleVertexDrawer extends Graph {

    public static final double FULL_ANGLE = 6.2831853;

    private int numOfVertices;
    private int vertexRadius;
    private Point drawingAreaCenter;

    protected Map<Integer, Point> vertexToPosition = new HashMap<>();

    public CircleVertexDrawer(int numOfVertices, DrawingApi drawingApi) {
        this(numOfVertices, 20, drawingApi);
    }

    public CircleVertexDrawer(int numOfVertices, int vertexRadius, DrawingApi drawingApi) {
        super(drawingApi);
        this.numOfVertices = numOfVertices;
        this.vertexRadius = vertexRadius;
        drawingAreaCenter = new Point(drawingApi.getDrawingAreaWidth() / 2, drawingApi.getDrawingAreaHeight() / 2);
    }

    protected double getOneVertexArcAngle() {
        return FULL_ANGLE / numOfVertices;
    }

    protected int getExternalRadius() {
        return (int) Math.min(Math.abs(drawingAreaCenter.x - drawingApi.getDrawingAreaWidth() * 0.9),
                Math.abs(drawingAreaCenter.y - drawingApi.getDrawingAreaHeight() * 0.9));
    }

    public void drawGraph() {
        double angleStep = getOneVertexArcAngle();
        double curAngle = 0;
        int externalRadius = getExternalRadius();
        for (int i = 0; i < numOfVertices; ++i) {
            int x = (int) (drawingAreaCenter.x + externalRadius * Math.cos(curAngle));
            int y = (int) (drawingAreaCenter.y - externalRadius * Math.sin(curAngle));
            Point vertexCenter = new Point(x, y);
            vertexToPosition.put(i, vertexCenter);
            drawingApi.drawCircle(vertexCenter, vertexRadius);
            curAngle += angleStep;
        }
    }
}