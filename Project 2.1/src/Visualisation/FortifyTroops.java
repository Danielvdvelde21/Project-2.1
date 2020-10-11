package Visualisation;
import BackEndStructure.Entities.Player;

import javax.swing.*;

public class FortifyTroops {

    JFrame frame;

    public FortifyTroops() {
        frame = new JFrame();
        String numberOfFortifiedTroops = JOptionPane.showInputDialog(frame, "Input number of troops");

        // If the input is not an integer we get an error
        if (!numberOfFortifiedTroops.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(frame, "Please input an integer", "Error", JOptionPane.WARNING_MESSAGE);
            new FortifyTroops();
        }
    }
}
