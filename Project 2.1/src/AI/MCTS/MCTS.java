package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    public void getPossibleAttacks(Graph g, Player p, Node parent) {

        ArrayList<Vertex> owned = getOwnedVertices(g, p);
        for (Vertex v : owned) {
            Node x = new Node(new State(g, p));
            parent.addChild(x);
        }

        // TODO return parent
    }

}
