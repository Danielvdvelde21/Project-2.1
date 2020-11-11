package BackEndStructure.Graph;

import java.util.*;

public class Graph {
    private final List<Vertex> graph;

    public Graph(){ graph = new ArrayList<Vertex>(); }

    public void AddVertex (Vertex newVertex){
        graph.add(newVertex);
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.getEdges().add(new Edge(vertex2));
        vertex2.getEdges().add(new Edge(vertex1));
    }

    public boolean isAdjecent(Vertex vertex1, Vertex vertex2) {
        for (int i = 0; i < vertex1.getEdges().size(); i++) {
            if(vertex1.getEdges().get(i).getVertex() == vertex2) {
                return true;
            }
        }
        return false;
    }

    public void printGraph(){
        for (Vertex v : graph){
            System.out.print("Territory name: "+ v.getTerritory().getTerritoryName());
            System.out.print(" Connected with: ");
            for (Edge e : v.getEdges()){
                System.out.print(e.getVertex().getTerritory().getTerritoryName() + ", ");
            }
            System.out.print("\n");
        }
    }

    public Vertex get(int i) {
        return graph.get(i);
    }

    public int getSize() { return graph.size(); }
}
