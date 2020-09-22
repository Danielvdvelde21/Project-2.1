package BackEndStructure;

public class Card {
    // Territory name or WILDCARD
    private String cardName;

    // Infantry, Cavalry, Artillery or WILDCARD
    private String cardType;

    public Card(String name, String type) {
        this.cardName = name;
        this.cardType = type;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardType() {
        return cardName;
    }
}
