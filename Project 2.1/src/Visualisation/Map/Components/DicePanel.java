package Visualisation.Map.Components;

import Visualisation.Map.Map;

import javax.swing.*;
import java.awt.*;

public class DicePanel {

    private int numberOfAttackingDice = 1;
    private int numberOfDefendingDice = 2;

    private final Die attackDice1 = new Die();
    private final Die attackDice2 = new Die();
    private final Die attackDice3 = new Die();
    private final Die defDice1 = new Die();
    private final Die defDice2 = new Die();

    private final JPanel p1 = new JPanel();   // offence label
    private final JPanel p1a = new JPanel();  // dice
    private final JPanel p1b = new JPanel();  // throw button
    private final JPanel p2 = new JPanel();   // defence label
    private final JPanel p2a = new JPanel();  // dice
    private final JPanel p2b = new JPanel();  // throw button

    private final JLabel playerOrder = new JLabel("Player Order Dice:");
    private final JLabel offence = new JLabel("offence:");
    private final JLabel defence = new JLabel("Defence:");

    private final JButton next = new JButton(">>");
    private final JButton next1 = new JButton(">>");
    private final JButton previous = new JButton("<<");
    private final JButton previous1 = new JButton("<<");

    private final JButton attackDiceRoll = new JButton("Throw");
    private final JButton defendDiceRoll = new JButton("Throw");

    private boolean diceRolled;

    public DicePanel() {
        playerOrder.setFont(new Font("Courier New", Font.BOLD, 14));
        offence.setFont(new Font("Courier New", Font.BOLD, 14));

        defence.setFont(new Font("Courier New", Font.BOLD, 14));

        p1.setBackground(Color.LIGHT_GRAY);
        p1a.setBackground(Color.LIGHT_GRAY);
        p1b.setBackground(Color.LIGHT_GRAY);
        p2.setBackground(Color.LIGHT_GRAY);
        p2a.setBackground(Color.LIGHT_GRAY);
        p2b.setBackground(Color.LIGHT_GRAY);

        next.setFont(new Font("Courier News", Font.PLAIN, 10));
        next.setBackground(null);
        next.setBorderPainted(false);
        next.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        next.setEnabled(true);
        next.setPreferredSize(next.getPreferredSize());

        previous.setFont(new Font("Courier News", Font.PLAIN, 10));
        previous.setBackground(null);
        previous.setBorderPainted(false);
        previous.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        previous.setEnabled(true);
        previous.setPreferredSize(previous.getPreferredSize());

        previous.addActionListener(actionEvent ->  {
            if (p1a.getComponents()[p1a.getComponents().length-1].equals(next)) {
                p1a.removeAll();
                p1a.add(attackDice1);
                p1a.add(next);
                numberOfAttackingDice = 1;
            }
            else {
                p1a.removeAll();
                p1a.add(previous);
                p1a.add(attackDice1);
                p1a.add(attackDice2);
                p1a.add(next);
                numberOfAttackingDice = 2;
            }

            p1a.repaint();
            p1a.setVisible(true);
            Map.frame.repaint();
            Map.frame.setVisible(true);
        });
        next.addActionListener(actionEvent ->  {
            if (p1a.getComponents().length == 2) {
                p1a.removeAll();
                p1a.add(previous);
                p1a.add(attackDice1);
                p1a.add(attackDice2);
                p1a.add(next);
                numberOfAttackingDice = 2;
            }
            else if (p1a.getComponents().length == 4 && p1a.getComponents()[p1a.getComponents().length-1].equals(next)) {
                p1a.removeAll();
                p1a.add(previous);
                p1a.add(attackDice1);
                p1a.add(attackDice2);
                p1a.add(attackDice3);
                numberOfAttackingDice = 3;
            }
            p1a.repaint();
            p1a.setVisible(true);
            Map.frame.repaint();
            Map.frame.setVisible(true);
        });

        next1.setFont(new Font("Courier News", Font.PLAIN, 10));
        next1.setBackground(null);
        next1.setBorderPainted(false);
        next1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        next1.setEnabled(true);
        next1.setPreferredSize(next.getPreferredSize());

        previous1.setFont(new Font("Courier News", Font.PLAIN, 10));
        previous1.setBackground(null);
        previous1.setBorderPainted(false);
        previous1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        previous1.setEnabled(true);
        previous1.setPreferredSize(previous.getPreferredSize());

        previous1.addActionListener(actionEvent ->  {
            p2a.removeAll();
            p2a.add(defDice1);
            p2a.add(next1);
            numberOfDefendingDice = 1;

            p2a.repaint();
            p2a.setVisible(true);
            Map.frame.repaint();
            Map.frame.setVisible(true);
        });
        next1.addActionListener(actionEvent ->  {
            p2a.removeAll();
            p2a.add(previous1);
            p2a.add(defDice1);
            p2a.add(defDice2);
            numberOfDefendingDice = 2;

            p2a.repaint();
            p2a.setVisible(true);
            Map.frame.repaint();
            Map.frame.setVisible(true);
        });

        attackDiceRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        attackDiceRoll.addActionListener(actionEvent -> {
            rollAttackDie();
        });

        defendDiceRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        defendDiceRoll.addActionListener(actionEvent -> {
            rollDefDie();
        });

        p1.add(playerOrder);
        p1a.add(attackDice1);
        p1b.add(attackDiceRoll);

        p1.setBounds(new Rectangle(Map.frameX, 218+100, 300, 40));
        p1a.setBounds(new Rectangle(Map.frameX, 218+140, 300, 80));
        p1b.setBounds(new Rectangle(Map.frameX, 218+140+80, 300, 120));
        p2.setBounds(new Rectangle(Map.frameX, 218+140+200, 300, 40));
        p2a.setBounds(new Rectangle(Map.frameX, 218+380, 300, 80));
        p2b.setBounds(new Rectangle(Map.frameX, 218+380+80, 300, 135));
        Map.frame.add(p1);
        Map.frame.add(p1a);
        Map.frame.add(p1b);
        Map.frame.add(p2);
        Map.frame.add(p2a);
        Map.frame.add(p2b);
    }

