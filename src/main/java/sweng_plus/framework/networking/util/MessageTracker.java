package sweng_plus.framework.networking.util;

import sweng_plus.framework.networking.interfaces.IClient;

import java.util.Comparator;

public class MessageTracker
{
    protected byte position;
    
    public MessageTracker()
    {
        position = 0;
    }
    
    public int get()
    {
        return Byte.toUnsignedInt(position);
    }
    
    public int getThenIncrement()
    {
        return Byte.toUnsignedInt(position++);
    }
    
    public byte getByte()
    {
        return position;
    }
    
    public byte getByteThenIncrement()
    {
        return position++;
    }
    
    public void increment()
    {
        position++;
    }
    
    public <C extends IClient> Comparator<TrackedMessage<?, C>> makeComparator()
    {
        return (msg1, msg2) ->
        {
            int pos1 = msg1.getPosition();
            int pos2 = msg2.getPosition();
            int tpos = get();
            
            if(pos1 >= tpos && pos2 >= tpos)
            {
                return Integer.compare(pos1, pos2);
            }
            else if(pos1 >= tpos)
            {
                return -1;
            }
            else if(pos2 >= tpos)
            {
                return 1;
            }
            else
            {
                return Integer.compare(pos1, pos2);
            }
        };
    }
}
