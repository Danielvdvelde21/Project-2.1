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

    public double getBSR() {
        int bst = 0;
        for (Edge e : edgeList) {
            bst += e.getVertex().getTerritory().getNumberOfTroops();
        }
        int units = territory.getNumberOfTroops();

        return bst / units;
    }

    public Territory getTerritory() { return territory; }
}
