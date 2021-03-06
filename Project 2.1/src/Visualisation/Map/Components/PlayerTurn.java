package Visualisation.Map.Components;

import BackEndStructure.Entities.Player;
import Visualisation.Map.Map;

import javax.swing.*;
import java.awt.*;

public class PlayerTurn {
    private final JLabel nameLabel = new JLabel();
    private final JPanel p2 = new JPanel();

    private boolean turnEnded = false;

    public PlayerTurn() {
        if(!AI.GlobalVariables.SIMULATED) {
            JLabel label = new JLabel("Player turn:");
            label.setFont(new Font("Courier New", Font.BOLD, 16));
            label.setForeground(Color.white);
            nameLabel.setFont(new Font("Courier New", Font.BOLD, 16));
            nameLabel.setForeground(Color.white);

            JButton endTurnButton = new JButton("End Turn");
            endTurnButton.setFont(new Font("Courier New", Font.BOLD, 16));
            endTurnButton.setBackground(new Color(80, 100, 182));
            endTurnButton.setForeground(Color.WHITE);
            endTurnButton.setPreferredSize(new Dimension(170, 30));
            endTurnButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            endTurnButton.addActionListener(e -> turnEnded = true);

            JPanel p1 = new JPanel();
            p1.setBackground(Map.themeColor);
            p2.setBackground(Map.themeColor);
            JPanel p3 = new JPanel();
            p3.setBackground(Map.themeColor);

            p1.add(label);
            p2.add(nameLabel);
            p3.add(endTurnButton);

            int yStart = 43;
            p1.setBounds(new Rectangle(Map.frameX, yStart, 300, 40));
            p2.setBounds(new Rectangle(Map.frameX, yStart + 40, 300, 35));
            p3.setBounds(new Rectangle(Map.frameX, yStart + 40 + 35, 300, 100));
            Map.frame.add(p1);
            Map.frame.add(p2);
            Map.frame.add(p3);
        }
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