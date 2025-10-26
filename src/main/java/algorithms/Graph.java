package algorithms;

import java.util.*;

public class Graph {
    private final Map<String, Integer> nameToIndex = new HashMap<>();
    private final List<String> indexToName = new ArrayList<>();
    private final List<List<Edge>> adj;  // adjacency by numeric indices
    private int e = 0;

    public Graph(List<String> vertices) {
        for (String v : vertices) {
            nameToIndex.put(v, indexToName.size());
            indexToName.add(v);
        }
        adj = new ArrayList<>(V());
        for (int i = 0; i < V(); i++) adj.add(new ArrayList<>());
    }

    public int V() { return indexToName.size(); }
    public int E() { return e; }

    public void addEdge(String from, String to, double weight) {
        Integer v = nameToIndex.get(from);
        Integer w = nameToIndex.get(to);
        if (v == null || w == null) throw new IllegalArgumentException("Unknown vertex");
        Edge edge = new Edge(v, w, weight);
        adj.get(v).add(edge);
        adj.get(w).add(edge);
        e++;
    }

    public Iterable<Edge> adj(String name) {
        Integer v = nameToIndex.get(name);
        return adj.get(v);
    }

    public Iterable<Edge> allEdges() {
        List<Edge> list = new ArrayList<>();
        boolean[][] seen = new boolean[V()][V()];
        for (int v = 0; v < V(); v++) {
            for (Edge ed : adj.get(v)) {
                int a = ed.either(), b = ed.other(a);
                int x = Math.min(a,b), y = Math.max(a,b);
                if (!seen[x][y]) { list.add(ed); seen[x][y]=true; }
            }
        }
        return list;
    }

    public Set<String> vertices() { return nameToIndex.keySet(); }
    public String nameOf(int index) { return indexToName.get(index); }
    public Integer indexOf(String name) { return nameToIndex.get(name); }
    public List<Edge> adjByIndex(int v) { return adj.get(v); }
    public EdgeWeightedGraph toEdgeWeightedGraph() {
        EdgeWeightedGraph g = new EdgeWeightedGraph(V(), new ArrayList<>(vertices()));

        for (Edge e : allEdges()) {
            g.addEdge(e);
        }

        return g;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int v = 0; v < V(); v++) {
            sb.append(nameOf(v)).append(": ");
            for (Edge e : adj.get(v)) {
                int w = e.other(v);
                sb.append(nameOf(v)).append("-").append(nameOf(w))
                        .append("(").append(e.weight()).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
