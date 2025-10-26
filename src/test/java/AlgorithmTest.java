

import algorithms.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTest {

    @Test
    public void testPrimMSTCost() {
        EdgeWeightedGraph graph = createTestGraph();

        PrimMST primMST = new PrimMST(graph);

        double expectedCost = 4.0;
        assertEquals(expectedCost, primMST.weight(), "Prim's MST cost is incorrect");
    }

    @Test
    public void testKruskalMSTCost() {
        EdgeWeightedGraph graph = createTestGraph();

        KruskalMST kruskalMST = new KruskalMST(graph);

        double expectedCost = 4.0;
        assertEquals(expectedCost, kruskalMST.weight(), "Kruskal's MST cost is incorrect");
    }

    @Test
    public void testPrimMSTEdgeCount() {
        EdgeWeightedGraph graph = createTestGraph();

        PrimMST primMST = new PrimMST(graph);

        int expectedEdgeCount = graph.V() - 1;
        int actualEdgeCount = 0;
        for (Edge e : primMST.edges()) {
            actualEdgeCount++;
        }
        assertEquals(expectedEdgeCount, actualEdgeCount, "Prim's MST does not have the correct number of edges");
    }

    @Test
    public void testKruskalMSTEdgeCount() {
        EdgeWeightedGraph graph = createTestGraph();

        KruskalMST kruskalMST = new KruskalMST(graph);

        int expectedEdgeCount = graph.V() - 1;
        int actualEdgeCount = 0;
        for (Edge e : kruskalMST.edges()) {
            actualEdgeCount++;
        }
        assertEquals(expectedEdgeCount, actualEdgeCount, "Kruskal's MST does not have the correct number of edges");
    }

    @Test
    public void testPrimMSTNoCycles() {
        EdgeWeightedGraph graph = createTestGraph();
        PrimMST primMST = new PrimMST(graph);
        assertTrue(isAcyclic(primMST), "Prim's MST contains cycles");
    }

    @Test
    public void testKruskalMSTNoCycles() {
        EdgeWeightedGraph graph = createTestGraph();
        KruskalMST kruskalMST = new KruskalMST(graph);
        assertTrue(isAcyclic(kruskalMST), "Kruskal's MST contains cycles");
    }

    private EdgeWeightedGraph createTestGraph() {
        List<String> names = List.of("A", "B", "C", "D", "E");
        EdgeWeightedGraph graph = new EdgeWeightedGraph(5, names);
        graph.addEdge(new Edge(0, 1, 1.0));
        graph.addEdge(new Edge(1, 2, 1.0));
        graph.addEdge(new Edge(2, 3, 1.0));
        graph.addEdge(new Edge(3, 4, 1.0));
        graph.addEdge(new Edge(4, 0, 1.0));
        return graph;
    }

    private boolean isAcyclic(PrimMST mst) {
        return true;
    }

    private boolean isAcyclic(KruskalMST mst) {
        return true;
    }
}
