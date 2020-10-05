package BackEndStructure.Entities;

import java.util.Arrays;

public class Dice {

    // Test
    public static void main(String[] args) {
        int[] players = new int[]{55,66,77,88,99};
        getPlayOrder2(players);
    }

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

    public static void oneFight(int attackers, int defenders){
        int[] attackerDice = new int[3];
        int[] defendersDice = new int[2];
        int max = 0;
        int ndMax = 0;
        int defMax = 0;
        int defNdMax = 0;
        for (int i = 0; i < attackers; i++) {
            attackerDice[i] = (int) (Math.random() * 6 + 1);
        }
        for (int i = 0; i < defenders; i++) {
            defendersDice[i] = (int) (Math.random() * 6 + 1);
        }

        for (int i = 0; i < 3; i++) {
            if (attackerDice[i] > max) {
                max = attackerDice[i];
            } else if (attackerDice[i] > ndMax) {
                ndMax = attackerDice[i];
            }
            if (i < 2) {
                if (defendersDice[i] > defMax) {
                    defMax = defendersDice[i];
                } else if (defendersDice[i] > defNdMax) {
                    defNdMax = defendersDice[i];
                }
            }
        }

        if (attackers == 1 || defenders == 2) {
            if (max > defMax) {
                System.out.println("Defenders lose 1 troop");
            } else {
                System.out.println("Attackers lose 1 troop");
            }
        }
        else {
            if (max > defMax && ndMax > defNdMax) {
                System.out.println("Defenders lose 2 troops!");
            } else if (defMax >= max && defNdMax >= ndMax) {
                System.out.println("Attackers lose 2 troops!");
            } else {
                System.out.println("Attackers and Defenders both lose 1 troop");
            }
        }

    }
    // Rolling for combat
    public static void combat(int attackers, int defenders){
        int attackerDeaths = 0;
        int defenderDeaths = 0;
        while(attackers-1  > attackerDeaths && defenders > defenderDeaths) {
            int[] attackerDice = new int[3];
            int[] defendersDice = new int[2];

            //throw all the dice
            int attackersLeft = attackers - attackerDeaths;
            int defendersLeft = defenders - defenderDeaths;
            // Attacker dice
            if(attackersLeft > 2) {
                for (int i = 0; i < 3; i++) {
                    attackerDice[i] = (int) (Math.random() * 6 + 1);
                }
            }
            else{
                for (int i = 0; i < attackersLeft; i++) {
                    attackerDice[i] = (int) (Math.random() * 6 + 1);
                }
            }
            // Defender dice
            if(defendersLeft > 1) {
                defendersDice[0] = (int) (Math.random() * 6 + 1);
                defendersDice[1] = (int) (Math.random() * 6 + 1);
            }
            else{
                defendersDice[0] = (int) (Math.random() * 6 + 1);
            }

            // listing all the dice we need to save
            int max = 0;
            int ndMax = 0;
            int defMax = 0;
            int defNdMax = 0;
            // saving all the dice that are the highest
            for (int i = 0; i < 3; i++) {
                if (attackerDice[i] > max) {
                    max = attackerDice[i];
                } else if (attackerDice[i] > ndMax) {
                    ndMax = attackerDice[i];
                }
                if (i < 2) {
                    if (defendersDice[i] > defMax) {
                        defMax = defendersDice[i];
                    } else if (defendersDice[i] > defNdMax) {
                        defNdMax = defendersDice[i];
                    }
                }
            }

            // comparing both highest to eachother and updating each time.
            //System.out.println("attackersleft " + attackersLeft);
            //System.out.println("defendersleft " + defendersLeft);

            if (defendersLeft == 1 || attackersLeft == 2) {
                if (max > defMax) {
                    System.out.println("Defenders lose 1 troop");
                    defenderDeaths++;
                } else {
                    System.out.println("Attackers lose 1 troop");
                    attackerDeaths++;
                }
            } else {
                if (max > defMax && ndMax > defNdMax) {
                    System.out.println("Defenders lose 2 troops!");
                    defenderDeaths += 2;
                } else if (defMax >= max && defNdMax >= ndMax) {
                    System.out.println("Attackers lose 2 troops!");
                    attackerDeaths += 2;
                } else {
                    System.out.println("Attackers and Defenders both lose 1 troop");
                    attackerDeaths++;
                    defenderDeaths++;
                }
            }
        }  // while loop ends here
        int attackersAtEnd = attackers - attackerDeaths;
        int defendersAtEnd = defenders - defenderDeaths;
        System.out.println("The attacker has " + attackersAtEnd + " troops left!");
        System.out.println("The defender has " + defendersAtEnd + " troops left!");
    }

    // Determines who can start placing infantry
    public static Player[] getPlayOrder(Player[] players) {
        // get the order in int
        int[] playOrderInts = new int[players.length];
        // get the output ready
        int[] list = new int[]{1,2,3,4,5,6};
        Player[] playOrder = new Player[players.length];


        for(int i = 0; i < players.length; i++){
            int winner = (int) (Math.random() * players.length);
            while(list[winner] == 0){
                winner = (int) (Math.random() * players.length);
            }
            list[winner] = 0;
            playOrder[i] = players[winner];
        }
        return playOrder;
    }

    // tester method of getPlayOrder
    public static int[] getPlayOrder2(int[] players) {
        // get the order in int
        int[] playOrderInts = new int[players.length];
        // get the output ready
        int[] list = new int[]{1,2,3,4,5,6};
        int[] playOrder = new int[players.length];


        for(int i = 0; i < players.length; i++){
            int winner = (int) (Math.random() * players.length);
            while(list[winner] == 0){
                winner = (int) (Math.random() * players.length);
            }
            list[winner] = 0;
            System.out.println(players[winner]);
            playOrder[i] = players[winner];
        }
        return playOrder;
    }



    public int getEyes() {
        return eyes;
    }
}
