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

        stack.add(new Card("Alaska", "Infantry",2));
        stack.add(new Card("Afghanistan", "Cavalry",3));
        stack.add(new Card("Alberta", "Cavalry",4));
        stack.add(new Card("Argentina", "Infantry",5));
        stack.add(new Card("Brazil", "Artillery",6));
        stack.add(new Card("Central America", "Infantry",7));
        stack.add(new Card("Central America", "Artillery",8));
        stack.add(new Card("China", "Infantry",9));
        stack.add(new Card("East Africa", "Infantry",10));
        stack.add(new Card("Eastern Australia", "Artillery",11));
        stack.add(new Card("Eastern Canada", "Cavalry",12));
        stack.add(new Card("Eastern United States", "Artillery",13));
        stack.add(new Card("Egypt", "Infantry",14));
        stack.add(new Card("Great Britain", "Artillery",15));
        stack.add(new Card("Greenland", "Cavalry",16));
        stack.add(new Card("Iceland", "Infantry",17));
        stack.add(new Card("India", "Cavalry",18));
        stack.add(new Card("Indonesia", "Artillery",19));
        stack.add(new Card("Irkutsk", "Cavalry",20));
        stack.add(new Card("Japan", "Artillery",21));
        stack.add(new Card("Kamchatka", "Infantry",22));
        stack.add(new Card("Madagascar", "Cavalry",23));
        stack.add(new Card("Middle East", "Infantry",24));
        stack.add(new Card("Mongolia", "Infantry",25));
        stack.add(new Card("New Guinea", "Infantry",26));
        stack.add(new Card("North Africa", "Cavalry",27));
        stack.add(new Card("Northern Europe", "Artillery",28));
        stack.add(new Card("Northwest Territory", "Artillery",29));
        stack.add(new Card("Ontario", "Cavalry",30));
        stack.add(new Card("Peru", "Infantry",31));
        stack.add(new Card("Russia", "Cavalry",32));
        stack.add(new Card("Scandinavia", "Cavalry",33));
        stack.add(new Card("Siberia", "Infantry",34));
        stack.add(new Card("South Africa", "Artillery",35));
        stack.add(new Card("Southeast Asia", "Infantry",36));
        stack.add(new Card("Southern Europe", "Artillery",37));
        stack.add(new Card("Ural", "Cavalry",38));
        stack.add(new Card("Venezuela", "Infantry",39));
        stack.add(new Card("Western Australia", "Artillery",40));
        stack.add(new Card("Western Europe", "Artillery",41));
        stack.add(new Card("Western United States", "Artillery",42));
        stack.add(new Card("Yakutsk", "Cavalry",43));
    }

    public void shuffle() {
        Collections.shuffle(stack);
    }

    public Card draw() {
        Card tempCard = stack.get(stack.size()-1);
        stack.remove(stack.size()-1);
        return tempCard;
    }

    public void returnCards(ArrayList<Card> usedCards) {
        stack.addAll(usedCards);
        shuffle();
    }

}
