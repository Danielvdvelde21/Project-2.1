package Visualisation;

import BackEndStructure.Entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerTurn {
    private final JLabel nameLabel = new JLabel();
    private JPanel p1 = new JPanel();
    private JPanel p2 = new JPanel();
    private JPanel p3 = new JPanel();

    private boolean turnEnded = false;

    public PlayerTurn() {
        JLabel label = new JLabel("Player turn:");
        label.setFont(new Font("Courier New", Font.PLAIN, 14));
        nameLabel.setFont(new Font("Courier New", Font.PLAIN, 14));

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setFont(new Font("Courier New", Font.PLAIN, 14));
        endTurnButton.addActionListener(e -> turnEnded = true);

        p1.setBackground(Color.LIGHT_GRAY);
        p2.setBackground(Color.LIGHT_GRAY);
        p3.setBackground(Color.LIGHT_GRAY);

        p1.add(label);
        p2.add(nameLabel);
        p3.add(endTurnButton);

        int yStart = 43;
        p1.setBounds(new Rectangle(Map.frameX, yStart, 200, 40));
        p2.setBounds(new Rectangle(Map.frameX, yStart+40, 200, 35));
        p3.setBounds(new Rectangle(Map.frameX, yStart+40+35, 200, 40));
        Map.frame.add(p1);
        Map.frame.add(p2);
        Map.frame.add(p3);
    }

    public void setPlayerTurn(Player player) {
        nameLabel.setText(player.getName());
        p2.setBackground(player.getColor());
    }

    public void resetTurn() {
        turnEnded = false;
    }

    public boolean hasTurnEnded() {
        return turnEnded;
    }
}