package BackEndStructure;

public class Dice {
    // Number of eyes rolled
    private int eyes;

    // Sets the eyes for a dice
    public void rollDice() {
        this.eyes = (int)(Math.random()*6+1);
    }

    // Roll multiple die
    public void rollDice(Dice[] redDice, Dice[] whiteDice) {
        for (Dice dice : redDice) {
            dice.rollDice();
        }
        for (Dice dice : whiteDice) {
            dice.rollDice();
        }
    }

    // Determines who can start placing infantry
    public Player whoGoesFirst(Player[] players) {
        int[] playerRolls = new int[players.length];
        Dice temp =  new Dice();

        for (int i = 0; i < players.length; i++) {
            temp.rollDice();
            playerRolls[i] = temp.getEyes();
        }

        int bestRoll = 0;
        for (int playerRoll : playerRolls) {
            if (bestRoll < playerRoll) {
                bestRoll = playerRoll;
            }
        }

        for (int playerRoll : playerRolls) {
            if (bestRoll == playerRoll) {

            }
        }

        return players[bestRoll];
    }

    public int getEyes() {
        return eyes;
    }
}
