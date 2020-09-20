package BackEndStructure.Graph;

public class Territory {

    private String territoryName;
    private int numberOfTroops;
    private int belongsTo;

    public Territory(String territoryName, int numberOfTroops, int playerNumber) {
        this.territoryName = territoryName;
        this.numberOfTroops = numberOfTroops;
        this.belongsTo = playerNumber;
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
