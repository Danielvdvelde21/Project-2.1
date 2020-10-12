package Visualisation;

import BackEndStructure.Graph.Territory;
import javax.swing.*;

public class FortifyTroops {

    private int troops;
    private boolean canceled = false ;

    public FortifyTroops(Territory from) {
        JFrame frame = new JFrame();
        String numberOfFortifiedTroops = JOptionPane.showInputDialog(frame, "Input number of troops to fortify with");

        if (numberOfFortifiedTroops == null) {
            canceled = true;
        } else {
            // If the input is not an integer we get an error
            if (!numberOfFortifiedTroops.matches("[0-9]+")) {
                JOptionPane.showMessageDialog(frame, "Please input a natural number!", "Error", JOptionPane.WARNING_MESSAGE);
                setVariables(new FortifyTroops(from));
            } else if (numberOfFortifiedTroops.equals("0")) {
                JOptionPane.showMessageDialog(frame, "You have to send atleast one troop to this territory!", "Error", JOptionPane.WARNING_MESSAGE);
                setVariables(new FortifyTroops(from));
            } else if (Integer.parseInt(numberOfFortifiedTroops) >= from.getNumberOfTroops())  {
                JOptionPane.showMessageDialog(frame, "You can't send that many troops to this territory!", "Error", JOptionPane.WARNING_MESSAGE);
                setVariables(new FortifyTroops(from));
            } else {
                troops = Integer.parseInt(numberOfFortifiedTroops);
            }
        }
    }

    private void setVariables(FortifyTroops w) {
        this.canceled = w.isCanceled();
        this.troops = w.getTroops();
    }

    public int getTroops() {
        return troops;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
