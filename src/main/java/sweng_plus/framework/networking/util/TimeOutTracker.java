package sweng_plus.framework.networking.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TimeOutTracker
{
    // How many seconds after the last packet was received is a ping to be sent?
    public static final int PING_START_TIME = 4 * 20;
    
    // How many pings are to be sent before ultimately giving up?
    public static final int PING_REPETITIONS = 3;
    
    // How long to wait for a response after sending a ping?
    public static final int PING_WAIT_TIME = 2 * 20;
    
    protected Runnable sendPing;
    protected Runnable onTimeOut;
    
    protected ReentrantReadWriteLock lock;
    
    protected int time;
    protected int pings;
    protected int nextPing;
    
    public TimeOutTracker(Runnable sendPing, Runnable onTimeOut)
    {
        this.sendPing = sendPing;
        this.onTimeOut = onTimeOut;
        lock = new ReentrantReadWriteLock();
        reset();
    }
    
    public void reset()
    {
        Lock lock = this.lock.writeLock();
        
        try
        {
            lock.lock();
            time = 0;
            pings = 0;
            nextPing = PING_START_TIME;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void update()
    {
        Lock lock = this.lock.writeLock();
        
        try
        {
            lock.lock();
            
            time++;
            
            if(time >= nextPing)
            {
                if(pings < PING_REPETITIONS)
                {
                    pings++;
                    nextPing += PING_WAIT_TIME;
                    sendPing.run();
                }
                else if(time == nextPing)
                {
                    onTimeOut.run();
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }
}
