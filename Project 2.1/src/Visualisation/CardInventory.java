package Visualisation;

import BackEndStructure.Entities.Cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class CardInventory {

    private JFrame f;

    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent windowEvent) {
                f.setState(JFrame.NORMAL);
                f.toFront();
                JOptionPane.showMessageDialog(f, "Error!");
            }
        };
    }

    public void getInventory(ArrayList<Card> cards) {
        f = new JFrame("Inventory");

        if(cards != null) { //fixing frame size
            f.setSize(140*cards.size()+90, 325);
            f.setLocation(1200-(140*cards.size()+90), 300);
        }

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(cards.size() > 4) {
                    f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);    //disables close operation
                }
                else {
                    Map.frame.setCursor(Cursor.getDefaultCursor()); //resets the cursor
                    Map.frame.removeWindowListener(Map.frame.getWindowListeners()[Map.frame.getWindowListeners().length-1]);    //removes the WindowAdapter getWindowAdapter()
                    e.getWindow().dispose();
                }
            }
        });
        f.setAlwaysOnTop(true);
        f.setResizable(false);
        Map.frame.addWindowListener(getWindowAdapter());    //gives error if Map.frame is activated
        Map.frame.setCursor(3); //wait cursor

        JPanel panell = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);
        panell.setBackground(Color.LIGHT_GRAY);

        for (Card card : cards) {   //adding all cards in panel1
            ImageIcon ii1 = card.getCardImageIcon();
            Image scaledImage = ii1.getImage().getScaledInstance(140, 195, Image.SCALE_SMOOTH);
            ii1 = new ImageIcon(scaledImage);
            JLabel cardLabel = new JLabel(ii1);
            cardLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cardLabel.addMouseListener(new MouseAdapter() {
                int i = 0;
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(i == 0) {    //selected
                        cardLabel.setBorder(BorderFactory.createLineBorder(Color.green, 2, true));
                        i = 1;
                    }
                    else if(i == 1) {
                        cardLabel.setBorder(null);
                        i = 0;
                    }
                }
            });
            panel1.add(cardLabel);
        }

        JButton turnInSetButton = new JButton("Turn in set");
        turnInSetButton.setFont(new Font("Courier New", Font.PLAIN, 14));
        JLabel errorLabel = new JLabel("[NOT A VALID SET SELECTED]");
        errorLabel.setForeground(Color.red.darker());
        errorLabel.setVisible(false);
        panell.add(errorLabel);

        turnInSetButton.addActionListener(e -> errorLabel.setVisible(true));    //TODO turn in set button
        panel2.add(turnInSetButton);

        f.add(panell, BorderLayout.PAGE_START);
        f.add(panel1, BorderLayout.CENTER);
        f.add(panel2, BorderLayout.AFTER_LAST_LINE);
        f.setVisible(true);
    }

    public CardInventory() {
        JButton cardInventory = new JButton("Card Inventory");
        cardInventory.setFont(new Font("Courier New", Font.PLAIN, 14));
        cardInventory.addActionListener(e -> getInventory(new ArrayList<>()));
        cardInventory.setEnabled(true);

        JPanel p = new JPanel();
        p.setBackground(Color.LIGHT_GRAY);
        p.add(cardInventory);
        p.setBounds(new Rectangle(Map.frameX, 218, 200, 40));
        Map.frame.add(p);
    }
}