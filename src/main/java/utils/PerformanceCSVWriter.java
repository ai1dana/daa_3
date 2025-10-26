package utils;

import algorithms.Edge;
import algorithms.EdgeWeightedGraph;
import algorithms.PrimMST;
import algorithms.KruskalMST;
import metrics.Metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PerformanceCSVWriter {

    public static void main(String[] args) {
        String csvFile = "src/main/resources/performance_results.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("Algorithm,Vertices,Edges,Comparisons,Unions,Inserts,Deletes,OperationsCount,ExecutionTime(ms)\n");

            int[] graphSizes = {10, 50, 100, 200, 500};
            double totalPrimOps = 0;
            double totalKruskalOps = 0;
            double totalPrimTime = 0;
            double totalKruskalTime = 0;

            for (int n : graphSizes) {
                EdgeWeightedGraph graph = createRandomGraph(n);

                // ==== PRIM ====
                Metrics.startTimer();
                long startPrim = System.nanoTime();
                PrimMST primMST = new PrimMST(graph);
                long endPrim = System.nanoTime();
                Metrics.stopTimer();

                double primTime = (endPrim - startPrim) / 1_000_000.0;

                int primOps = Metrics.getComparisonCount()
                        + Metrics.getUnionCount()
                        + Metrics.getInsertCount()
                        + Metrics.getDeleteCount();

                totalPrimOps += primOps;
                totalPrimTime += primTime;

                writer.append(String.format(
                        "Prim,%d,%d,%d,%d,%d,%d,%d,%.4f\n",
                        graph.V(),
                        graph.E(),
                        Metrics.getComparisonCount(),
                        Metrics.getUnionCount(),
                        Metrics.getInsertCount(),
                        Metrics.getDeleteCount(),
                        primOps,
                        primTime
                ));

                // ==== KRUSKAL ====
                Metrics.startTimer();
                long startKruskal = System.nanoTime();
                KruskalMST kruskalMST = new KruskalMST(graph);
                long endKruskal = System.nanoTime();
                Metrics.stopTimer();

                double kruskalTime = (endKruskal - startKruskal) / 1_000_000.0;

                int kruskalOps = Metrics.getComparisonCount()
                        + Metrics.getUnionCount()
                        + Metrics.getInsertCount()
                        + Metrics.getDeleteCount();

                totalKruskalOps += kruskalOps;
                totalKruskalTime += kruskalTime;

                writer.append(String.format(
                        "Kruskal,%d,%d,%d,%d,%d,%d,%d,%.4f\n",
                        graph.V(),
                        graph.E(),
                        Metrics.getComparisonCount(),
                        Metrics.getUnionCount(),
                        Metrics.getInsertCount(),
                        Metrics.getDeleteCount(),
                        kruskalOps,
                        kruskalTime
                ));

                System.out.printf("✅ Graph(%d): Prim = %.4f ms, Kruskal = %.4f ms%n", n, primTime, kruskalTime);
            }

            // === TOTAL SUMMARY ===
            writer.append("\n");
            writer.append(String.format("Prim Total,,,,,,,,%.4f\n", totalPrimTime));
            writer.append(String.format("Kruskal Total,,,,,,,,%.4f\n", totalKruskalTime));
            writer.append(String.format("Prim Total Ops,,,,,,,%d,\n", (int) totalPrimOps));
            writer.append(String.format("Kruskal Total Ops,,,,,,,%d,\n", (int) totalKruskalOps));

            System.out.println("\n✅ Performance results written to: " + csvFile);
            System.out.printf("Prim Total Time: %.3f ms, Total Ops: %.0f%n", totalPrimTime, totalPrimOps);
            System.out.printf("Kruskal Total Time: %.3f ms, Total Ops: %.0f%n", totalKruskalTime, totalKruskalOps);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔧 Генерация связного случайного графа
    private static EdgeWeightedGraph createRandomGraph(int n) {
        List<String> names = java.util.stream.IntStream.range(0, n)
                .mapToObj(i -> "V" + i)
                .toList();

        EdgeWeightedGraph graph = new EdgeWeightedGraph(n, names);
        Random random = new Random(42);

        // базовая связность (цепочка)
        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(new Edge(i, i + 1, random.nextInt(1, 50)));
        }

        // дополнительные случайные рёбра
        int extraEdges = n * 2;
        for (int i = 0; i < extraEdges; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                graph.addEdge(new Edge(u, v, random.nextInt(1, 100)));
            }
        }

        return graph;
    }
}
