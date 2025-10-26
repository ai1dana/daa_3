

import algorithms.Edge;
import algorithms.EdgeWeightedGraph;
import algorithms.PrimMST;
import algorithms.KruskalMST;
import metrics.Metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PerformanceCSVWriter {

    public static void main(String[] args) {
        String csvFile = "src/main/resources/performance_results.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("Algorithm,Vertices,Edges,Comparisons,Unions,Inserts,Deletes,ExecutionTime(ms)\n");

            EdgeWeightedGraph graph = createLargeGraph(500);

            int repeats = 50;

            double totalPrimTime = 0;
            for (int i = 0; i < repeats; i++) {
                Metrics.startTimer();
                PrimMST primMST = new PrimMST(graph);
                Metrics.stopTimer();
                totalPrimTime += (double) Metrics.getExecutionTime();
            }
            double avgPrimTime = totalPrimTime / repeats;

            writer.append(String.format(
                    "Prim,%d,%d,%d,%d,%d,%d,%.3f\n",
                    graph.V(),
                    graph.E(),
                    Metrics.getComparisonCount(),
                    Metrics.getUnionCount(),
                    Metrics.getInsertCount(),
                    Metrics.getDeleteCount(),
                    avgPrimTime
            ));

            double totalKruskalTime = 0;
            for (int i = 0; i < repeats; i++) {
                Metrics.startTimer();
                KruskalMST kruskalMST = new KruskalMST(graph);
                Metrics.stopTimer();
                totalKruskalTime += (double) Metrics.getExecutionTime();
            }
            double avgKruskalTime = totalKruskalTime / repeats;

            writer.append(String.format(
                    "Kruskal,%d,%d,%d,%d,%d,%d,%.3f\n",
                    graph.V(),
                    graph.E(),
                    Metrics.getComparisonCount(),
                    Metrics.getUnionCount(),
                    Metrics.getInsertCount(),
                    Metrics.getDeleteCount(),
                    avgKruskalTime
            ));

            System.out.println("✅ Performance results written to: " + csvFile);
            System.out.printf("Prim avg time: %.3f ms%n", avgPrimTime);
            System.out.printf("Kruskal avg time: %.3f ms%n", avgKruskalTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static EdgeWeightedGraph createLargeGraph(int n) {
        List<String> names = java.util.stream.IntStream.range(0, n)
                .mapToObj(i -> "V" + i)
                .toList();

        EdgeWeightedGraph graph = new EdgeWeightedGraph(n, names);
        java.util.Random random = new java.util.Random(42);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(new Edge(i, i + 1, random.nextInt(1, 50)));
        }

        for (int i = 0; i < n * 2; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                graph.addEdge(new Edge(u, v, random.nextInt(1, 100)));
            }
        }

        return graph;
    }
}
