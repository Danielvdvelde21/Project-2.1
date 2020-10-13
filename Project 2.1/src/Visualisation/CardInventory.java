package Visualisation;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Cards.SetHandler;
import BackEndStructure.Entities.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CardInventory {

    private final JFrame f = new JFrame("Inventory");

    private final SetHandler setHandler = new SetHandler();

    private Player currentPlayer;
    private boolean allowTrading;
    private boolean attacking;

    private ArrayList<Card> selectedCards = new ArrayList<>();

    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                super.windowClosing(we);
                JOptionPane.showMessageDialog(f, "Can't Exit");
            }
            @Override
            public void windowIconified(WindowEvent we) {
                f.setState(JFrame.NORMAL);
                JOptionPane.showMessageDialog(f, "Can't Minimize");
            }
        };
    }

    public void getInventory() {
        ArrayList<Card> playerCards = currentPlayer.getHand();

        f.setDefaultCloseOperation(2);
        f.setAlwaysOnTop(true);
        f.setResizable(false);

        if(playerCards != null) {
            f.setSize(140*playerCards.size()+90, 325);
        }
        if(playerCards.size() > 4) {
            f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f.addWindowListener(getWindowAdapter());    //can't exit or minimize
            // Map.frame.se //TODO set Map.frame as not editable
        }

        JPanel panell = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);
        panell.setBackground(Color.LIGHT_GRAY);

        for (Card card : playerCards) {   //adding all cards in panel1
            ImageIcon ii1 = card.getCardImageIcon();
            Image scaledImage = ii1.getImage().getScaledInstance(140, 195, Image.SCALE_SMOOTH);
            ii1 = new ImageIcon(scaledImage);
            JLabel cardLabel = new JLabel(ii1);
            cardLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cardLabel.addMouseListener(new MouseAdapter() {
                int i = 0;
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(i == 0) {
                        cardLabel.setBorder(BorderFactory.createLineBorder(Color.green, 2, true));
                        i = 1;
                        selectedCards.add(card);
                    }
                    else if(i == 1) {
                        cardLabel.setBorder(null);
                        i = 0;
                        selectedCards.remove(card);
                    }
                }
            });
            panel1.add(cardLabel);
        }

        JButton turnInSetButton = new JButton("Turn in set");
        turnInSetButton.setFont(new Font("Courier New", Font.PLAIN, 14));
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.red.darker());
        errorLabel.setVisible(false);
        panell.add(errorLabel);
        turnInSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (allowTrading) {
                    if(setHandler.isSet(selectedCards) && selectedCards.size() == 3) {
                        currentPlayer.getHand().removeAll(selectedCards);
                        if (attacking) {
                            allowTrading = false;
                        }
                        } else {
                        errorLabel.setText("[NOT A VALID SET SELECTED]");
                        errorLabel.setVisible(true);
                    }
                } else {
                    errorLabel.setText("[YOU MAY NOT TRADE RIGHT NOW]");
                    errorLabel.setVisible(true);
                }
            }
        });

        panel2.add(turnInSetButton);

        f.setLocation(800, 350);
        f.add(panell, BorderLayout.PAGE_START);
        f.add(panel1, BorderLayout.CENTER);
        f.add(panel2, BorderLayout.AFTER_LAST_LINE);
        f.setVisible(true);
    }

    public CardInventory() {
        JButton cardInventory = new JButton("Card Inventory");
        cardInventory.setFont(new Font("Courier New", Font.PLAIN, 14));
        cardInventory.addActionListener(e -> {getInventory();
        });

        JPanel p = new JPanel();
        p.setBackground(Color.LIGHT_GRAY);
        p.add(cardInventory);
        p.setBounds(new Rectangle(Map.frameX, 218, 200, 40));
        Map.frame.add(p);
    }

    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    public void tradingAllowed(boolean b) {
        allowTrading = b;
    }

    public void setAttacking(boolean b) {
        attacking = b;
    }
}