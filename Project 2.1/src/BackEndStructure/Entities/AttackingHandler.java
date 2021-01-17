package BackEndStructure.Entities;

import java.util.Arrays;

public class AttackingHandler {

    private int lostTroopsDefenders;
    private int lostTroopsAttackers;

    // Simulates 1 fight with 1-3 attackers and 1-2 defenders
    // AttackValues is always size 3 and DefendValues always size 2
    public void oneFight(int attackers, int[] attackValues, int defenders, int[] defendValues) {
        switch (attackers) {
            case 1:
                attackValues[1] = 0;
                attackValues[2] = 0;
                break;
            case 2:
                attackValues[2] = 0;
        }

        if (defenders == 1) {
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


