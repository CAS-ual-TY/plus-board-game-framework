package sweng_plus.framework.networking.util;

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
    
    protected int time;
    protected int pings;
    protected int nextPing;
    
    public TimeOutTracker(Runnable sendPing, Runnable onTimeOut)
    {
        this.sendPing = sendPing;
        this.onTimeOut = onTimeOut;
        reset();
    }
    
    public void reset()
    {
        time = 0;
        pings = 0;
        nextPing = PING_START_TIME;
    }
    
    public void update()
    {
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
}
