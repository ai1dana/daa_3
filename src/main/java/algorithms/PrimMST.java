package algorithms;

import metrics.Metrics;
import java.util.*;

/**
 * Реализация алгоритма Прима (MST) с поддержкой метрик
 */
public class PrimMST {

    private static final double FLOATING_POINT_EPSILON = 1.0E-12;
    private Edge[] edgeTo;
    private double[] distTo;
    private boolean[] marked;
    private PriorityQueue<Edge> pq;
    private double totalWeight;

    public PrimMST(EdgeWeightedGraph G) {
        int V = G.V();
        edgeTo = new Edge[V];
        distTo = new double[V];
        marked = new boolean[V];
        pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::weight));
        totalWeight = 0.0;

        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }

        Metrics.startTimer();

        visit(G, 0);

        while (!pq.isEmpty()) {
            Metrics.incrementComparisons();
            Edge edge = pq.poll();
            int v = edge.either();
            int w = edge.other(v);

            if (marked[v] && marked[w]) continue;

            if (!marked[v]) visit(G, v);
            if (!marked[w]) visit(G, w);

            totalWeight += edge.weight();
        }

        Metrics.stopTimer();
    }

    private void visit(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            Metrics.incrementComparisons();

            if (marked[w]) continue;

            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                pq.add(e);
                Metrics.incrementInserts();
            }
        }
    }

    public Iterable<Edge> edges() {
        List<Edge> mst = new ArrayList<>();
        for (Edge e : edgeTo) {
            if (e != null) mst.add(e);
        }
        return mst;
    }

    public double weight() {
        return totalWeight;
    }

    private boolean check(EdgeWeightedGraph G) {
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        UF uf = new UF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        for (Edge e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        return true;
    }
}
