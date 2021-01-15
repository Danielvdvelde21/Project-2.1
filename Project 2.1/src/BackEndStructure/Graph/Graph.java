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

    public void AddVertex (Vertex newVertex){
        graph.add(newVertex);
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.getEdges().add(new Edge(vertex2));
        vertex2.getEdges().add(new Edge(vertex1));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Checking for adjacency

    public boolean isAdjacent(Vertex vertex1, Vertex vertex2) {
        for (int i = 0; i < vertex1.getEdges().size(); i++) {
            if(vertex1.getEdges().get(i).getVertex() == vertex2) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getters

    public Vertex get(int i) { return graph.get(i); }

    public int getSize() { return graph.size(); }

    public ArrayList<Vertex> getArrayList() { return graph; }

    public Graph clone(){
        ArrayList<Vertex> clonedVs=new ArrayList<Vertex>();
        for(Vertex v : graph) {
            Vertex newV=new Vertex(v.getTerritory().clone(),v.getEdges());
            clonedVs.add(newV);
        }
        Graph g1=new Graph(clonedVs);
        return g1;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Printing the graph

    public void printGraph(){
        System.out.println("Printing Graph:");
        for (Vertex v : graph){
            System.out.print("Territory name: "+ v.getTerritory().getTerritoryName());
            System.out.print(", owned by: "+ v.getTerritory().getOwner().getName());
            System.out.print(", Connected with: ");
            for (Edge e : v.getEdges()){
                System.out.print(e.getVertex().getTerritory().getTerritoryName() + ", ");
            }
            System.out.print("\n");
        }
    }

    public void printGraphShort(){
        System.out.println("Printing short graph:");
        for (Vertex v : graph){
            System.out.print("["+v.getTerritory().getTerritoryNumber());
            System.out.print(", "+v.getTerritory().getOwner().getName()+"],");
        }
        System.out.print("\n");
    }
}
