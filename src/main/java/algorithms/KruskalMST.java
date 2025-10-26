package algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import metrics.Metrics;

public class KruskalMST {

    private static final double FLOATING_POINT_EPSILON = 1.0E-12;
    private double weight;
    private Queue<Edge> mst = new LinkedList<>();

    public KruskalMST(EdgeWeightedGraph G) {
        Edge[] edges = new Edge[G.E()];
        int t = 0;
        for (Edge e : G.edges()) {
            edges[t++] = e;
        }

        Arrays.sort(edges);

        UF uf = new UF(G.V());

        Metrics.startTimer();

        for (Edge e : edges) {
            int v = e.either();
            int w = e.other(v);

            Metrics.incrementComparisons();

            if (uf.find(v) != uf.find(w)) {
                uf.union(v, w);
                mst.add(e);
                weight += e.weight();
                Metrics.incrementUnions();
            }
        }

        Metrics.stopTimer();
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
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
            Metrics.incrementComparisons();

            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
            Metrics.incrementUnions();
        }

        for (Edge e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        for (Edge e : edges()) {
            uf = new UF(G.V());
            for (Edge f : edges()) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
                Metrics.incrementUnions();
            }
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (uf.find(x) != uf.find(y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
