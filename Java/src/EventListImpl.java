import java.util.Vector;

public class EventListImpl implements EventList
{
    private Vector data;
    
    public EventListImpl()
    {
        data = new Vector();
    }
    
    public boolean add(Event e)
    {
        data.addElement(e);
        return true;
    }
    
    public Event removeNext()
    {
        if (data.isEmpty())
        {
            return null;
        }
    
        int firstIndex = 0;
        double first = ((Event)data.elementAt(firstIndex)).getTime();
        for (int i = 0; i < data.size(); i++)
        {
            if (((Event)data.elementAt(i)).getTime() < first)
            {
                first = ((Event)data.elementAt(i)).getTime();
                firstIndex = i;
            }
        }
        
        Event next = (Event)data.elementAt(firstIndex);
        data.removeElement(next);
    
        return next;
    }
    
    public String toString()
    {
        return data.toString();
    }
    
    public double getLastPacketTime(int nodeFrom, int nodeTo)
    {
        double time = 0.0;
        for (int i = 0; i < data.size(); i++)
        {
            if ((((Event)(data.elementAt(i))).getType() == 
                                           NetworkSimulator.FROMNODE) &&
                (((Event)(data.elementAt(i))).getNode() == nodeTo) &&
                (((Event)(data.elementAt(i))).getPacket().getSource() ==
                                           nodeFrom)
               )
            {
                time = ((Event)(data.elementAt(i))).getTime();
            }
        }
    
        return time;
    }
}
