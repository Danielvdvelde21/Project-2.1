package BackEndStructure.Graph;

import java.util.*;

public class Graph {
    private HashSet<Vertex> territories;

    public Graph(){
        territories = new HashSet<>();
    }

    public boolean AddVertex(Vertex newVertex){
        return territories.add(newVertex);
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.getEdges().add(new Edge(vertex2));
    }

    public void printGraph(){
        for (Vertex v : territories){
            System.out.print("BackEndStructure.Graph.Territory name: "+ v.territoryData.get_TerritoryName());
            System.out.print(" Connected with: ");
            for(Edge e : v.getEdges()){
                System.out.print(e.getVertex().territoryData.get_TerritoryName() + ", ");
            }
            System.out.print("\n");
        }
    }

}
