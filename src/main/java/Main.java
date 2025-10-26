import algorithms.EdgeWeightedGraph;
import algorithms.PrimMST;
import algorithms.KruskalMST;
import utils.GraphLoader;
import metrics.Metrics;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Metrics.startTimer();

        String inputFilePath = "src/main/resources/ass_3_input.json";
        String outputFilePath = "src/main/resources/output.json";

        try {
            List<EdgeWeightedGraph> graphs = GraphLoader.loadGraphFromJson(inputFilePath);

            JSONObject result = new JSONObject();

            for (int i = 0; i < graphs.size(); i++) {
                EdgeWeightedGraph graph = graphs.get(i);
                System.out.println("Processing graph #" + (i + 1) + ":");

                Metrics.startTimer();
                PrimMST primMST = new PrimMST(graph);
                System.out.println("Prim's Algorithm:");
                System.out.println("Total MST cost: " + primMST.weight());
                Metrics.printMetrics();

                Metrics.startTimer();
                KruskalMST kruskalMST = new KruskalMST(graph);
                System.out.println("Kruskal's Algorithm:");
                System.out.println("Total MST cost: " + kruskalMST.weight());
                Metrics.printMetrics();

                result.put("graph" + (i + 1), new JSONObject()
                        .put("primMSTCost", primMST.weight())
                        .put("kruskalMSTCost", kruskalMST.weight())
                        .put("executionTime", Metrics.getExecutionTime())
                        .put("primComparisons", Metrics.getComparisonCount())
                        .put("kruskalComparisons", Metrics.getComparisonCount())
                        .put("primUnions", Metrics.getUnionCount())
                        .put("kruskalUnions", Metrics.getUnionCount()));
            }

            FileWriter writer = new FileWriter(outputFilePath);
            writer.write(result.toString(4));
            writer.close();

            System.out.println("Results have been written to output.json");

        } catch (IOException e) {
            System.err.println("Error reading input data: " + e.getMessage());
        }

        Metrics.stopTimer();
    }
}
