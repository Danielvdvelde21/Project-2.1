package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.MainGameLoop;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    // Adds all the possible attacks on the current graph to the parent node
    public void addPossibleAttacks(Graph g, Player p, Node parent) {
        ArrayList<Vertex> owned = getOwnedVertices(g, p);
        for (Vertex v : owned) {
            Node x = new Node(new State(g, p));
            parent.addChild(x);
        }
    }


    // TODO Selection
    public void selection() {

    }

    // TODO Play-out
    public void playOut(int playerNo) {
        String[] names = new String[playerNo];
        boolean[] bots = new boolean[playerNo];
        for (int i = 0; i < playerNo; i++) {
            names[i] = "" + i;
            bots[i] = true;
        }
        MainGameLoop game = new MainGameLoop(playerNo, names, bots, true);
        System.out.println(game.getWinner());
    }

    // TODO Expansion
    public void expansion() {

    }

    // TODO back-propagation
    public void backProp() {

    }


}
