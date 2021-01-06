package Visualisation.Map.Components;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Cards.SetHandler;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import Visualisation.Map.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CardInventory {
    private boolean simulatedGame;

    private JFrame f;

    private final SetHandler setHandler = new SetHandler();
    private Game game;

    private Player currentPlayer;
    private boolean menuClosed;
    private boolean allowTrading;
    private boolean attacking;

    private ArrayList<Card> selectedCards = new ArrayList<>();
    private ArrayList<JLabel> selectedLabels = new ArrayList<>();

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
        if (!simulatedGame) {
            ArrayList<Card> playerCards = currentPlayer.getHand();
            menuClosed = true;
            f = new JFrame("Inventory");

            if (playerCards != null) { //fixing frame size
                f.setSize(140 * playerCards.size() + 90, 325);
                f.setLocation(1200 - (140 * playerCards.size() + 90), 300);
            }

            f.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    if (playerCards.size() > 4 && allowTrading) {
                        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);    //disables close operation
                    } else {
                        menuClosed = false;
                        selectedCards.clear();
                        Map.frame.setCursor(Cursor.getDefaultCursor()); //resets the cursor
                        Map.frame.removeWindowListener(Map.frame.getWindowListeners()[Map.frame.getWindowListeners().length - 1]);    //removes the WindowAdapter getWindowAdapter()
                        e.getWindow().dispose();
                    }
                }
            });
            f.setAlwaysOnTop(true);
            f.setResizable(false);
            Map.frame.addWindowListener(getWindowAdapter());    //gives error if Map.frame is activated
            Map.frame.setCursor(Cursor.getPredefinedCursor(3)); //wait cursor

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
                        if (i == 0) {    //selected
                            cardLabel.setBorder(BorderFactory.createLineBorder(Color.green, 2, true));
                            i = 1;
                            selectedCards.add(card);
                            selectedLabels.add(cardLabel);
                        } else if (i == 1) {
                            cardLabel.setBorder(null);
                            i = 0;
                            selectedCards.remove(card);
                            selectedLabels.remove(cardLabel);
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

            turnInSetButton.addActionListener(actionEvent -> {
                if (allowTrading) {
                    if (attacking) {
                        if (selectedCards.size() == 3 && setHandler.isSet(game, currentPlayer, selectedCards)) {
                            // Return cards to stack
                            game.getCardStack().returnCards(selectedCards);
                            // Remove cards from player hand
                            currentPlayer.getHand().removeAll(selectedCards);
                            // Deselect card list
                            selectedCards.clear();
                            // Player has 1 more completed set
                            currentPlayer.incrementSetsOwned();

                            f.remove(panel1);
                            for (JLabel label : selectedLabels) {
                                panel1.remove(label);
                            }
                            f.add(panel1);
                            f.repaint();
                            f.setVisible(true);
                        } else {
                            errorLabel.setText("[NOT A VALID SET SELECTED]");
                            errorLabel.setVisible(true);
                        }
                    } else {
                        if (selectedCards.size() == 3 && setHandler.isSet(game, currentPlayer, selectedCards)) {
                            // Return cards to stack
                            game.getCardStack().returnCards(selectedCards);
                            // Remove cards from player hand
                            currentPlayer.getHand().removeAll(selectedCards);
                            // Deselect card list
                            selectedCards.clear();
                            // Player has 1 more completed set
                            currentPlayer.incrementSetsOwned();
                            // Allow trading is set to false (player can only turn in one set
                            attacking = false;
                            allowTrading = false;

                            f.remove(panel1);
                            for (JLabel label : selectedLabels) {
                                panel1.remove(label);
                            }
                            f.add(panel1);
                            f.repaint();
                            f.setVisible(true);
                        } else {
                            errorLabel.setText("[NOT A VALID SET SELECTED]");
                            errorLabel.setVisible(true);
                        }
                    }
                } else {
                    errorLabel.setText("[YOU MAY NOT TRADE RIGHT NOW]");
                    errorLabel.setVisible(true);
                }
            });

            panel2.add(turnInSetButton);
            f.add(panell, BorderLayout.PAGE_START);
            f.add(panel1, BorderLayout.CENTER);
            f.add(panel2, BorderLayout.AFTER_LAST_LINE);
            f.setVisible(true);
        }
    }

    public CardInventory() {
        if (!simulatedGame) {
            JButton cardInventory = new JButton("Card Inventory");
            cardInventory.setFont(new Font("Courier New", Font.BOLD, 16));
            cardInventory.setBackground(new Color(80, 100, 182));
            cardInventory.setForeground(Color.WHITE);
            cardInventory.setPreferredSize(new Dimension(170, 30));
            cardInventory.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cardInventory.addActionListener(e -> getInventory());
            cardInventory.setEnabled(true);

            JPanel p = new JPanel();
            p.setBackground(Map.themeColor);
            p.add(cardInventory);
            p.setBounds(new Rectangle(Map.frameX, 218, 300, 100));
            Map.frame.add(p);
        }
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

    public void setGame(Game g) {
        game = g;
    }

    public boolean getMenuClosed() { return menuClosed; }

    public void setSimulatedGame(boolean b) { simulatedGame = b; }
}