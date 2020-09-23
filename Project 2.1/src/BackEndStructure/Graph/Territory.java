package BackEndStructure.Graph;

public class Territory {

    private String territoryName;
    private int numberOfTroops;
    // Change to Player name TODO
    private int belongsTo;

    public Territory(String territoryName, int numberOfTroops, int playerNumber) {
        this.territoryName = territoryName;
        this.numberOfTroops = numberOfTroops;
        this.belongsTo = playerNumber;
    }

    public Territory(String territoryName) {
        this.territoryName = territoryName;
        this.numberOfTroops = 0;
        this.belongsTo = 0;
    }

    public String get_TerritoryName() {
        return territoryName;
    }

    public void setNumberOfTroops(int num, int playerNum) {
        numberOfTroops = num;
        belongsTo = playerNum;
    }

    public int getNumberOfTroops() {
        return numberOfTroops;
    }
}
