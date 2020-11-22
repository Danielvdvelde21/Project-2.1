package AI.BasicBot;

public class AIMain {

    private final BotPlaceTroops placeTroops;
    private final BotAttacking botAttacking;
    private final BotReinforcement botReinforcement;
    private final BotCards botCards;

    public AIMain() {
        this.placeTroops = new BotPlaceTroops();
        this.botAttacking = new BotAttacking();
        this.botReinforcement = new BotReinforcement();
        this.botCards = new BotCards();
    }

    public BotPlaceTroops getPlaceTroops() {
        return placeTroops;
    }

    public BotAttacking getBotAttacking() {
        return botAttacking;
    }

    public BotReinforcement getBotReinforcement() {
        return botReinforcement;
    }

    public BotCards getBotCards() {
        return botCards;
    }
}
