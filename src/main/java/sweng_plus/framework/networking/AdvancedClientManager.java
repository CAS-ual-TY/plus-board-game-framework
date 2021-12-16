package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class AdvancedClientManager<C extends IClient> extends ClientManager<C>
{
    // How many seconds after the last packet was received is a ping to be sent?
    public static final int PING_START_TIME = 4 * 20;
    
    // How many pings are to be sent before ultimately giving up?
    public static final int PING_REPETITIONS = 3;
    
    // How long to wait for a response after sending a ping?
    public static final int PING_WAIT_TIME = 2 * 20;
    
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedClientEventsListener advancedEventsListener;
    
    protected ReentrantReadWriteLock lifeTimeLock;
    protected int lifeTime;
    protected int pings;
    protected int nextPing;
    
    public AdvancedClientManager(IAdvancedMessageRegistry<C> registry, IAdvancedClientEventsListener eventsListener,
                                 String ip, int port) throws IOException
    {
        super(registry, eventsListener, ip, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        
        lifeTimeLock = new ReentrantReadWriteLock();
        resetLifeTime();
    }
    
    @Override
    public void update()
    {
        if(close)
        {
            return;
        }
        
        super.update();
        
        Lock lock = lifeTimeLock.writeLock();
        
        try
        {
            lock.lock();
            
            lifeTime++;
            
            if(lifeTime >= nextPing)
            {
                if(pings >= PING_REPETITIONS)
                {
                    close();
                    advancedEventsListener.lostConnection();
                }
                else
                {
                    try
                    {
                        sendMessageToServer(advancedRegistry.requestPing());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    
                    pings++;
                    nextPing += PING_WAIT_TIME;
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public void receivedMessage(Consumer<Optional<C>> message)
    {
        super.receivedMessage(message);
        
        Lock lock = lifeTimeLock.writeLock();
        
        try
        {
            lock.lock();
            resetLifeTime();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void resetLifeTime()
    {
        lifeTime = 0;
        pings = 0;
        nextPing = PING_START_TIME;
    }
}
