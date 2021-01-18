package BackEndStructure.Graph;

public class Vertex {

    private final Edge[]edgeList;
    public int edgeNo=0;

    private Territory territory;

    public Vertex(Territory data) {
        this.territory = data;
        edgeList =new Edge[6];
    }

    public Vertex(Territory data,Edge[] edgeList) {
        this.territory = data;
        this.edgeList = edgeList;
    }

    public Edge[] getEdges() {
        return edgeList;
    }

    // Calculates Border Security Threat for Vertex
    public double getBSR() {
        int bst = 0;
        Edge[] neighbours = edgeList;
        int neighboursNo = edgeNo;
        for (int i=0;i<neighboursNo;i++) {
            Edge e=neighbours[i];
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

    public int getEdgeNo() {
        return edgeNo;
    }

    public void setEdgeNo(int edgeNo) {
        this.edgeNo = edgeNo;
    }

    public void addEdge(Edge edge) {
        edgeList[edgeNo]=edge;
        edgeNo++;
    }
}
