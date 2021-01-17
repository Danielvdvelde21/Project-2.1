package BackEndStructure.Graph;

import java.util.*;

public class Graph {

    private final ArrayList<Vertex> graph;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors

    public Graph() {
        graph = new ArrayList<>();
    }

    public Graph(ArrayList<Vertex> existingGraph) {
        graph = existingGraph;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Building a graph

    public void AddVertex(Vertex newVertex) {
        graph.add(newVertex);
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.addEdge(new Edge(vertex2));
        vertex2.addEdge(new Edge(vertex1));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Checking for adjacency

    public boolean isAdjacent(Vertex vertex1, Vertex vertex2) {
        for (int i = 0; i < vertex1.getEdgeNo(); i++) {
            if (vertex1.getEdges()[i].getVertex() == vertex2) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getters

    public Vertex get(int i) {
        return graph.get(i);
    }

    public int getSize() {
        return graph.size();
    }

    public ArrayList<Vertex> getArrayList() {
        return graph;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Printing the graph

    public void printGraph() {
        System.out.println("Printing Graph:");
        for (Vertex v : graph) {
            System.out.print("Territory name: " + v.getTerritory().getTerritoryName());
            System.out.print(", owned by: " + v.getTerritory().getOwner().getName());
            System.out.print(", Connected with: ");
            Edge[] neighbours = v.getEdges();
            int neighboursNo = v.getEdgeNo();
            for (int i=0;i<neighboursNo;i++) {
                Edge e=neighbours[i];
                System.out.print(e.getVertex().getTerritory().getTerritoryName() + ", ");
            }
            System.out.print("\n");
        }
    }

    public void printGraphShort() {
        System.out.println("Printing short graph:");
        for (Vertex v : graph) {
            System.out.print("[" + v.getTerritory().getTerritoryNumber());
            System.out.print(", " + v.getTerritory().getOwner().getName() + "],");
        }
        System.out.print("\n");
    }
}
