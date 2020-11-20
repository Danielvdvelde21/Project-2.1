package BackEndStructure.Graph;

import BackEndStructure.Entities.Player;

public class Territory {
    private final String territoryName;
    private final int territoryNumber;
    private int numberOfTroops;
    private Player owner;

    public Territory(String territoryName, int num) {
        this.territoryName = territoryName;
        this.territoryNumber = num;
        this.numberOfTroops = 0;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setNumberOfTroops(int troops) {
        this.numberOfTroops = troops;
    }

    public int getNumberOfTroops() {
        return numberOfTroops;
    }

    public void setOwner(Player playerName) {
        this.owner = playerName;
    }

    public Player getOwner() {
        return owner;
    }

    public int getTerritoryNumber() { return territoryNumber; }
}
