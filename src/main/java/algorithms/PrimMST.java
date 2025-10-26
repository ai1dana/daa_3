package algorithms;

import java.util.*;

import metrics.Metrics;

public class PrimMST {

    private static final double FLOATING_POINT_EPSILON = 1.0E-12;
    private Edge[] edgeTo;
    private double[] distTo;
    private boolean[] marked;
    private PriorityQueue<Edge> pq;

    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::weight));

        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }

        Metrics.startTimer();

        visit(G, 0);

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            Metrics.incrementComparisons();
            int v = edge.from();
            visit(G, v);
        }

        Metrics.stopTimer();
    }

    private void visit(EdgeWeightedGraph G, int v) {
        marked[v] = true;

        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;

            Metrics.incrementComparisons();

            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;

                if (pq.contains(e)) {
                    pq.remove(e);
                    Metrics.incrementDeletes();
                }
                pq.add(e);
                Metrics.incrementInserts();
            }
        }
    }

    public Iterable<Edge> edges() {
        Queue<Edge> mst = new LinkedList<>();
        for (int v = 0; v < edgeTo.length; v++) {
            if (edgeTo[v] != null) {
                mst.add(edgeTo[v]);
            }
        }
        return mst;
    }

    public double weight() {
        double weight = 0.0;
        for (Edge e : edges()) {
            weight += e.weight();
        }
        return weight;
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
