import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.awt.*;
import java.util.ArrayList;

public class SimulatedGameTest {
    public static void main (String[] args) {
        Player one = new Player("1", 0, Color.red,true);
        Player two = new Player("2", 1, Color.blue,true);
        Player three = new Player("3", 2, Color.green,true);
        ArrayList<Player> order = new ArrayList<>();
        order.add(one);
        order.add(two);
        order.add(three);
        Graph graph = new Graph();
        SimulatedGameLoop game = new SimulatedGameLoop(graph, order);
    }
}
