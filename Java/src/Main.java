import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        NetworkSimulator simulator;

        int trace = -1;
        int hasLinkChange = -1;
        long seed = -1;
        String buffer = "";
        boolean hasChange;

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Distance Vector Routing :");

        //get trace from user
        while (trace < 0)
        {
            System.out.print("Enter trace level (>= 0): [0] ");
            try
            {
                buffer = stdIn.readLine();
            }
            catch (IOException ioe)
            {
                System.out.println("IOError reading your input!");
                System.exit(1);
            }

            if (buffer.equals(""))
            {
                trace = 0;
            }
            else
            {
                try
                {
                    trace = Integer.parseInt(buffer);
                }
                catch (NumberFormatException nfe)
                {
                    trace = -1;
                }
            }
        }
        // get hasLinkChanged from user
        while ((hasLinkChange < 0) || (hasLinkChange > 1))
        {
            System.out.print("Will the link change (1 = Yes, 0 = No): ");
            try
            {
                buffer = stdIn.readLine();
            }
            catch (IOException ioe)
            {
                System.out.println("IOError reading your input!");
                System.exit(1);
            }

            if (buffer.equals(""))
            {
                hasLinkChange = 0;
            }
            else
            {
                try
                {
                    hasLinkChange = Integer.parseInt(buffer);
                }
                catch (NumberFormatException nfe)
                {
                    hasLinkChange = -1;
                }
            }
        }

        // get seed from user
        while (seed < 1)
        {
            System.out.print("Enter random seed: ");
            try
            {
                buffer = stdIn.readLine();
            }
            catch (IOException ioe)
            {
                System.out.println("IOError reading your input!");
                System.exit(1);
            }

            if (buffer.equals(""))
            {
                seed = System.currentTimeMillis();
            }
            else
            {
                try
                {
                    seed = (Long.valueOf(buffer)).longValue();
                }
                catch (NumberFormatException nfe)
                {
                    seed = -1;
                }
            }
        }

        if (hasLinkChange == 0)
        {
            hasChange = false;
        }
        else
        {
            hasChange = true;
        }

        simulator = new NetworkSimulator(hasChange, trace, seed);

        simulator.runSimulator();
    }
}
