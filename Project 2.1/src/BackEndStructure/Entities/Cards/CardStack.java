package BackEndStructure.Entities.Cards;

import BackEndStructure.Entities.Cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CardStack {
    private final ArrayList<Card> stack = new ArrayList<>();

    public CardStack() {
        stack.add(new Card("WILDCARD", "WILDCARD"));
        stack.add(new Card("WILDCARD", "WILDCARD"));

        stack.add(new Card("ALASKA", "Infantry"));
        stack.add(new Card("AFGHANISTAN", "Cavalry"));
        stack.add(new Card("ALBERTA", "Cavalry"));
        stack.add(new Card("ARGENTINA", "Infantry"));
        stack.add(new Card("BRAZIL", "Artillery"));
        stack.add(new Card("CONGO", "Infantry"));
        stack.add(new Card("CENTRAL AMERICA", "Artillery"));
        stack.add(new Card("CHINA", "Infantry"));
        stack.add(new Card("EAST AFRICA", "Infantry"));
        stack.add(new Card("EASTERN AUSTRALIA", "Artillery"));
        stack.add(new Card("EASTERN CANADA", "Cavalry"));
        stack.add(new Card("EASTERN UNITED STATES", "Artillery"));
        stack.add(new Card("EGYPT", "Infantry"));
        stack.add(new Card("GREAT BRITAIN", "Artillery"));
        stack.add(new Card("GREENLAND", "Cavalry"));
        stack.add(new Card("ICELAND", "Infantry"));
        stack.add(new Card("INDIA", "Cavalry"));
        stack.add(new Card("INDONESIA", "Artillery"));
        stack.add(new Card("IRKUTSK", "Cavalry"));
        stack.add(new Card("JAPAN", "Artillery"));
        stack.add(new Card("KAMCHATKA", "Infantry"));
        stack.add(new Card("MADAGASCAR", "Cavalry"));
        stack.add(new Card("MIDDLE EAST", "Infantry"));
        stack.add(new Card("MONGOLIA", "Infantry"));
        stack.add(new Card("NEW GUINEA", "Infantry"));
        stack.add(new Card("NORTH AFRICA", "Cavalry"));
        stack.add(new Card("NORTHERN EUROPE", "Artillery"));
        stack.add(new Card("NORTHWEST TERRITORY", "Artillery"));
        stack.add(new Card("ONTARIO", "Cavalry"));
        stack.add(new Card("PERU", "Infantry"));
        stack.add(new Card("RUSSIA", "Cavalry"));
        stack.add(new Card("SCANDINAVIA", "Cavalry"));
        stack.add(new Card("SIBERIA", "Infantry"));
        stack.add(new Card("SOUTH AFRICA", "Artillery"));
        stack.add(new Card("SOUTHEAST ASIA", "Infantry"));
        stack.add(new Card("SOUTHERN EUROPE", "Artillery"));
        stack.add(new Card("URAL", "Cavalry"));
        stack.add(new Card("VENEZUELA", "Infantry"));
        stack.add(new Card("WESTERN AUSTRALIA", "Artillery"));
        stack.add(new Card("WESTERN EUROPE", "Artillery"));
        stack.add(new Card("WESTERN UNITED STATES", "Artillery"));
        stack.add(new Card("YAKUTSK", "Cavalry"));
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
