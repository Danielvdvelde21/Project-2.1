package BackEndStructure;

import java.util.Arrays;

public class Dice {

    // Test
    public static void main(String[] args) {
        int[] players = new int[6];
        whoGoesFirst(players);
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
    // TODO roll a dice for each player, the one that throw the most eyes starts the game
    // TODO if two people throw 6, throw again?
    public static int[] whoGoesFirst(int[] players) {

        // first we get a simple list of players
        int[] playerss = new int[players.length];
        // give everyone a number
        for (int i = 0; i < playerss.length; i++) {
            playerss[i] = (int) (Math.random() * 6 + 1);
        }
        // decide who got the best roll
        int max = 0;
        int[] playersWhoThrewMax = new int[players.length];
        int goodRolls = 0;
        int[] playOrder = new int[players.length];
        int highRolls = 0;
        for (int i = 0; i < players.length; i++) {

            for (int j = 0; j < players.length; j++) {
                if (playerss[j] > max) {
                    max = playerss[j];
                    goodRolls = 0;
                    playersWhoThrewMax = new int[players.length];
                    playersWhoThrewMax[goodRolls] = j+1;
                } else if (playerss[j] == max) {
                    goodRolls++;
                    playersWhoThrewMax[goodRolls] = j+1;
                }
            }

            for (int k = 0; k < playersWhoThrewMax.length; k++) {
                if (playersWhoThrewMax[k] >= 1) {
                    highRolls++;
                    //System.out.println("this is high enough = " + playersWhoThrewMax[k] + "this is highrolls now = " + highRolls);
                }
                //System.out.println("amount that threw the same " + highRolls);
                for (int l = 0; l < highRolls; l++) {
                    System.out.println("player who threw max " + playersWhoThrewMax[k]);
                    playOrder[i] = playersWhoThrewMax[k] - 1;
                    i++;
                    playerss[playersWhoThrewMax[k]-1] = 0;
                    //System.out.println(playOrder[i]);
                    //System.out.println("here we skip one, current highrolls = " + highRolls);
                }
                highRolls =0;
            }


        }
        for(int i = 0; i < players.length; i++){
            System.out.println(playOrder[i]);
        }
        return playOrder;
    }
        /*
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
        */



    public int getEyes() {
        return eyes;
    }
}
