package ru.ifmo.rain.khusainov.graphdrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphReader {
    public static List<List<Integer>> readMatrixGraph() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            graph.add(new ArrayList<>());
            for (int j = 0; j < n; ++j) {
                int edge = in.nextInt();
                if (edge == 1) {
                    graph.get(i).add(j);
                }
            }
        }
        return graph;
    }

    public static List<List<Integer>> readListOfEdgesGraph() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < m; ++i) {
            int u, v;
            u = in.nextInt();
            v = in.nextInt();
            graph.get(u-1).add(v-1);
        }
        return graph;
    }
}
