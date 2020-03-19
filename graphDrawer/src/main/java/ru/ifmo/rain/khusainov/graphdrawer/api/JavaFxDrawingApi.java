package ru.ifmo.rain.khusainov.graphdrawer.api;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.ifmo.rain.khusainov.graphdrawer.primitives.Circle;
import ru.ifmo.rain.khusainov.graphdrawer.primitives.Line;
import ru.ifmo.rain.khusainov.graphdrawer.primitives.Point;

import java.util.ArrayList;
import java.util.List;

public class JavaFxDrawingApi extends Application implements DrawingApi {

    public static int DEFAULT_WIDTH = 800;
    public static int DEFAULT_HEIGHT = 800;

    private static int drawingWidth;
    private static int drawingHeight;

    private static List<Circle> circlesToDraw = new ArrayList<>();
    private static List<Line> linesToDraw = new ArrayList<>();

    public JavaFxDrawingApi() {
        super();
        drawingWidth = DEFAULT_WIDTH;
        drawingHeight = DEFAULT_HEIGHT;
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Canvas canvas = new Canvas(drawingWidth, drawingHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < circlesToDraw.size(); ++i) {
            Circle circle = circlesToDraw.get(i);
            Point leftTop = circle.getRectangleLeftTop();
            int radius = circle.getRadius();
            gc.setFill(Color.BLACK);
            gc.strokeOval(leftTop.x, leftTop.y, radius * 2, radius * 2);
            gc.setFill(Color.BLACK);
            gc.fillText(i + 1 + "", leftTop.x + radius, leftTop.y + radius);
        }
        gc.setFill(Color.BLACK);
        linesToDraw.forEach(line -> {
            Point p1, p2;
            p1 = line.getP1();
            p2 = line.getP2();
            gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
        });
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public int getDrawingAreaWidth() {
        return drawingWidth;
    }

    @Override
    public int getDrawingAreaHeight() {
        return drawingHeight;
    }

    @Override
    public void drawCircle(Point center, int radius) {
        circlesToDraw.add(new Circle(center, radius));
    }

    @Override
    public void drawLine(Point p1, Point p2) {
        linesToDraw.add(new Line(p1, p2));
    }

}