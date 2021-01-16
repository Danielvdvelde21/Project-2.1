package Visualisation.Map.Components;

import BackEndStructure.Graph.Territory;
import Visualisation.Map.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TerritoryCaptured {

    private final JFrame f = new JFrame();
    private boolean validNumberInserted = false;
    private int troops;

    public TerritoryCaptured(Territory t) {
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JTextField input = new JTextField(20);
        JLabel l = new JLabel("Input number of troops to capture new territory with");
        JButton button = new JButton("OK");

        button.addActionListener(actionEvent -> {
            String text = input.getText();
            if (!text.matches("[0-9]+")) {
                JOptionPane.showMessageDialog(f, "Please input a natural number!", "Error", JOptionPane.WARNING_MESSAGE);
            } else if (Integer.parseInt(text) == 0) {
                JOptionPane.showMessageDialog(f, "You have to send atleast one troop to this territory!", "Error", JOptionPane.WARNING_MESSAGE);
            } else if (Integer.parseInt(text) >= t.getNumberOfTroops())  {
                JOptionPane.showMessageDialog(f, "You can't send that many troops to this territory!", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                validNumberInserted = true;
                troops = Integer.parseInt(text);
                Map.frame.setCursor(Cursor.getDefaultCursor());
                Map.frame.removeWindowListener(Map.frame.getWindowListeners()[Map.frame.getWindowListeners().length-1]);
                f.dispose();
            }
        });

        input.setBounds(50,50,200,100);

        panel.add(l);
        panel.add(input);
        panel2.add(button);

        f.add(panel);
        f.add(panel2, BorderLayout.SOUTH);
        f.setBounds(800, 500, 400, 150);

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(!validNumberInserted) {
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
        Map.frame.setCursor(Cursor.getPredefinedCursor(3)); //wait cursor
        f.setVisible(true);
    }

    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent windowEvent) {
                f.setState(JFrame.NORMAL);
                f.toFront();
                JOptionPane.showMessageDialog(f, "Please choose how many troops will be sent to your new territory");
            }
        };
    }

    public int getTroops() {
        return troops;
    }

    public boolean getValidNumberInserted() {
        return validNumberInserted;
    }

}
