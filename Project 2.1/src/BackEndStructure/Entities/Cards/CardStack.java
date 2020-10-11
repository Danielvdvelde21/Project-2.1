package BackEndStructure.Entities.Cards;

import BackEndStructure.Entities.Cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CardStack {
    private final ArrayList<Card> stack = new ArrayList<>();

    public CardStack() {
        stack.add(new Card("WILDCARD", "WILDCARD",0));
        stack.add(new Card("WILDCARD", "WILDCARD",1));

        stack.add(new Card("ALASKA", "Infantry",2));
        stack.add(new Card("AFGHANISTAN", "Cavalry",3));
        stack.add(new Card("ALBERTA", "Cavalry",4));
        stack.add(new Card("ARGENTINA", "Infantry",5));
        stack.add(new Card("BRAZIL", "Artillery",6));
        stack.add(new Card("CONGO", "Infantry",7));
        stack.add(new Card("CENTRAL AMERICA", "Artillery",8));
        stack.add(new Card("CHINA", "Infantry",9));
        stack.add(new Card("EAST AFRICA", "Infantry",10));
        stack.add(new Card("EASTERN AUSTRALIA", "Artillery",11));
        stack.add(new Card("EASTERN CANADA", "Cavalry",12));
        stack.add(new Card("EASTERN UNITED STATES", "Artillery",13));
        stack.add(new Card("EGYPT", "Infantry",14));
        stack.add(new Card("GREAT BRITAIN", "Artillery",15));
        stack.add(new Card("GREENLAND", "Cavalry",16));
        stack.add(new Card("ICELAND", "Infantry",17));
        stack.add(new Card("INDIA", "Cavalry",18));
        stack.add(new Card("INDONESIA", "Artillery",19));
        stack.add(new Card("IRKUTSK", "Cavalry",20));
        stack.add(new Card("JAPAN", "Artillery",21));
        stack.add(new Card("KAMCHATKA", "Infantry",22));
        stack.add(new Card("MADAGASCAR", "Cavalry",23));
        stack.add(new Card("MIDDLE EAST", "Infantry",24));
        stack.add(new Card("MONGOLIA", "Infantry",25));
        stack.add(new Card("NEW GUINEA", "Infantry",26));
        stack.add(new Card("NORTH AFRICA", "Cavalry",27));
        stack.add(new Card("NORTHERN EUROPE", "Artillery",28));
        stack.add(new Card("NORTHWEST TERRITORY", "Artillery",29));
        stack.add(new Card("ONTARIO", "Cavalry",30));
        stack.add(new Card("PERU", "Infantry",31));
        stack.add(new Card("RUSSIA", "Cavalry",32));
        stack.add(new Card("SCANDINAVIA", "Cavalry",33));
        stack.add(new Card("SIBERIA", "Infantry",34));
        stack.add(new Card("SOUTH AFRICA", "Artillery",35));
        stack.add(new Card("SOUTHEAST ASIA", "Infantry",36));
        stack.add(new Card("SOUTHERN EUROPE", "Artillery",37));
        stack.add(new Card("URAL", "Cavalry",38));
        stack.add(new Card("VENEZUELA", "Infantry",39));
        stack.add(new Card("WESTERN AUSTRALIA", "Artillery",40));
        stack.add(new Card("WESTERN EUROPE", "Artillery",41));
        stack.add(new Card("WESTERN UNITED STATES", "Artillery",42));
        stack.add(new Card("YAKUTSK", "Cavalry",43));
    }

    public void shuffle() {
        Collections.shuffle(stack);
    }

    public Card draw() {
        Card tempCard = stack.get(stack.size()-1);
        stack.remove(stack.size()-1);
        return tempCard;
    }

    public void returnCards(Card[] usedCards) {
        stack.addAll(Arrays.asList(usedCards));
        shuffle();
    }

    public boolean isSet(Card[] cards) {
        int countInfantry = 0;
        int countCavalry = 0;
        int countArtillery = 0;

        for(Card c : cards) {
            if(c.getCardType().equals("WILDCARD")) {
                return true;
            }
            if(c.getCardType().equals("Infantry")) {
                countInfantry++;
                continue;
            }
            if(c.getCardType().equals("Cavalry")) {
                countCavalry++;
                continue;
            }
            if(c.getCardType().equals("Artillery")) {
                countArtillery++;
            }
        }
        return countInfantry >= 3 || countCavalry >= 3 || countArtillery >= 3 || (countInfantry > 0 && countCavalry > 0 && countArtillery > 0);
    }


}
