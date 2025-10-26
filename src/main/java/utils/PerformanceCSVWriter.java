package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class PerformanceCSVWriter {


    static class EdgeOut {
        String from;
        String to;
        double weight;
    }

    static class AlgoOut {
        @SerializedName("mst_edges")
        List<EdgeOut> mstEdges = new ArrayList<>();

        @SerializedName("total_cost")
        double totalCost;

        @SerializedName("operations_count")
        long operationsCount;

        @SerializedName("execution_time_ms")
        double executionTimeMs;
    }

    static class InputStats {
        int vertices;
        int edges;
    }

    static class GraphResult {
        @SerializedName("graph_id")
        int graphId;

        @SerializedName("input_stats")
        InputStats inputStats;

        AlgoOut prim;
        AlgoOut kruskal;
    }

    static class OutputRoot {
        List<GraphResult> results = new ArrayList<>();
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        String inputPath  = args.length > 0 ? args[0] : "src/main/resources/output.json";
        String outputPath = args.length > 1 ? args[1] : "src/main/resources/output.csv";

        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), StandardCharsets.UTF_8))) {

            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .create();

            OutputRoot root = gson.fromJson(reader, OutputRoot.class);
            if (root == null || root.results == null || root.results.isEmpty()) {
                System.err.println("ERROR " + inputPath);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {

                writer.write(String.join(",",
                        "graph_id",
                        "algorithm",
                        "vertices",
                        "edges",
                        "total_cost",
                        "operations_count",
                        "execution_time_ms",
                        "mst_edge_count",
                        "mst_edges"
                ));
                writer.newLine();

                for (GraphResult graph : root.results) {
                    int gid = graph != null ? graph.graphId : -1;
                    int V = (graph != null && graph.inputStats != null) ? graph.inputStats.vertices : 0;
                    int E = (graph != null && graph.inputStats != null) ? graph.inputStats.edges : 0;

                    writeAlgoRow(writer, gid, "Prim", V, E, graph != null ? graph.prim : null);
                    
                    writeAlgoRow(writer, gid, "Kruskal", V, E, graph != null ? graph.kruskal : null);
                }

                writer.flush();
                System.out.println("CSV file created successfully: " + outputPath);
            }

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeAlgoRow(BufferedWriter writer,
                                     int graphId,
                                     String algorithm,
                                     int vertices,
                                     int edges,
                                     AlgoOut algo) throws IOException {

        double totalCost = safeDouble(algo == null ? null : algo.totalCost);
        long ops = safeLong(algo == null ? null : algo.operationsCount);
        double timeMs = safeDouble(algo == null ? null : algo.executionTimeMs);

        List<EdgeOut> mst = (algo != null && algo.mstEdges != null) ? algo.mstEdges : List.of();
        int mstCount = mst.size();
        String mstStr = formatMstEdges(mst);

        writer.write(csv(graphId) + ","
                + csv(algorithm) + ","
                + csv(vertices) + ","
                + csv(edges) + ","
                + csv(totalCost) + ","
                + csv(ops) + ","
                + csv(timeMs) + ","
                + csv(mstCount) + ","
                + csv(mstStr));
        writer.newLine();
    }


    private static String formatMstEdges(List<EdgeOut> edges) {
        if (edges == null || edges.isEmpty()) return "";
        return edges.stream()
                .filter(Objects::nonNull)
                .map(e -> {
                    String f = e.from == null ? "" : e.from;
                    String t = e.to == null ? "" : e.to;
                    double w = e.weight;
                    String wStr = (Math.rint(w) == w) ? String.format(Locale.US, "%.0f", w) : String.format(Locale.US, "%.4f", w);
                    return f + "-" + t + "(" + wStr + ")";
                })
                .collect(Collectors.joining("; "));
    }

    private static String csv(Object value) {
        String s = (value == null) ? "" : String.valueOf(value);
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        if (needQuotes) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

    private static double safeDouble(Double d) {
        return d == null ? 0.0 : d;
    }

    private static long safeLong(Long l) {
        return l == null ? 0L : l;
    }
}
