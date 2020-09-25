package Visual;

import BackEndStructure.Graph.Territory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {

    private String selectedTerritory = "";

    private static Insets insets;
    private static Dimension size;
    private static BufferedImage wPic;
    private static int frameX = 1600;
    private static int frameY = 900;
    // public static int frameX=1100;
    // public static int frameY=700;


    private JButton createButton(JPanel p, String name, int posX, int posY) {
        JButton b = new JButton(name);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        // b.repaint();
        insets = p.getInsets();
        size = b.getPreferredSize();
        b.setBounds(((posX * frameX) / 1100) - size.width / 2, ((posY * frameY) / 700) - size.height / 2, size.width, size.height);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked on "+((JButton)e.getSource()).getText().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
                selectedTerritory = ((JButton)e.getSource()).getText().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
                // TODO Need a method that when a button is pressed it can point to a territory
            }
        });
        return b;
    }

    private JLabel createLabel(JPanel p, String number, int xPos, int yPos){
        JLabel l = new JLabel(number);
        l.setText("0");
        l.setLocation(xPos, yPos);
        return l;
    }

    public void createMap() {
        JFrame frame = new JFrame("RISK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameX, frameY);
        JPanel p = new JPanel();

        p.setLayout(null);
        p.setBounds(new Rectangle(0, 0, 200, 200));

        ImageIcon imageIcon = new ImageIcon("src\\resources\\risk-board.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(frameX, frameY, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);

        JLabel imgLabel = new JLabel(imageIcon);
        imgLabel.setBounds(0, 0, frameX, frameY);

        ArrayList<JButton> buttonList = new ArrayList<JButton>();

        buttonList.add(createButton(p, "ALASKA", 87, 113));
        buttonList.add(createButton(p, "NORTH WEST TERRITORY", 190, 109));
        buttonList.add(createButton(p, "ALBERTA", 177, 165));
        buttonList.add(createButton(p, "ONTARIO", 246, 180));
        buttonList.add(createButton(p, "QUEBEC", 320, 178));
        buttonList.add(createButton(p, "GREENLAND", 385, 73));
        buttonList.add(createButton(p, "<html><center>WESTERN<br>UNITED STATES</center></html>", 183, 232));
        buttonList.add(createButton(p, "<html><center>EASTERN<br>UNITED STATES</center></html>", 247, 262));
        buttonList.add(createButton(p, "CENTRAL AMERICA", 192, 327));
        buttonList.add(createButton(p, "VENEZUELA", 265, 381));
        buttonList.add(createButton(p, "BRAZIL", 341, 443));
        buttonList.add(createButton(p, "ARGENTINA", 293, 539));
        buttonList.add(createButton(p, "PERU", 288, 478));

        buttonList.add(createButton(p, "ICELAND", 474, 143));
        buttonList.add(createButton(p, "GREAT BRITAIN", 457, 216));
        buttonList.add(createButton(p, "SCANDINAVIA", 574, 122));
        buttonList.add(createButton(p, "<html><center>NORTHERN<br>EUROPE</center></html>", 555, 232));
        buttonList.add(createButton(p, "<html><center>SOUTHERN<br>EUROPE</center></html>", 560, 284));
        buttonList.add(createButton(p, "<html><center>WESTERN<br>EUROPE</center></html>", 481, 303));
        buttonList.add(createButton(p, "UKRAINE", 653, 173));

        buttonList.add(createButton(p, "NORTH AFRICA", 527, 428));
        buttonList.add(createButton(p, "EGYPT", 596, 392));
        buttonList.add(createButton(p, "EAST AFRICA", 656, 472));
        buttonList.add(createButton(p, "CONGO", 594, 503));
        buttonList.add(createButton(p, "SOUTH AFRICA", 607, 583));
        buttonList.add(createButton(p, "MADAGASCAR", 697, 595));

        buttonList.add(createButton(p, "URAL", 758, 169));
        buttonList.add(createButton(p, "SIBERIA", 817, 129));
        buttonList.add(createButton(p, "YAKUTSK", 900, 92));
        buttonList.add(createButton(p, "IRKUTSK", 881, 183));
        buttonList.add(createButton(p, "KAMCHATKA", 984, 95));
        buttonList.add(createButton(p, "MONGOLIA", 891, 238));
        buttonList.add(createButton(p, "CHINA", 873, 302));
        buttonList.add(createButton(p, "AFGHANISTAN", 745, 252));
        buttonList.add(createButton(p, "MIDDLE EAST", 695, 337));
        buttonList.add(createButton(p, "INDIA", 805, 351));
        buttonList.add(createButton(p, "SIAM", 882, 383));
        buttonList.add(createButton(p, "JAPAN", 1002, 249));

        buttonList.add(createButton(p, "INDONESIA", 901, 489));
        buttonList.add(createButton(p, "<html><center>NEW<br>GUINEA</center></html>", 999, 468));
        buttonList.add(createButton(p, "<html><center>WESTERN<br>AUSTRALIA</center></html>", 946, 590));
        buttonList.add(createButton(p, "<html><center>EASTERN<br>AUSTRALIA</center></html>", 1014, 557));

        for (JButton button : buttonList) {
            p.add(button);
        }
        p.add(imgLabel);
        frame.add(p);

        frame.setVisible(true);
    }

    public String getSelectedTerritory() {
        return selectedTerritory;
    }

    public void resetSelectedTerritory() {
        selectedTerritory = "";
    }
}
