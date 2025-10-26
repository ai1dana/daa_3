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

    public static List<EdgeWeightedGraph> loadGraphFromJson(String filename) throws IOException {
        FileReader reader = new FileReader(filename);
        StringBuilder jsonBuilder = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            jsonBuilder.append((char) c);
        }

        JSONObject json = new JSONObject(jsonBuilder.toString());

        JSONArray graphsArray = json.getJSONArray("graphs");
        List<EdgeWeightedGraph> graphs = new ArrayList<>();

        for (int i = 0; i < graphsArray.length(); i++) {
            JSONObject graphObj = graphsArray.getJSONObject(i);

            JSONArray nodesArray = graphObj.getJSONArray("nodes");
            List<String> nodes = new ArrayList<>();
            for (int j = 0; j < nodesArray.length(); j++) {
                nodes.add(nodesArray.getString(j));
            }

            JSONArray edgesArray = graphObj.getJSONArray("edges");
            List<Edge> edges = new ArrayList<>();
            for (int j = 0; j < edgesArray.length(); j++) {
                JSONObject edgeObj = edgesArray.getJSONObject(j);
                String from = edgeObj.getString("from");
                String to = edgeObj.getString("to");
                double weight = edgeObj.getDouble("weight");

                int fromIndex = nodes.indexOf(from);
                int toIndex = nodes.indexOf(to);

                edges.add(new Edge(fromIndex, toIndex, weight));
            }

            EdgeWeightedGraph graph = new EdgeWeightedGraph(nodes.size());
            for (Edge edge : edges) {
                graph.addEdge(edge);
            }

            graphs.add(graph);
        }

        return graphs;
    }
}
