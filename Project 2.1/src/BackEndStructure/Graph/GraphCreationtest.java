package BackEndStructure.Graph;

public class GraphCreationtest {
    public static void main(String[] args) {
        Graph testGraph = new Graph();

        // TODO Make map for Map.java
        // Territories
        Territory t0 = new Territory("London", 3, 1);
        Territory t1 = new Territory("Amsterdam", 6, 2);
        Territory t2 = new Territory("Maastricht", 7, 3);
        Territory t3 = new Territory("New York", 1, 2);

        // Vertices
        Vertex v0 = new Vertex(t0);
        Vertex v1 = new Vertex(t1);
        Vertex v2 = new Vertex(t2);
        Vertex v3 = new Vertex(t3);

        testGraph.AddVertex(v0);
        testGraph.AddVertex(v1);
        testGraph.AddVertex(v2);
        testGraph.AddVertex(v3);

        // Edges
        testGraph.addEdge(v0, v1);
        testGraph.addEdge(v1, v2);
        testGraph.addEdge(v2, v0);
        testGraph.addEdge(v2, v3);

        testGraph.printGraph();
    }
}
