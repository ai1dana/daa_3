
import algorithms.EdgeWeightedGraph;
import algorithms.PrimMST;
import algorithms.KruskalMST;
import utils.GraphLoader;
import metrics.Metrics;
import org.json.JSONObject;


import java.io.FileWriter;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        Metrics.startTimer();

        String inputFilePath = "src/main/resources/assign_3_input.json";
        String outputFilePath = "src/main/resources/output.json";

        try {
            EdgeWeightedGraph graph = GraphLoader.loadGraphFromJson(inputFilePath);

            PrimMST primMST = new PrimMST(graph);
            System.out.println("Prim's Algorithm:");
            System.out.println("Total MST cost: " + primMST.weight());
            Metrics.printMetrics();

            KruskalMST kruskalMST = new KruskalMST(graph);
            System.out.println("Kruskal's Algorithm:");
            System.out.println("Total MST cost: " + kruskalMST.weight());
            Metrics.printMetrics();

            JSONObject result = new JSONObject();
            result.put("primMSTCost", primMST.weight());
            result.put("kruskalMSTCost", kruskalMST.weight());
            result.put("executionTime", Metrics.getExecutionTime());
            result.put("primComparisons", Metrics.getComparisonCount());
            result.put("kruskalComparisons", Metrics.getComparisonCount());
            result.put("primUnions", Metrics.getUnionCount());
            result.put("kruskalUnions", Metrics.getUnionCount());

            FileWriter writer = new FileWriter(outputFilePath);
            writer.write(result.toString(4));
            writer.close();

        } catch (IOException e) {
            System.err.println("Error reading input data: " + e.getMessage());
        }

        Metrics.stopTimer();
    }
}


