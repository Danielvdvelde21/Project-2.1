package Visualisation;

import javax.swing.*;
import java.awt.*;

public class DicePanel {
    private JPanel p1 = new JPanel();
    private JPanel p2 = new JPanel();

    public DicePanel() {
        JLabel offence = new JLabel("Offence:");
        offence.setFont(new Font("Courier New", Font.PLAIN, 14));
        JLabel defence = new JLabel("Defence:");
        defence.setFont(new Font("Courier New", Font.PLAIN, 14));

        p1.setBackground(Color.LIGHT_GRAY);
        p2.setBackground(Color.LIGHT_GRAY);

        p1.add(offence);
        p2.add(defence);

        p1.setBounds(new Rectangle(Map.frameX, 218+100, 200, 200));
        p2.setBounds(new Rectangle(Map.frameX, 218+300, 200, 300));
        Map.frame.add(p1);
        Map.frame.add(p2);
    }
}
