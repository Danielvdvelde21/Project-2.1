package BackEndStructure.Graph;

import java.util.*;

public class Vertex {
    private final LinkedList<Edge> edgeList;
    private Territory territory;

    public Vertex(Territory data) {
        this.territory = data;
        edgeList = new LinkedList<>();
    }

    public Vertex(Territory data,LinkedList<Edge> edgeList) {
        this.territory = data;
        this.edgeList = edgeList;
    }

    public LinkedList<Edge> getEdges() {
        return edgeList;
    }

    // Calculates Border Security Threat for Vertex
    public double getBSR() {
        int bst = 0;
        for (Edge e : edgeList) {
            // Only add enemy-owned territories to the bsr
            if (e.getVertex().getTerritory().getOwner()!=this.territory.getOwner()) {
                bst += e.getVertex().getTerritory().getNumberOfTroops();
            }
        }
        int units = territory.getNumberOfTroops();
        return (double) bst / units;
    }

    public Territory getTerritory() { return territory; }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

}
