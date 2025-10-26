package algorithms;

public class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // Метод для получения одной из вершин ребра
    public int either() {
        return v;  // Можно вернуть либо v, либо w, в зависимости от контекста
    }

    public int other(int vertex) {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Inconsistent edge");
    }

    public double weight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
}
