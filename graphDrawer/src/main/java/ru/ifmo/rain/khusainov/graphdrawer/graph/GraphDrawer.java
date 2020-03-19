package ru.ifmo.rain.khusainov.graphdrawer.graph;

import ru.ifmo.rain.khusainov.graphdrawer.api.DrawingApi;

import java.util.List;

public class GraphDrawer extends CircleVertexDrawer {
    List<List<Integer>> graph;

    public GraphDrawer(List<List<Integer>> graph, DrawingApi drawingApi) {
        super(graph.size(), drawingApi);
        this.graph = graph;
    }

    public void drawGraph() {
        super.drawGraph();
        for (int i = 0; i < graph.size(); ++i) {
            for (int j = 0; j < graph.get(i).size(); j++) {
                drawingApi.drawLine(vertexToPosition.get(i), vertexToPosition.get(graph.get(i).get(j)));
            }
        }
    }
}
