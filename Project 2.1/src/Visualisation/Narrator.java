package Visualisation;

import javax.swing.*;
import java.awt.*;

public class Narrator {
    private JLabel label= new JLabel();
    private String oldText = null;

    public Narrator() {
        JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        p2.setBackground(Color.LIGHT_GRAY);
        p2.add(label);
        Map.frame.add(p2, BorderLayout.BEFORE_FIRST_LINE);
    }
    public void addText(String text) {
        String newText = text;
        if(oldText == null) {
            label.setText("<html><br>" + ">> " + newText + "</html>");
        }
        else {
            label.setText("<html>" + "> " + oldText + "<br>" + ">> " + newText + "</html>");
        }
        oldText = newText;

        label.setVisible(true);
    }
}
