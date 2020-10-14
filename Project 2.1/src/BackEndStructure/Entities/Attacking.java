package BackEndStructure.Entities;

public class Attacking {

    public static void main(String [] args) {
        oneFight(3, new int[]{1,5,6}, 2, new int[]{3,4});
        System.out.println("new experiment");
        oneFight(2, new int[]{1,5,6}, 1, new int[]{3,4});
    }

    // Simulates 1 fight with 1-3 attackers and 1-2 defenders
    // AttackValues is always size 3 and DefendValues always size 2
    public static void oneFight(int attackers, int[] attackValues, int defenders, int[] defendValues){
        int max = 0;
        int ndMax = 0;
        int defMax = 0;
        int defNdMax = 0;

        for (int i = 0; i < 3; i++) {
            if (attackValues[i] > max) {
                max = attackValues[i];
            } else if (attackValues[i] > ndMax) {
                ndMax = attackValues[i];
            }
            if (i < 2) {
                if (defendValues[i] > defMax) {
                    defMax = defendValues[i];
                } else if (defendValues[i] > defNdMax) {
                    defNdMax = defendValues[i];
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

