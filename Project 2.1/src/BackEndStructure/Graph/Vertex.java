package BackEndStructure.Graph;

import java.util.*;

public class Vertex {

    Territory territoryData;
    private LinkedList<Edge> edgeList;

    public Vertex(Territory data) {
        this.territoryData = data;
        edgeList = new LinkedList<>();
    }

    public LinkedList<Edge> getEdges() {
        return edgeList;
    }
}
