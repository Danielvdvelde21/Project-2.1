package BackEndStructure.Entities.Cards;

public class Card {
    // Territory name or WILDCARD
    private String cardName;

    // Infantry, Cavalry, Artillery or WILDCARD
    private String cardType;

    private int cardImageId;

    public Card(String name, String type, int cardImageId) {
        this.cardName = name;
        this.cardType = type;
        this.cardImageId = cardImageId;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public int getCardImageId() {
        return cardImageId;
    }
}
