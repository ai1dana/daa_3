package utils;

import algorithms.Edge;
import algorithms.EdgeWeightedGraph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLoader {

    public static List<EdgeWeightedGraph> loadGraphFromJson(String filename) throws IOException {
        List<EdgeWeightedGraph> graphs = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            JSONObject root = new JSONObject(new JSONTokener(reader));
            JSONArray graphArray = root.getJSONArray("graphs");

            for (int i = 0; i < graphArray.length(); i++) {
                JSONObject graphObj = graphArray.getJSONObject(i);

                JSONArray nodeArray = graphObj.getJSONArray("nodes");
                JSONArray edgeArray = graphObj.getJSONArray("edges");

                List<String> nodes = new ArrayList<>();
                for (int n = 0; n < nodeArray.length(); n++) {
                    nodes.add(nodeArray.getString(n));
                }

                EdgeWeightedGraph graph = new EdgeWeightedGraph(nodes.size(), nodes);

                for (int e = 0; e < edgeArray.length(); e++) {
                    JSONObject edgeObj = edgeArray.getJSONObject(e);

                    String from = edgeObj.getString("from");
                    String to = edgeObj.getString("to");
                    double weight = edgeObj.getDouble("weight");

                    int fromIndex = nodes.indexOf(from);
                    int toIndex = nodes.indexOf(to);

                    if (fromIndex == -1 || toIndex == -1) {
                        throw new IllegalArgumentException("Invalid edge: node not found (" + from + " or " + to + ")");
                    }

                    graph.addEdge(new Edge(fromIndex, toIndex, weight));
                }

                graphs.add(graph);
            }
        }

        return graphs;
    }
}
