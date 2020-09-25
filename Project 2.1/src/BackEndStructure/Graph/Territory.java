package BackEndStructure.Graph;

public class Territory {

    private String territoryName;
    private int numberOfTroops;
    private String owner = "unowned";

    public Territory(String territoryName) {
        this.territoryName = territoryName;
        this.numberOfTroops = 0;
    }

    public String get_TerritoryName() {
        return territoryName;
    }

    public void setNumberOfTroops(int troops) {
        this.numberOfTroops = troops;
    }

    public int getNumberOfTroops() {
        return numberOfTroops;
    }

    public void setOwner(String playerName) {
        this.owner = playerName;
    }

    public String getOwner() {
        return owner;
    }
}
