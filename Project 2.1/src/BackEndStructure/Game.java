package BackEndStructure;

public class Game {
    private Player[] players;

    // How manny troops each player gets from the start
    private int startingTroops;

    // Attacking Die
    private Dice red1;
    private Dice red2;
    private Dice red3;

    // Defending Die
    private Dice white1;
    private Dice white2;

    // Cards
    private CardStack cardStack;

    // TODO take as imput each player name
    public Game(int numberOfPlayers, String[] names) {
        switch (numberOfPlayers) {
            case 2:
                int startingTroops = 40;
                break;
            case 3:
                startingTroops = 35;
                break;
            case 4:
                startingTroops = 30;
                break;
            case 5:
                startingTroops = 25;
                break;
            case 6:
                startingTroops = 20;
                break;
            default:
                System.out.println("Insert 2,3,4,5 or 6");
                break;
        }
        this.players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            this.players[i] = new Player(startingTroops, names[i]);
        }

        this.cardStack = new CardStack();

    }

}
