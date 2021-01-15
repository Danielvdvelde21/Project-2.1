package BackEndStructure.Graph;

public class Edge {

    private final Vertex vertex;

    public Edge(Vertex newVertex) {
        this.vertex = newVertex;
    }

    public Vertex getVertex() {
        return vertex;
    }
}
