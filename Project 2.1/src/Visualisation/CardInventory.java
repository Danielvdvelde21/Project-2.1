package Visualisation;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Cards.SetHandler;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CardInventory {

    private JFrame f;

    private final SetHandler setHandler = new SetHandler();
    private Game game;

    private Player currentPlayer;
    private boolean allowTrading;
    private boolean attacking;
    private boolean tradingCompleted;

    private ArrayList<Card> selectedCards = new ArrayList<>();

    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent windowEvent) {
                f.setState(JFrame.NORMAL);
                f.toFront();
                JOptionPane.showMessageDialog(f, "Please trade in a set first or Close your card inventory");
            }
        };
    }

    public void getInventory() {
        ArrayList<Card> playerCards = currentPlayer.getHand();
        f = new JFrame("Inventory");

        if(playerCards != null) { //fixing frame size
            f.setSize(140*playerCards.size()+90, 325);
            f.setLocation(1200-(140*playerCards.size()+90), 300);
        }

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(playerCards.size() > 4) {
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
                    if(i == 0) {    //selected
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
        JLabel errorLabel = new JLabel("[NOT A VALID SET SELECTED]");
        errorLabel.setForeground(Color.red.darker());
        errorLabel.setVisible(false);
        panell.add(errorLabel);

        turnInSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (allowTrading) {
                    if (attacking) {
                        if (selectedCards.size() == 3 && setHandler.isSet(game, currentPlayer, selectedCards)) {
                            currentPlayer.getHand().removeAll(selectedCards);
                            currentPlayer.incrementSetsOwned();
                            tradingCompleted = true;
                        } else {
                            errorLabel.setText("[NOT A VALID SET SELECTED]");
                            errorLabel.setVisible(true);
                        }
                    } else {
                        if (selectedCards.size() == 3 && setHandler.isSet(game, currentPlayer, selectedCards)) {
                            currentPlayer.getHand().removeAll(selectedCards);
                            currentPlayer.incrementSetsOwned();
                            if (currentPlayer.getHand().size() < 3) {
                                tradingCompleted = true;
                            }
                        } else {
                            errorLabel.setText("[NOT A VALID SET SELECTED]");
                            errorLabel.setVisible(true);
                        }
                    }
                } else {
                    errorLabel.setText("[YOU MAY NOT TRADE RIGHT NOW]");
                    errorLabel.setVisible(true);
                }
            }});

        panel2.add(turnInSetButton);
        f.add(panell, BorderLayout.PAGE_START);
        f.add(panel1, BorderLayout.CENTER);
        f.add(panel2, BorderLayout.AFTER_LAST_LINE);
        f.setVisible(true);
    }

    public CardInventory() {
        JButton cardInventory = new JButton("Card Inventory");
        cardInventory.setFont(new Font("Courier New", Font.PLAIN, 14));
        cardInventory.addActionListener(e -> getInventory());
        cardInventory.setEnabled(true);

        JPanel p = new JPanel();
        p.setBackground(Color.LIGHT_GRAY);
        p.add(cardInventory);
        p.setBounds(new Rectangle(Map.frameX, 218, 200, 40));
        Map.frame.add(p);
    }

    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    public void attacking(boolean b) {
        attacking = b;
    }

    public void tradingAllowed(boolean b) {
        allowTrading = b;
    }

    public boolean isTradingCompleted() {
        return tradingCompleted;
    }

    public void setTradingCompleted(boolean b) {
        tradingCompleted = b;
    }

    public void setGame(Game g) {
        game = g;
    }
}