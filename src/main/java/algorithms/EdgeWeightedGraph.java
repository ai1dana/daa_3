package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdgeWeightedGraph {
    private final int V;
    private int E;
    private final List<Edge>[] adj;
    private final Map<Integer, String> nodeNames;

    @SuppressWarnings("unchecked")
    public EdgeWeightedGraph(int V, List<String> names) {
        this.V = V;
        this.E = 0;
        this.adj = (List<Edge>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }

        this.nodeNames = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            nodeNames.put(i, names.get(i));
        }
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public String nodeName(int index) {
        return nodeNames.getOrDefault(index, "V" + index);
    }

    public Iterable<Edge> edges() {
        List<Edge> allEdges = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj[v]) {
                if (e.other(v) > v) {
                    allEdges.add(e);
                }
            }
        }
        return allEdges;
    }
}
