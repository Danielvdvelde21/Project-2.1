package Visualisation.Map;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    // -----------------------------------------------------------------------------------------------------------------
    // Disable graphics
    private boolean simulatedGame;

    // -----------------------------------------------------------------------------------------------------------------
    // Variables for MainGameLoop
    private int territoryNumber = -1; // Int that represents a territory
    private int buttonCount = -1; // The first button will have count 0 (array indexing)

    // -----------------------------------------------------------------------------------------------------------------
    // Variables for creating the map
    private static Insets insets;
    private static Dimension size;
    private static BufferedImage wPic;

    // private static int frameX = 1600;
    // private static int frameY = 900;

    public static int frameX=1100;
    public static int frameY=700;

    private ArrayList<JButton> buttonList = new ArrayList<JButton>();
    private ArrayList<JLabel> labelList = new ArrayList<>();

    public static JFrame frame = new JFrame("RISK");
    public static Color themeColor = Color.darkGray;

    // -----------------------------------------------------------------------------------------------------------------
    public Map(boolean b) {
        simulatedGame = b;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Label for how many troops a territory has
    private JLabel troopLabel(JPanel p, String name, int posX, int posY) {
        int numOfTroops = 0;

        JLabel nt = new JLabel();
        nt.setOpaque(true);
        nt.setText(String.valueOf(numOfTroops));

        Color color = Color.lightGray;
        Border line = BorderFactory.createLineBorder(color, 2, true);
        Border compound = BorderFactory.createCompoundBorder(line, line);
        nt.setBorder(compound);
        nt.setBackground(Color.lightGray);

        size = nt.getPreferredSize();
        nt.setBounds(((posX * frameX) / 1100) - size.width / 2, (((posY * frameY) / 700) - size.height / 2) + 20, size.width + 3, size.height - 3);
        return nt;
    }

    // Update the troopCount label color
    public void setTroopCountColor(int territoryNumber, Player player) {
        if(!simulatedGame) {
            Border line = BorderFactory.createLineBorder(player.getColor(), 2, true);
            Border compound = BorderFactory.createCompoundBorder(line, line);
            labelList.get(territoryNumber).setBorder(compound);
        }
    }

    // Update the troopCount label
    public void updateTroopCount(int territoryNumber, int troopCount) {
        if(!simulatedGame) {
            // labelList is alphabetically sorted
            labelList.get(territoryNumber).setText(String.valueOf(troopCount));
            size = labelList.get(territoryNumber).getPreferredSize();
            labelList.get(territoryNumber).setSize(size.width + 3, size.height - 3);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Button for selecting territories
    private JButton createButton(JPanel p, String name, int posX, int posY) {
        // New button created, buttonCount goes up
        buttonCount++;
        JButton b = new JButton(name);

        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        // b.repaint();
        insets = p.getInsets();
        size = b.getPreferredSize();
        b.setBounds(((posX * frameX) / 1100) - size.width / 2, ((posY * frameY) / 700) - size.height / 2, size.width, size.height);

        b.addActionListener(new ActionListener() {
            // Each territory has an int to represent it (which is based on the buttonCount
            public final int territoryCount = buttonCount;

            public void actionPerformed(ActionEvent e) {
                territoryNumber = territoryCount;
            }
        });
        return b;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Creates the map
    public void createMap() {
        if (!simulatedGame) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocation(80, 7);
            frame.setSize(frameX + 300, frameY + 100);
            frame.setResizable(false);
            JPanel p = new JPanel();

            p.setLayout(null);
            p.setBounds(new Rectangle(0, 0, 200, 200));
            p.setBackground(Color.LIGHT_GRAY);

            ImageIcon imageIcon = new ImageIcon("src/res/risk-board.png"); // load the image to a imageIcon

            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(frameX, frameY, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon = new ImageIcon(newimg);

            JLabel imgLabel = new JLabel(imageIcon);
            imgLabel.setBounds(0, 0, frameX, frameY);

            // Sorted on alphabet
            buttonList.add(createButton(p, "AFGHANISTAN", 745, 252));
            labelList.add(troopLabel(p, "AFGHANISTAN", 745, 252));
            buttonList.add(createButton(p, "ALASKA", 87, 113));
            labelList.add(troopLabel(p, "ALASKA", 87, 113));
            buttonList.add(createButton(p, "ALBERTA", 177, 165));
            labelList.add(troopLabel(p, "ALBERTA", 177, 165));
            buttonList.add(createButton(p, "ARGENTINA", 293, 539));
            labelList.add(troopLabel(p, "ARGENTINA", 293, 539));
            buttonList.add(createButton(p, "BRAZIL", 341, 443));
            labelList.add(troopLabel(p, "BRAZIL", 341, 443));
            buttonList.add(createButton(p, "CENTRAL AMERICA", 192, 327));
            labelList.add(troopLabel(p, "CENTRAL AMERICA", 192, 327));
            buttonList.add(createButton(p, "CHINA", 873, 302));
            labelList.add(troopLabel(p, "CHINA", 873, 302));
            buttonList.add(createButton(p, "CENTRAL AFRICA", 594, 503));
            labelList.add(troopLabel(p, "CENTRAL AFRICA", 594, 503));
            buttonList.add(createButton(p, "EAST AFRICA", 656, 472));
            labelList.add(troopLabel(p, "EAST AFRICA", 656, 472));
            buttonList.add(createButton(p, "<html><center>EASTERN<br>AUSTRALIA</center></html>", 1014, 557));
            labelList.add(troopLabel(p, "<html><center>EASTERN<br>AUSTRALIA</center></html>", 1014, 557));
            buttonList.add(createButton(p, "<html><center>EASTERN<br>UNITED STATES</center></html>", 247, 262));
            labelList.add(troopLabel(p, "<html><center>EASTERN<br>UNITED STATES</center></html>", 247, 262));
            buttonList.add(createButton(p, "EGYPT", 596, 392));
            labelList.add(troopLabel(p, "EGYPT", 596, 392));
            buttonList.add(createButton(p, "GREAT BRITAIN", 457, 216));
            labelList.add(troopLabel(p, "GREAT BRITAIN", 457, 216));
            buttonList.add(createButton(p, "GREENLAND", 385, 73));
            labelList.add(troopLabel(p, "GREENLAND", 385, 73));
            buttonList.add(createButton(p, "ICELAND", 474, 143));
            labelList.add(troopLabel(p, "ICELAND", 474, 143));
            buttonList.add(createButton(p, "INDIA", 805, 351));
            labelList.add(troopLabel(p, "INDIA", 805, 351));
            buttonList.add(createButton(p, "INDONESIA", 901, 489));
            labelList.add(troopLabel(p, "INDONESIA", 901, 489));
            buttonList.add(createButton(p, "IRKUTSK", 881, 183));
            labelList.add(troopLabel(p, "IRKUTSK", 881, 183));
            buttonList.add(createButton(p, "JAPAN", 1002, 249));
            labelList.add(troopLabel(p, "JAPAN", 1002, 249));
            buttonList.add(createButton(p, "KAMCHATKA", 984, 95));
            labelList.add(troopLabel(p, "KAMCHATKA", 984, 95));
            buttonList.add(createButton(p, "MADAGASCAR", 697, 595));
            labelList.add(troopLabel(p, "MADAGASCAR", 697, 595));
            buttonList.add(createButton(p, "MIDDLE EAST", 695, 337));
            labelList.add(troopLabel(p, "MIDDLE EAST", 695, 337));
            buttonList.add(createButton(p, "MONGOLIA", 891, 238));
            labelList.add(troopLabel(p, "MONGOLIA", 891, 238));
            buttonList.add(createButton(p, "<html><center>NEW<br>GUINEA</center></html>", 999, 468));
            labelList.add(troopLabel(p, "<html><center>NEW<br>GUINEA</center></html>", 999, 468));
            buttonList.add(createButton(p, "NORTH AFRICA", 527, 428));
            labelList.add(troopLabel(p, "NORTH AFRICA", 527, 428));
            buttonList.add(createButton(p, "NORTH WEST TERRITORY", 190, 109));
            labelList.add(troopLabel(p, "NORTH WEST TERRITORY", 190, 109));
            buttonList.add(createButton(p, "<html><center>NORTHERN<br>EUROPE</center></html>", 555, 232));
            labelList.add(troopLabel(p, "<html><center>NORTHERN<br>EUROPE</center></html>", 555, 232));
            buttonList.add(createButton(p, "ONTARIO", 246, 180));
            labelList.add(troopLabel(p, "ONTARIO", 246, 180));
            buttonList.add(createButton(p, "PERU", 288, 478));
            labelList.add(troopLabel(p, "PERU", 288, 478));
            buttonList.add(createButton(p, "QUEBEC", 320, 178));
            labelList.add(troopLabel(p, "QUEBEC", 320, 178));
            buttonList.add(createButton(p, "SCANDINAVIA", 574, 122));
            labelList.add(troopLabel(p, "SCANDINAVIA", 574, 122));
            buttonList.add(createButton(p, "SIAM", 882, 383));
            labelList.add(troopLabel(p, "SIAM", 882, 383));
            buttonList.add(createButton(p, "SIBERIA", 817, 129));
            labelList.add(troopLabel(p, "SIBERIA", 817, 129));
            buttonList.add(createButton(p, "SOUTH AFRICA", 607, 583));
            labelList.add(troopLabel(p, "SOUTH AFRICA", 607, 583));
            buttonList.add(createButton(p, "<html><center>SOUTHERN<br>EUROPE</center></html>", 560, 284));
            labelList.add(troopLabel(p, "<html><center>SOUTHERN<br>EUROPE</center></html>", 560, 284));
            buttonList.add(createButton(p, "UKRAINE", 653, 173));
            labelList.add(troopLabel(p, "UKRAINE", 653, 173));
            buttonList.add(createButton(p, "URAL", 758, 169));
            labelList.add(troopLabel(p, "URAL", 758, 169));
            buttonList.add(createButton(p, "VENEZUELA", 265, 381));
            labelList.add(troopLabel(p, "VENEZUELA", 265, 381));
            buttonList.add(createButton(p, "<html><center>WESTERN<br>AUSTRALIA</center></html>", 946, 590));
            labelList.add(troopLabel(p, "<html><center>WESTERN<br>AUSTRALIA</center></html>", 946, 590));
            buttonList.add(createButton(p, "<html><center>WESTERN<br>EUROPE</center></html>", 481, 303));
            labelList.add(troopLabel(p, "<html><center>WESTERN<br>EUROPE</center></html>", 481, 303));
            buttonList.add(createButton(p, "<html><center>WESTERN<br>UNITED STATES</center></html>", 183, 232));
            labelList.add(troopLabel(p, "<html><center>WESTERN<br>UNITED STATES</center></html>", 183, 232));
            buttonList.add(createButton(p, "YAKUTSK", 900, 92));
            labelList.add(troopLabel(p, "YAKUTSK", 900, 92));

            for (JButton button : buttonList) {
                p.add(button);
            }
            for (JLabel label : labelList) {
                p.add(label);
            }
            p.add(imgLabel);

            frame.add(p);
            frame.setVisible(true);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Methods for MainGameLoop
    // Get current selected territory
    public int getTerritoryNumber() {
        return territoryNumber;
    }

    // Deselect current territory
    public void deselectTerritory() { territoryNumber = -1; }

    // -----------------------------------------------------------------------------------------------------------------
}