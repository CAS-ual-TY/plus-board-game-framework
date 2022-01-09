package sweng_plus.framework.networking.util;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuthTracker<C>
{
    // How many seconds after the last packet was received is a ping to be sent?
    public static final int AUTH_WAIT_TIME = 4 * 20;
    
    protected C client;
    protected List<C> removalList;
    
    protected ReentrantReadWriteLock lock;
    
    protected int time;
    
    public AuthTracker(C client, List<C> removalList)
    {
        this.client = client;
        this.removalList = removalList;
        lock = new ReentrantReadWriteLock();
    }
    
    public C getClient()
    {
        return client;
    }
    
    public void update()
    {
        Lock lock = this.lock.writeLock();
        
        try
        {
            lock.lock();
            
            time++;
            
            if(time >= AUTH_WAIT_TIME)
            {
                removalList.add(client);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
}
