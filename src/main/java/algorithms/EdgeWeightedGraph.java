package algorithms;

import java.util.*;

public class EdgeWeightedGraph {
    private final int V;
    private final List<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
        this.V = V;
        adj = (List<Edge>[]) new List[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
    }

    public Iterable<Edge> adj(int v) {
        return adj[v];
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

    public int V() {
        return V;
    }

    public int E() {
        int count = 0;
        for (int v = 0; v < V; v++) {
            count += adj[v].size();
        }
        return count / 2;
    }
}
