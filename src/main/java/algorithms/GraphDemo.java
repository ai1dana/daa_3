package algorithms;

public class GraphDemo {
    public static void main(String[] args) {
        java.util.List<String> vertices = java.util.List.of("A", "B", "C", "D");
        Graph graph = new Graph(vertices);

        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 1);

        System.out.println(graph);
    }
}
