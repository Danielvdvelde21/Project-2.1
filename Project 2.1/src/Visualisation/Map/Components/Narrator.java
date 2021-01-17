package Visualisation.Map.Components;

import Visualisation.Map.Map;

import javax.swing.*;
import java.awt.*;

public class Narrator {
    private final JLabel label = new JLabel();
    private String oldText = null;

    public Narrator() {
        if(!AI.GlobalVariables.SIMULATED) {
            JPanel p2 = new JPanel();
            p2.setBackground(Map.themeColor);
            label.setFont(new Font("Courier New", Font.BOLD, 16));
            label.setForeground(Color.white);
            label.setLocation(200, 50);
            p2.add(label);
            Map.frame.add(p2, BorderLayout.BEFORE_FIRST_LINE);
        }
    }

    public void addText(String text) {
        /*System.out.println(text);
        String newText = text;
        if (oldText == null) {
            label.setText("<html><br>" + ">> " + newText + "</html>");
        } else {
            label.setText("<html>" + "> " + oldText + "<br>" + ">> " + newText + "</html>");
        }
        oldText = newText;

        label.setVisible(true);*/

    }


}
