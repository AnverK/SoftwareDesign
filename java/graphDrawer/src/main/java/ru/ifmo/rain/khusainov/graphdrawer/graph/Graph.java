package ru.ifmo.rain.khusainov.graphdrawer.graph;

import ru.ifmo.rain.khusainov.graphdrawer.api.DrawingApi;

public abstract class Graph {
    protected DrawingApi drawingApi;

    public Graph(DrawingApi drawingApi){
        this.drawingApi = drawingApi;
    }

    public abstract void drawGraph();
}
