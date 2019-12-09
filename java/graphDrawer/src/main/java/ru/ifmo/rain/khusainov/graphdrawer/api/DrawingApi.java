package ru.ifmo.rain.khusainov.graphdrawer.api;

import ru.ifmo.rain.khusainov.graphdrawer.primitives.Point;

public interface DrawingApi {
    int getDrawingAreaWidth();
    int getDrawingAreaHeight();
    void drawCircle(Point center, int radius);
    void drawLine(Point p1, Point p2);
}
