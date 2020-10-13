package BackEndStructure.Entities;

import java.util.ArrayList;

public class Dice {

    // TODO MAYBE SPLIT CLASS UP INTO ATTACKING AND GETTING PLAYERORDER
    // Simulates 1 fight with 1-3 attackers and 1-2 defenders
    public void oneFight(int attackers, int defenders){
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

    // Determines who can start placing infantry
    public ArrayList<Player> getPlayOrder(ArrayList<Player> players) {
        // get the order in int
        int[] playOrderInts = new int[players.size()];
        // get the output ready
        int[] list = new int[]{1,2,3,4,5,6};
        ArrayList<Player> playOrder = new ArrayList<>();

        for(int i = 0; i < players.size(); i++){
            int winner = (int) (Math.random() * players.size());
            while(list[winner] == 0){
                winner = (int) (Math.random() * players.size());
            }
            list[winner] = 0;
            playOrder.add(players.get(winner));
        }
        return playOrder;
    }

//    // Rolling for combat
//    public void combat(int attackers, int defenders){
//        int attackerDeaths = 0;
//        int defenderDeaths = 0;
//        while(attackers-1  > attackerDeaths && defenders > defenderDeaths) {
//            int[] attackerDice = new int[3];
//            int[] defendersDice = new int[2];
//
//            //throw all the dice
//            int attackersLeft = attackers - attackerDeaths;
//            int defendersLeft = defenders - defenderDeaths;
//            // Attacker dice
//            if(attackersLeft > 2) {
//                for (int i = 0; i < 3; i++) {
//                    attackerDice[i] = (int) (Math.random() * 6 + 1);
//                }
//            }
//            else{
//                for (int i = 0; i < attackersLeft; i++) {
//                    attackerDice[i] = (int) (Math.random() * 6 + 1);
//                }
//            }
//            // Defender dice
//            if(defendersLeft > 1) {
//                defendersDice[0] = (int) (Math.random() * 6 + 1);
//                defendersDice[1] = (int) (Math.random() * 6 + 1);
//            }
//            else{
//                defendersDice[0] = (int) (Math.random() * 6 + 1);
//            }
//
//            // listing all the dice we need to save
//            int max = 0;
//            int ndMax = 0;
//            int defMax = 0;
//            int defNdMax = 0;
//            // saving all the dice that are the highest
//            for (int i = 0; i < 3; i++) {
//                if (attackerDice[i] > max) {
//                    max = attackerDice[i];
//                } else if (attackerDice[i] > ndMax) {
//                    ndMax = attackerDice[i];
//                }
//                if (i < 2) {
//                    if (defendersDice[i] > defMax) {
//                        defMax = defendersDice[i];
//                    } else if (defendersDice[i] > defNdMax) {
//                        defNdMax = defendersDice[i];
//                    }
//                }
//            }
//
//            // comparing both highest to eachother and updating each time.
//            //System.out.println("attackersleft " + attackersLeft);
//            //System.out.println("defendersleft " + defendersLeft);
//
//            if (defendersLeft == 1 || attackersLeft == 2) {
//                if (max > defMax) {
//                    System.out.println("Defenders lose 1 troop");
//                    defenderDeaths++;
//                } else {
//                    System.out.println("Attackers lose 1 troop");
//                    attackerDeaths++;
//                }
//            } else {
//                if (max > defMax && ndMax > defNdMax) {
//                    System.out.println("Defenders lose 2 troops!");
//                    defenderDeaths += 2;
//                } else if (defMax >= max && defNdMax >= ndMax) {
//                    System.out.println("Attackers lose 2 troops!");
//                    attackerDeaths += 2;
//                } else {
//                    System.out.println("Attackers and Defenders both lose 1 troop");
//                    attackerDeaths++;
//                    defenderDeaths++;
//                }
//            }
//        }  // while loop ends here
//        int attackersAtEnd = attackers - attackerDeaths;
//        int defendersAtEnd = defenders - defenderDeaths;
//        System.out.println("The attacker has " + attackersAtEnd + " troops left!");
//        System.out.println("The defender has " + defendersAtEnd + " troops left!");
//    }


    // tester method of getPlayOrder
//    public int[] getPlayOrder2(int[] players) {
//        // get the order in int
//        int[] playOrderInts = new int[players.length];
//        // get the output ready
//        int[] list = new int[]{1,2,3,4,5,6};
//        int[] playOrder = new int[players.length];
//
//        for(int i = 0; i < players.length; i++){
//            int winner = (int) (Math.random() * players.length);
//            while(list[winner] == 0){
//                winner = (int) (Math.random() * players.length);
//            }
//            list[winner] = 0;
//            System.out.println(players[winner]);
//            playOrder[i] = players[winner];
//        }
//        return playOrder;
//    }
}
