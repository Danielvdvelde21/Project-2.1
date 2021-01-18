package AI.MCTS;

// Class that uses Upper Confidence Bounds to make a smart decision about which node to explore
public class UCT {

    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node findBestChildWithUCT(Node node) {
        double maxUCT=0;
        int maxUCTind=0;
        double uct=0;
        for (int i=0;i<node.getChildrenSize();i++) {
            Node n=node.getChildren()[i];
            if(!n.isTerminal()){
                uct=uctValue(n.getParent().getVisitCount(), n.getWinScore(), n.getVisitCount());
            } else {
                uct=0;
            }
            if(uct>maxUCT){
                maxUCT=uct;
                maxUCTind=i;
            }
        }
        if(node.getChildren()[maxUCTind].isTerminal()){
            node.setTerminal();
        }
        return node.getChildren()[maxUCTind];
    }
}
