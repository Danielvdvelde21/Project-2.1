package AI.BasicBot.Components;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;

import java.util.ArrayList;
import java.util.Arrays;

public class BotCards extends UsefulMethods {

    // -----------------------------------------------------------------------------------------------------------------
    // Trading cards

    /**
     * Based on the bots hand (p.getHand()) and the current board
     * Let the bot make a decision on if and how it wants to return its cards
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return a list of cards that the bot wants to turn in
     */
    public ArrayList<Card> cards(Graph g, Player p) {
        // No sets available if the bot has less than 3 cards
        if (p.getHand().size() < 3) {
            return null;
        }

        // Get all territories owned
        ArrayList<Territory> territoriesOwned = getOwnedTerritories(g, p);
        int h = p.getHand().size();

        // Evaluate what territories owned match with the cards
        ArrayList<Card> preferredCards = new ArrayList<>();
        for (Card c : p.getHand()) {
            for (Territory t : territoriesOwned) {
                if (c.getCardName().equals(t.getTerritoryName())) {
                    preferredCards.add(c);
                }
            }
        }

        // Create each possible set of cards
        ArrayList<Card> bestSet = new ArrayList<>();
        int maxPrefSetsUsed = 0;

        for (int i = 1; i < h; i++) {
            for (int j = i + 1; j < h; j++) {
                for (int k = j + 1; k < h; k++) {
                    ArrayList<Card> tempSet = new ArrayList<>(Arrays.asList(p.getHand().get(i), p.getHand().get(j), p.getHand().get(k)));
                    // Check if the tempSet is a valid set
                    if (isSet(tempSet)) {
                        // Check how many preferredCards the set is using
                        int prefCardsUsed = -1;
                        for (Card c : tempSet) {
                            for (Card prefCard : preferredCards) {
                                if (c == prefCard) {
                                    prefCardsUsed++;
                                }
                            }
                        }
                        if (prefCardsUsed > maxPrefSetsUsed) {
                            bestSet = tempSet;
                            maxPrefSetsUsed = prefCardsUsed;
                        }
                    }
                }
            }
        }
        return bestSet;
    }

    // Sets are always pairs of 3 cards
    private boolean isSet(ArrayList<Card> cards) {
        int countInfantry = 0;
        int countCavalry = 0;
        int countArtillery = 0;

        for (Card c : cards) {
            String cardType = c.getCardType();
            switch (cardType) {
                case "WILDCARD":
                    return true;
                case "Infantry":
                    countInfantry++;
                    break;
                case "Cavalry":
                    countCavalry++;
                    break;
                case "Artillery":
                    countArtillery++;
            }
        }
        return countInfantry == 3 || countCavalry == 3 || countArtillery == 3 || (countInfantry == 1 && countCavalry == 1 && countArtillery == 1);
    }

}
