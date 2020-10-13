package BackEndStructure.Entities.Cards;

import BackEndStructure.Entities.Player;

import java.util.ArrayList;

public class SetHandler {

    // Sets are always pairs of 3 cards
    public boolean isSet(Player player, ArrayList<Card> cards) {
        // TODO IF YOU HAVE A CARD WITH A TERRITORY ON IT THAT YOU OWN RECEIVE +2 TROOPS ON THAT TERRITORY

        int countInfantry = 0;
        int countCavalry = 0;
        int countArtillery = 0;

        // cards is length 3 always
        for (Card c : cards) {
            // Any 2 cards plus a wildcard is a set
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
        return (countInfantry == 3 || countCavalry == 3 || countArtillery == 3 || (countInfantry == 1 && countCavalry == 1 && countArtillery == 1));
    }
}
