package BackEndStructure.Entities;

public class AttackingHandler {

    private int lostTroopsDefenders;
    private int lostTroopsAttackers;

    // Simulates 1 fight with 1-3 attackers and 1-2 defenders
    // AttackValues is always size 3 and DefendValues always size 2
    public void oneFight(int attackers, int[] attackValues, int defenders, int[] defendValues) {
        switch(attackers) {
            case 3:
                break;
            case 2:
                attackValues[1] = 0;
                break;
            case 1:
                attackValues[1] = 0;
                attackValues[2] = 0;
        }
        switch(defenders) {
            case 2:
                break;
            case 1:
                defendValues[1] = 0;
        }

        int max = 0;
        int ndMax = 0;
        int defMax = 0;
        int defNdMax = 0;
        int bestADice = 0;
        int bestDDice = 0;

        for (int i = 0; i < 3; i++) {
            if (attackValues[i] > max) {
                max = attackValues[i];
                bestADice = i;
            }
            if (i < 2) {
                if (defendValues[i] > defMax) {
                    defMax = defendValues[i];
                    bestDDice = i;
                }
            }
        }

        attackValues[bestADice] = 0;
        defendValues[bestDDice] = 0;

        for (int j = 0; j < 3; j++) {
            if (attackValues[j] > ndMax) {
                ndMax = attackValues[j];
            }
            if (j < 2) {
                if (defendValues[j] > defNdMax) {
                    defNdMax = defendValues[j];
                }
            }
        }
        // System.out.println(max + "    " + ndMax + "     " + defMax + "     " + defNdMax);

        if (attackers == 1 || defenders == 1) {
            if (max > defMax) {
                lostTroopsDefenders = 1;
            } else {
                lostTroopsAttackers = 1;
            }
        } else {
            if (max > defMax && ndMax > defNdMax) {
                lostTroopsDefenders = 2;
            } else if (defMax >= max && defNdMax >= ndMax) {
                lostTroopsAttackers = 2;
            } else {
                lostTroopsAttackers = 1;
                lostTroopsDefenders = 1;
            }
        }

    }

    public int getLostTroopsDefenders() {
        return lostTroopsDefenders;
    }

    public int getLostTroopsAttackers() {
        return lostTroopsAttackers;
    }

    public void resetTroopsLost() {
        lostTroopsAttackers = 0;
        lostTroopsDefenders = 0;
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