    public void playerOrderObtained() {
        p1.add(offence);
        p1.remove(playerOrder);
        p1a.add(next);
        p2.add(defence);
        p2a.add(previous1);
        p2a.add(defDice1);
        p2a.add(defDice2);
        p2b.add(defendDiceRoll);
    }

    public boolean isDiceRolled() {
        if (diceRolled) {
            resetDiceRolled();
            return true;
        }
        return false;
    }

    private void resetDiceRolled() {
        diceRolled = false;
    }

    public int getEyesPlayerOrderDice() {
        return attackDice1.getDieValue();
    }

    private void rollAttackDie() {
        switch (numberOfAttackingDice) {
            case 1:
                // For player order
                diceRolled = true;
                attackDice1.rollDie();
                break;
            case 2:
                attackDice1.rollDie();
                attackDice2.rollDie();
                break;
            case 3:
                attackDice1.rollDie();
                attackDice2.rollDie();
                attackDice3.rollDie();
        }
    }

    private void rollDefDie() {
        switch (numberOfDefendingDice) {
            case 1:
                defDice1.rollDie();
                break;
            case 2:
                defDice1.rollDie();
                defDice2.rollDie();
        }
    }

    public int getNumberOfAttackingDice() {
        return numberOfAttackingDice;
    }

    public int getNumberOfDefendingDice() {
        return numberOfDefendingDice;
    }

    public int[] getAttackDieValues() {
        return new int[] {attackDice1.getDieValue(), attackDice2.getDieValue(), attackDice3.getDieValue()};
    }

    public int[] getDefendDieValues() {
        return new int[] {defDice1.getDieValue(), defDice1.getDieValue()};
    }

    public static class Die extends JComponent {

        private static final int DOT_DIAM = 6;  //diameter of the dots
        private int dieValue;
        private final Dimension dimension = new Dimension(40,40);

        public Die() {
            setPreferredSize(dimension);
            // Set the die to a random number when initialized
            rollDie();
        }

        //returns random value from 1 to 6 and draws the dice accordingly
        public void rollDie() {
            dieValue = (int) ( 6 * Math.random() + 1);
            repaint();
        }

        public int getDieValue() {
            return dieValue;
        }

        @Override
        public void paintComponent(Graphics g) {
            int w = dimension.width;
            int h = dimension.height;

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.red); //background
            g2.fillRect(0, 0, w, h);
            g2.setColor(Color.BLACK);
            g2.drawRect(0, 0, w-1, h-1);    //border

            switch (dieValue) {
                case 1:
                    drawDot(g2, w/2, h/2);
                    break;
                case 2:
                    drawDot(g2, w/4, h/4);
                    drawDot(g2, 3*w/4, 3*h/4);
                    break;
                case 3:
                    drawDot(g2, w/2, h/2);
                    drawDot(g2, w/4, h/4);
                    drawDot(g2, 3*w/4, 3*h/4);
                    break;
                case 5:
                    drawDot(g2, w/2, h/2);
                case 4:
                    drawDot(g2, w/4, h/4);
                    drawDot(g2, 3*w/4, 3*h/4);
                    drawDot(g2, 3*w/4, h/4);
                    drawDot(g2, w/4, 3*h/4);
                    break;
                case 6:
                    drawDot(g2, w/4, h/4);
                    drawDot(g2, 3*w/4, 3*h/4);
                    drawDot(g2, 3*w/4, h/4);
                    drawDot(g2, w/4, 3*h/4);
                    drawDot(g2, w/4, h/2);
                    drawDot(g2, 3*w/4, h/2);
                    break;
            }
        }

        private void drawDot(Graphics2D g2, int posX, int posY) {
            g2.fillOval(posX-DOT_DIAM/2, posY-DOT_DIAM/2, DOT_DIAM, DOT_DIAM);
        }
    }
}
