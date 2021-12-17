package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.IClientFactory;
import sweng_plus.framework.networking.util.TimeOutTracker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class AdvancedHostManager<C extends IClient> extends HostManager<C>
{
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedHostEventsListener<C> advancedEventsListener;
    
    protected ReentrantReadWriteLock clientTimeOutTrackerMapLock;
    public HashMap<C, TimeOutTracker> clientTimeOutTrackerMap;
    
    public AdvancedHostManager(IAdvancedMessageRegistry<C> registry, IAdvancedHostEventsListener<C> eventsListener,
                               IClientFactory<C> clientFactory, int port) throws IOException
    {
        super(registry, eventsListener, clientFactory, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        
        clientTimeOutTrackerMapLock = new ReentrantReadWriteLock();
        clientTimeOutTrackerMap = new HashMap<>();
    }
    
    @Override
    public void close()
    {
        try
        {
            sendMessageToAllClients(advancedRegistry.serverClosed());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        super.close();
    }
    
    @Override
    public void receivedMessage(Consumer<Optional<C>> message, Optional<C> client)
    {
        super.receivedMessage(message, client);
        
        client.ifPresent(c ->
        {
            Lock lock = clientTimeOutTrackerMapLock.readLock();
            
            try
            {
                lock.lock();
                TimeOutTracker tracker = clientTimeOutTrackerMap.get(c);
                tracker.reset();
            }
            finally
            {
                lock.unlock();
            }
        });
    }
    
    @Override
    public void addClient(Connection<C> connection, C client)
    {
        super.addClient(connection, client);
        
        Lock lock = clientTimeOutTrackerMapLock.writeLock();
        
        try
        {
            lock.lock();
            clientTimeOutTrackerMap.put(client, new TimeOutTracker(() -> sendPing(client), () -> lostConnection(client)));
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public void removeClient(C client)
    {
        super.removeClient(client);
        
        Lock lock = clientTimeOutTrackerMapLock.writeLock();
        
        try
        {
            lock.lock();
            clientTimeOutTrackerMap.remove(client);
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void sendPing(C client)
    {
        try
        {
            sendMessageToClient(client, advancedRegistry.requestPing());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void lostConnection(C client)
    {
        removeClient(client);
        advancedEventsListener.clientLostConnection(client);
    }
}
