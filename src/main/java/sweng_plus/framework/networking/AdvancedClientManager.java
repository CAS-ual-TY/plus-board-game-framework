package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.TimeOutTracker;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class AdvancedClientManager<C extends IClient> extends ClientManager<C>
{
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedClientEventsListener advancedEventsListener;
    
    protected ReentrantReadWriteLock lifeTimeLock;
    protected TimeOutTracker timeOutTracker;
    
    public AdvancedClientManager(IAdvancedMessageRegistry<C> registry, IAdvancedClientEventsListener eventsListener,
                                 String ip, int port) throws IOException
    {
        super(registry, eventsListener, ip, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        
        lifeTimeLock = new ReentrantReadWriteLock();
        timeOutTracker = new TimeOutTracker(this::sendPing, this::lostConnection);
    }
    
    @Override
    public void update()
    {
        if(shouldClose())
        {
            return;
        }
        
        super.update();
        
        Lock lock = lifeTimeLock.writeLock();
        
        try
        {
            lock.lock();
            timeOutTracker.update();
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
            timeOutTracker.reset();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void sendPing()
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages ->
        {
            mainThreadMessages.add(() -> trySendMessageToServer(advancedRegistry.requestPing()));
        });
    }
    
    public void lostConnection()
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages ->
        {
            mainThreadMessages.add(() ->
            {
                close();
                advancedEventsListener.lostConnection();
            });
        });
    }
}
