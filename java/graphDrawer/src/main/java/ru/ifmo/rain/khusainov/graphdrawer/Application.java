package ru.ifmo.rain.khusainov.graphdrawer;

import ru.ifmo.rain.khusainov.graphdrawer.api.AwtDrawingApi;
import ru.ifmo.rain.khusainov.graphdrawer.api.DrawingApi;
import ru.ifmo.rain.khusainov.graphdrawer.api.JavaFxDrawingApi;
import ru.ifmo.rain.khusainov.graphdrawer.graph.Graph;
import ru.ifmo.rain.khusainov.graphdrawer.graph.GraphDrawer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Application {
    public static void main(String... args) {
        if (args.length != 2) {
            System.out.println("Usage:java Drawer <library> <storage_type>");
            return;
        }
        String library = args[0];
        String storageType = args[1];

        DrawingApi api;
        switch (library) {
            case "awt":
                api = new AwtDrawingApi(600, 600);
                break;
            case "javafx":
                api = new JavaFxDrawingApi();
                break;
            default:
                System.out.println("Only \"awt\" and \"javafx\" libraries are supported now");
                return;
        }
        List<List<Integer>> g;
        switch (storageType) {
            case "matrix":
                g = GraphReader.readMatrixGraph();
                break;
            case "edges":
                g = GraphReader.readListOfEdgesGraph();
                break;
            default:
                System.out.println("Only \"matrix\" and \"edges\" types of storage are supported now");
                return;
        }
        Graph graph = new GraphDrawer(g, api);
        graph.drawGraph();
        if (library.equals("awt")) {
            AwtDrawingApi frameApi = (AwtDrawingApi) api;
            frameApi.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
            });
            frameApi.setSize(frameApi.getDrawingAreaWidth(), frameApi.getDrawingAreaHeight());
            frameApi.setVisible(true);
            frameApi.setResizable(false);
        } else {
            javafx.application.Application.launch(JavaFxDrawingApi.class);
        }
    }

}
