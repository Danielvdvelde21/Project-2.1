package Visualisation;

import BackEndStructure.Entities.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerTurnPanels {

    private String name;    // name of the current player
    private Color col;  // TODO get the color of the current player
    private JLabel nameLabel = new JLabel();
    private JPanel p2;

    public void setCurrentPlayerLabel(Player p) {
        this.name = p.getName();
        this.col = p.getColor();
        if(!p2.equals(null)){p2.remove(nameLabel);}
        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Courier New", Font.BOLD, 17));
        nameLabel.setVisible(true);
        p2.setBackground(col);
        p2.add(nameLabel);
    }

    public PlayerTurnPanels() {
        JLabel label = new JLabel("Player turn:");
        label.setFont(new Font("Courier New", Font.PLAIN, 14));

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setFont(new Font("Courier New", Font.PLAIN, 14));
        setButtonFunction(endTurnButton);

        JPanel p1 = new JPanel();
        p2 = new JPanel();
        JPanel p3 = new JPanel();
        p1.setBackground(Color.LIGHT_GRAY);
        p2.setBackground(Color.LIGHT_GRAY);
        p3.setBackground(Color.LIGHT_GRAY);

        p1.add(label);
        p3.add(endTurnButton);

        int yStart = 43;
        p1.setBounds(new Rectangle(Map.frameX, yStart, 200, 40));
        p2.setBounds(new Rectangle(Map.frameX, yStart+40, 200, 35));
        p3.setBounds(new Rectangle(Map.frameX, yStart+40+35, 200, 40));
        Map.frame.add(p1);
        Map.frame.add(p2);
        Map.frame.add(p3);
    }

    public void setButtonFunction(JButton endTurnButton) {
        //TODO add on action to the JButton "End Turn"

    }
}