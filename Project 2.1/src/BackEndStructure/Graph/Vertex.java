package BackEndStructure.Graph;

import java.util.*;

public class Vertex {
    private LinkedList<Edge> edgeList;
    private Territory territory;

    public Vertex(Territory data) {
        this.territory = data;
        edgeList = new LinkedList<>();
    }

    public LinkedList<Edge> getEdges() {
        return edgeList;
    }

    public Territory getTerritory() { return territory; }
}
