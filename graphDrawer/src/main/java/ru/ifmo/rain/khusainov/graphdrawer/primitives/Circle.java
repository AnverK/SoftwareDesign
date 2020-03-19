package ru.ifmo.rain.khusainov.graphdrawer.primitives;

public class Circle {
    private Point rectangleLeftTop;
    private int radius;

    public Circle(Point center, int radius) {
        this.rectangleLeftTop = new Point(center.x - radius, center.y - radius);
        this.radius = radius;
    }

    public Point getRectangleLeftTop() {
        return rectangleLeftTop;
    }

    public int getRadius() {
        return radius;
    }
}
