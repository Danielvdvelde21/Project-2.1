package BackEndStructure.Graph;

public class Edge {

    private Vertex vertex;

    // TODO add ege backwards
    public Edge(Vertex newVertex) {
        this.vertex = newVertex;
    }

    public Vertex getVertex() {
        return vertex;
    }
}
