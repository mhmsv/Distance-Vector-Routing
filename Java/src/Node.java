public abstract class Node {
    // Each entity will have a distance table
//    protected int[][] distanceTable = new int[NetworkSimulator.NUMNODES]
//            [NetworkSimulator.NUMNODES];
//
//    protected Boolean[] neighbors = new Boolean[NetworkSimulator.NUMNODES];

    public abstract void rtupdate(Packet pkt ) ;
    public abstract void rtinit() ;
    public abstract void sendRoutePackets();

    protected abstract void printDT();
}
