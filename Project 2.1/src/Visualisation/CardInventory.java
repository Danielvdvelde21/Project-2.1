package Visualisation;

import BackEndStructure.Entities.Cards.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardInventory {

    private static JFrame f = new JFrame("Inventory");
    private ArrayList<Card> cards;

    public void getInventory() {
        f.setDefaultCloseOperation(2);
        if(cards != null) {
            f.setSize(140*cards.size()+50, 300);
        }

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);

        for (Card card : cards) {   //adding all cards in panel1
            ImageIcon ii1 = card.getCardImageIcon();
            Image scaledImage = ii1.getImage().getScaledInstance(140, 195, Image.SCALE_SMOOTH);
            ii1 = new ImageIcon(scaledImage);
            JLabel cardLabel = new JLabel(ii1);
            panel1.add(cardLabel);
        }

        JButton turnInSetButton = new JButton("Turn in set");
        turnInSetButton.setFont(new Font("Courier New", Font.PLAIN, 14));
        turnInSetButton.addActionListener(null);    //TODO turn in set button

        panel2.add(turnInSetButton);

        f.setLocation(800, 350);
        f.add(panel1, BorderLayout.CENTER);
        f.add(panel2, BorderLayout.AFTER_LAST_LINE);
        f.setVisible(true);
    }

    public CardInventory() {
        JButton cardInventory = new JButton("Card Inventory");
        cardInventory.setFont(new Font("Courier New", Font.PLAIN, 14));
        cardInventory.addActionListener(e -> getInventory());

        JPanel p = new JPanel();
        p.setBackground(Color.LIGHT_GRAY);
        p.add(cardInventory);
        p.setBounds(new Rectangle(Map.frameX, 218, 200, 40));
        Map.frame.add(p);
    }

    public void setCards(ArrayList<Card> c) {
        cards = c;
    }
}