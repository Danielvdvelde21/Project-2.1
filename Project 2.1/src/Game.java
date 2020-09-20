public class Game {
    private Graph board;
    private int players;

    public Game(int players) {
        // initialize board
        this.players = players;
    }

    public void setUpTwo() {

    }

    public void setUp() {
        switch (this.players) {
            case 2:
                setUpTwo();
                break;
            case 3:
                // 35 pieces
            case 4:
                // 30 pieces
            case 5:
                // 25 pieces
            case 6:
                // 20 pieces
        }

        int[] start = new int[this.players];
        for (int i = 0; i < this.players; i++) {
            start[i] = rollDice();
        }
    }

    public int rollDice(){
        return (int) Math.round(Math.random() * 5.0) + 1;
    }

    public int[] sortDice(int[] dice) {
        if (dice.length == 2) {
            // sort for 2
        }
        if (dice.length == 3) {
            // sort for 3
        }
        return dice;
    }
}
