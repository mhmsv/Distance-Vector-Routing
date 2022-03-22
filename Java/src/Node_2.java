public class Node_2 extends Node {

    int[][] distanceTable ;
    Boolean[] neighbors ;

    public Node_2(){
        distanceTable = new int[NetworkSimulator.NUMNODES][NetworkSimulator.NUMNODES];
        neighbors = new Boolean[NetworkSimulator.NUMNODES];
    }
    public void rtinit() {
        // Initialize distance table to INFINITY and neighbors array to NO for all edges
        int i, j;
        for (i = 0; i < NetworkSimulator.NUMNODES; i++) {
            for (j = 0; j <  NetworkSimulator.NUMNODES; j++) {
                distanceTable[i][j] =  NetworkSimulator.INFINITY;
            }
            neighbors[i] = false;
        }

        // Update distance table for neighbors
        for (i = 0; i < NetworkSimulator.NUMNODES ; i++) {
            if (NetworkSimulator.cost[2][i] < NetworkSimulator.INFINITY) {
                distanceTable[2][i] = NetworkSimulator.cost[2][i];
                if (i != 2)
                    neighbors[i] = true;
            }
        }

        System.out.println("node 2 initial distance vector: "+ distanceTable[2][0]+" "+
                distanceTable[2][1]+" "+ distanceTable[2][2]+" "+  distanceTable[2][3] );

        // Update neighbors
        sendRoutePackets();
    }

    public void rtupdate(Packet pkt ) {
        System.out.println("rtupdate2() called, by a pkt received from Sender id: " + pkt.getSource());

        // Current shortest distance to node of origin
        int destDistance = distanceTable[2][pkt.getSource()];

        // Only want to send out new packets once, so wait until loop is finished
        boolean changedFlag = false;


        for (int i = 0; i < NetworkSimulator.NUMNODES; i++) {
            if (pkt.getMincost(i) != distanceTable[pkt.getSource()][i])
                distanceTable[pkt.getSource()][i]=pkt.getMincost(i);
        }

        for (int i = 0; i < NetworkSimulator.NUMNODES; i++) {
            int oldDistance = distanceTable[2][i];
            int newDistance = destDistance + pkt.getMincost(i);
            if (newDistance < oldDistance) {
                changedFlag = true;
                distanceTable[2][i] = newDistance;
            }
            System.out.println("node 2 current distance vector: " + distanceTable[2][0]+" "+
                    distanceTable[2][1]+" "+ distanceTable[2][2]+" "+  distanceTable[2][3]);
        }
        if (changedFlag) {
            sendRoutePackets();
        }
    }


    public void sendRoutePackets() {

        // Send RoutePackets to all neighbors
        for (int i = 0; i < NetworkSimulator.NUMNODES; i++) {
            if (!neighbors[i] || i == 2 || !NetworkSimulator.activeNodes[i]) continue;

            System.out.println("node 2 sends packet to node " + i +" with: "+ distanceTable[2][0]+" "+
                    distanceTable[2][1]+" "+ distanceTable[2][2]+" "+  distanceTable[2][3] );
            // Create and initialize route packet structure, except for destid.
            int[] minCosts = new int[NetworkSimulator.NUMNODES];
            System.arraycopy(distanceTable[2], 0, minCosts, 0, NetworkSimulator.NUMNODES);
            Packet pkt = new Packet(2 , i , minCosts);
            NetworkSimulator.toNode(pkt);
        }
    }

    public void printDT()
    {
        System.out.println();
        System.out.println(" D2 |   0   1   2   3");
        System.out.println("----+----------------");
        for (int i = 0; i < NetworkSimulator.NUMNODES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMNODES; j++)
            {
                if (distanceTable[i][j] < 10)
                {
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else
                {
                    System.out.print(" ");
                }

                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
