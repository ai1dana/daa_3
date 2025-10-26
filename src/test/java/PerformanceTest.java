

import algorithms.Edge;
import algorithms.EdgeWeightedGraph;
import algorithms.PrimMST;
import algorithms.KruskalMST;
import metrics.Metrics;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceTest {

    @Test
    public void testPrimMSTPerformance() {
        EdgeWeightedGraph graph = createTestGraph();

        Metrics.startTimer();
        PrimMST primMST = new PrimMST(graph);
        Metrics.stopTimer();

        long executionTime = Metrics.getExecutionTime();
        System.out.println("Prim MST execution time: " + executionTime + " ms");
        System.out.println("Comparisons: " + Metrics.getComparisonCount());
        System.out.println("Unions: " + Metrics.getUnionCount());
        System.out.println("Inserts: " + Metrics.getInsertCount());
        System.out.println("Deletes: " + Metrics.getDeleteCount());

        assertTrue(primMST.weight() > 0, "Prim's MST weight must be positive");
    }

    @Test
    public void testKruskalMSTPerformance() {
        EdgeWeightedGraph graph = createTestGraph();

        Metrics.startTimer();
        KruskalMST kruskalMST = new KruskalMST(graph);
        Metrics.stopTimer();

        long executionTime = Metrics.getExecutionTime();
        System.out.println("Kruskal MST execution time: " + executionTime + " ms");
        System.out.println("Comparisons: " + Metrics.getComparisonCount());
        System.out.println("Unions: " + Metrics.getUnionCount());
        System.out.println("Inserts: " + Metrics.getInsertCount());
        System.out.println("Deletes: " + Metrics.getDeleteCount());

        assertTrue(kruskalMST.weight() > 0, "Kruskal's MST weight must be positive");
    }

    private EdgeWeightedGraph createTestGraph() {
        List<String> names = List.of("A", "B", "C", "D", "E");
        EdgeWeightedGraph graph = new EdgeWeightedGraph(5, names);
        graph.addEdge(new Edge(0, 1, 2.0));
        graph.addEdge(new Edge(0, 2, 3.0));
        graph.addEdge(new Edge(1, 2, 1.0));
        graph.addEdge(new Edge(1, 3, 4.0));
        graph.addEdge(new Edge(2, 4, 5.0));
        return graph;
    }
}
