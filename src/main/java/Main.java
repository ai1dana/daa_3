import algorithms.*;
import metrics.Metrics;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.GraphImageRenderer;
import utils.GraphLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> verts = List.of("A","B","C","D","E");
        Graph myGraph = new Graph(verts);
        myGraph.addEdge("A","B",4);
        myGraph.addEdge("A","C",3);
        myGraph.addEdge("B","C",2);
        myGraph.addEdge("B","D",5);
        myGraph.addEdge("C","D",7);
        myGraph.addEdge("C","E",8);
        myGraph.addEdge("D","E",6);

        GraphImageRenderer.renderGraph(myGraph, null,
                "src/main/resources/graph_demo.png");

        EdgeWeightedGraph gForMst = myGraph.toEdgeWeightedGraph();

        PrimMST pmst = new PrimMST(gForMst);
        List<Edge> primEdges = new ArrayList<>();
        pmst.edges().forEach(primEdges::add);
        GraphImageRenderer.renderGraph(myGraph, primEdges,
                "src/main/resources/graph_demo_mst_prim.png");

        KruskalMST kmst = new KruskalMST(gForMst);
        List<Edge> kruskalEdges = new ArrayList<>();
        kmst.edges().forEach(kruskalEdges::add);
        GraphImageRenderer.renderGraph(myGraph, kruskalEdges,
                "src/main/resources/graph_demo_mst_kruskal.png");

        System.out.println(" Graph images saved (bonus visualization).");


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
                    edgeObj.put("from", String.valueOf(e.either()));
                    edgeObj.put("to", String.valueOf(e.other(e.either())));
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
                    edgeObj.put("from", String.valueOf(e.either()));
                    edgeObj.put("to", String.valueOf(e.other(e.either())));
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

                Metrics.startTimer();
                Metrics.stopTimer();
            }

            JSONObject finalOutput = new JSONObject();
            finalOutput.put("results", resultsArray);

            try (FileWriter writer = new FileWriter(outputFilePath)) {
                writer.write(finalOutput.toString(4));
            }

            System.out.println(" Results successfully written to " + outputFilePath);

        } catch (IOException e) {
            System.err.println(" Error reading input: " + e.getMessage());
        }
    }
}
