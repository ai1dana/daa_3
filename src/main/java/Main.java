import algorithms.EdgeWeightedGraph;
import algorithms.PrimMST;
import algorithms.KruskalMST;
import utils.GraphLoader;
import metrics.Metrics;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/ass_3_input.json";
        String outputFilePath = "src/main/resources/output.json";

        JSONArray resultsArray = new JSONArray();

        try {
            List<EdgeWeightedGraph> graphs = GraphLoader.loadGraphFromJson(inputFilePath);

            for (int i = 0; i < graphs.size(); i++) {
                EdgeWeightedGraph graph = graphs.get(i);
                int graphId = i + 1;

                System.out.println("Processing graph #" + graphId);

                Metrics.startTimer();
                PrimMST primMST = new PrimMST(graph);
                Metrics.stopTimer();
                long primTime = Metrics.getExecutionTime();
                int primOps = Metrics.getComparisonCount() + Metrics.getInsertCount() + Metrics.getUnionCount();

                JSONArray primEdgesArray = new JSONArray();
                for (var e : primMST.edges()) {
                    JSONObject edgeObj = new JSONObject();
                    edgeObj.put("from", graph.nodeName(e.either()));
                    edgeObj.put("to", graph.nodeName(e.other(e.either())));
                    edgeObj.put("weight", e.weight());
                    primEdgesArray.put(edgeObj);
                }

                JSONObject primObj = new JSONObject();
                primObj.put("mst_edges", primEdgesArray);
                primObj.put("total_cost", primMST.weight());
                primObj.put("operations_count", primOps);
                primObj.put("execution_time_ms", primTime / 1_000_000.0);

                Metrics.startTimer();
                KruskalMST kruskalMST = new KruskalMST(graph);
                Metrics.stopTimer();
                long kruskalTime = Metrics.getExecutionTime();
                int kruskalOps = Metrics.getComparisonCount() + Metrics.getUnionCount();

                JSONArray kruskalEdgesArray = new JSONArray();
                for (var e : kruskalMST.edges()) {
                    JSONObject edgeObj = new JSONObject();
                    edgeObj.put("from", graph.nodeName(e.either()));
                    edgeObj.put("to", graph.nodeName(e.other(e.either())));
                    edgeObj.put("weight", e.weight());
                    kruskalEdgesArray.put(edgeObj);
                }

                JSONObject kruskalObj = new JSONObject();
                kruskalObj.put("mst_edges", kruskalEdgesArray);
                kruskalObj.put("total_cost", kruskalMST.weight());
                kruskalObj.put("operations_count", kruskalOps);
                kruskalObj.put("execution_time_ms", kruskalTime / 1_000_000.0);

                JSONObject graphResult = new JSONObject();
                graphResult.put("graph_id", graphId);

                JSONObject stats = new JSONObject();
                stats.put("vertices", graph.V());
                stats.put("edges", graph.E());
                graphResult.put("input_stats", stats);

                graphResult.put("prim", primObj);
                graphResult.put("kruskal", kruskalObj);

                resultsArray.put(graphResult);
            }

            JSONObject finalOutput = new JSONObject();
            finalOutput.put("results", resultsArray);

            try (FileWriter writer = new FileWriter(outputFilePath)) {
                writer.write(finalOutput.toString(4));
            }

            System.out.println("Results successfully written to " + outputFilePath);

        } catch (IOException e) {
            System.err.println(" Error reading input: " + e.getMessage());
        }
    }
}
