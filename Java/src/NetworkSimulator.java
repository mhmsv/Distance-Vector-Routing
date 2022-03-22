import java.util.Random;

public class NetworkSimulator {

    public static final int INFINITY = 999;

    // This is the number of nodes in the simulator
    public static final int NUMNODES = 4;

    // These constants are possible events
    public static final int FROMNODE = 0;
    public static final int LINKCHANGE = 1;

    // Parameters of the simulation
    private boolean linkChanges;
    private static int traceLevel;
    private static EventList eventList;
    private static Random rand;

    // Data used for the simulation
    public Node[] nodes;
    public static int[][] cost;
    private static double time;
    public static Boolean[] activeNodes;

    Node n0;
    Node n1;
    Node n2;
    Node n3;
    // Initializes the simulator
    public NetworkSimulator(boolean hasLinkChange, int trace, long seed)
    {
        linkChanges = hasLinkChange;
        traceLevel = trace;
        eventList = new EventListImpl();
        rand = new Random(seed);
        time = 0.0;

        cost = new int[NUMNODES][NUMNODES];
        cost[0][0] = 0;
        cost[0][1] = 1;
        cost[0][2] = 3;
        cost[0][3] = 7;
        cost[1][0] = 1;
        cost[1][1] = 0;
        cost[1][2] = 1;
        cost[1][3] = 999;
        cost[2][0] = 3;
        cost[2][1] = 1;
        cost[2][2] = 0;
        cost[2][3] = 2;
        cost[3][0] = 7;
        cost[3][1] = 999;
        cost[3][2] = 2;
        cost[3][3] = 0;

        n0 = new Node_0();
        n1 = new Node_1();
        n2 = new Node_2();
        n3 = new Node_3();

        nodes = new Node[] {n0 , n1 , n2 , n3};
        activeNodes = new Boolean[NUMNODES];
        activeNodes[0]=false;
        activeNodes[1]=false;
        activeNodes[2]=false;
        activeNodes[3]=false;

        n0.rtinit();
        activeNodes[0]=true;

        n1.rtinit();
        activeNodes[1]=true;

        n2.rtinit();
        activeNodes[2]=true;

        n3.rtinit();
        activeNodes[3]=true;


        if (linkChanges)
            eventList.add(new Event(10000.0, LINKCHANGE, 0));


    }

    // Starts the simulation. It will end when no more packets are in the medium

    public void runSimulator()
    {
        Event next;
        Packet p;

        while(true)
        {
            next = eventList.removeNext();

            if (next == null)
                break;

            if (traceLevel > 1)
            {
                System.out.println();
                System.out.println("main(): event received.  t=" + next.getTime() +", node=" + next.getNode());

                if (next.getType() == FROMNODE)
                {
                    p = next.getPacket();

                    System.out.print("  src=" + p.getSource() + ", ");
                    System.out.print("dest=" + p.getDest() + ", ");
                    System.out.print("contents=[");
                    for (int i = 0; i < NUMNODES - 1; i++)
                        System.out.print(p.getMincost(i) + ", ");
                    System.out.println(p.getMincost(NUMNODES - 1) + "]");

                }
                else if (next.getType() == LINKCHANGE)
                {
                    System.out.println("  Link cost change.");
                }
            }

            time = next.getTime();

            if (next.getType() == FROMNODE)
            {
                p = next.getPacket();
                if ((next.getNode() < 0) || (next.getNode() >= NUMNODES))
                    System.out.println("main(): Panic. Unknown event node.");
                else
                    nodes[next.getNode()].rtupdate(p);
            }
            else
            {
                System.out.println("main(): Panic.  Unknown event type.");
            }
        }

        for (int i = 0 ; i < NUMNODES ; i++){
            nodes[i].printDT();
            System.out.println();
        }
        System.out.println("Simulator terminated at t=" + time +
                ", no packets in medium.");
    }


    public static void toNode(Packet p)
    {
        Packet currentPacket;
        double arrivalTime;

        if ((p.getSource() < 0) || (p.getSource() >= NUMNODES))
        {
            System.out.println("WARNING: Illegal source id in packet; ignoring.");
            return;
        }
        if ((p.getDest() < 0) || (p.getDest() >= NUMNODES))
        {
            System.out.println("WARNING: Illegal destination id in packet; ignoring.");
            return;
        }
        if (p.getSource() == p.getDest())
        {
            System.out.println("WARNING: Identical source and destination in packet; ignoring.");
            return;
        }
        if (cost[p.getSource()][p.getDest()] == 999)
        {
            System.out.println("WARNING: Source and destination not connected; ignoring.");
            return;
        }


        if (traceLevel > 2)
        {
            System.out.println("             toNode(): source=" + p.getSource() +
                    " dest=" + p.getDest());
            System.out.print("             costs:");
            for (int i = 0; i < NUMNODES; i++)
            {
                System.out.print(" " + p.getMincost(i));
            }
            System.out.println();
        }

        arrivalTime = eventList.getLastPacketTime(p.getSource(), p.getDest());
        if (arrivalTime == 0.0)
        {
            arrivalTime = time;
        }
        arrivalTime = arrivalTime + 1.0 + (rand.nextDouble() * 9.0);

        if (traceLevel > 2)
        {
            System.out.println("toNode(): Scheduling arrival of packet.");
        }

        currentPacket = new Packet(p);
        eventList.add(new Event(arrivalTime, FROMNODE,
                currentPacket.getDest(), currentPacket));
    }
}

