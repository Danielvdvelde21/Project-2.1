package BackEndStructure.Entities.Cards;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;

import java.util.ArrayList;

public class SetHandler {

    // Sets are always pairs of 3 cards
    public boolean isSet(Game game, Player player, ArrayList<Card> cards) {
        // IF YOU HAVE A CARD WITH A TERRITORY ON IT THAT YOU OWN RECEIVE +2 TROOPS ON THAT TERRITORY
        checkMatchingTerritories(game, player, cards);

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

    private void checkMatchingTerritories(Game game, Player player, ArrayList<Card> cards) {
        String territoriesOnCards = "";
        for (int i = 0; i < 42; i++) {
            for (int j = 0; j < 3; j++) {
                if (game.getGraph().get(i).getTerritory().getOwner().equals(player.getName()) && game.getGraph().get(i).getTerritory().getTerritoryName().equals(cards.get(j).getCardName())) {
                    game.getGraph().get(i).getTerritory().setNumberOfTroops(game.getGraph().get(i).getTerritory().getNumberOfTroops() + 2);
                    game.getMap().updateTroopCount(i, game.getGraph().get(i).getTerritory().getNumberOfTroops());
                    territoriesOnCards += game.getGraph().get(i).getTerritory().getTerritoryName() + ", ";
                }
            }
        }
        if (!territoriesOnCards.equals("")) {
            territoriesOnCards = territoriesOnCards.substring(0, territoriesOnCards.length() - 2);
            game.getNarrator().addText("Since player " + player.getName() + " owned territories: " + territoriesOnCards + " , he will get 2 additional troops on these territories!");
        }
    }
}
