package BackEndStructure.Graph;

import java.util.*;

public class Graph {
    private HashSet<Vertex> territoryGraph;

    public Graph(){ territoryGraph = new HashSet<>(); }

    public void AddVertex (Vertex newVertex){
        territoryGraph.add(newVertex);
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
        for (Vertex v : territoryGraph){
            System.out.print("Territory name: "+ v.getTerritory().getTerritoryName());
            System.out.print(" Connected with: ");
            for (Edge e : v.getEdges()){
                System.out.print(e.getVertex().getTerritory().getTerritoryName() + ", ");
            }
            System.out.print("\n");
        }
    }

}
