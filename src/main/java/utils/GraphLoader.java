package utils;

import algorithms.Edge;
import algorithms.EdgeWeightedGraph;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLoader {

    public static EdgeWeightedGraph loadGraphFromJson(String filename) throws IOException {
        FileReader reader = new FileReader(filename);
        StringBuilder jsonBuilder = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            jsonBuilder.append((char) c);
        }

        JSONObject json = new JSONObject(jsonBuilder.toString());
        JSONArray edgesArray = json.getJSONArray("edges");
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < edgesArray.length(); i++) {
            JSONObject edgeObj = edgesArray.getJSONObject(i);
            int v = edgeObj.getInt("from");
            int w = edgeObj.getInt("to");
            double weight = edgeObj.getDouble("weight");
            edges.add(new Edge(v, w, weight));
        }

        int vertices = json.getInt("vertices");
        EdgeWeightedGraph graph = new EdgeWeightedGraph(vertices);
        for (Edge e : edges) {
            graph.addEdge(e);
        }

        return graph;
    }
}

