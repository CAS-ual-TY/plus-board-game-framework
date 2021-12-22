package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.IClientFactory;
import sweng_plus.framework.networking.util.LockedObject;
import sweng_plus.framework.networking.util.TimeOutTracker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class AdvancedHostManager<C extends IClient> extends HostManager<C>
{
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedHostEventsListener<C> advancedEventsListener;
    
    public LockedObject<HashMap<C, TimeOutTracker>> clientTimeOutTrackerMap;
    
    public AdvancedHostManager(IAdvancedMessageRegistry<C> registry, IAdvancedHostEventsListener<C> eventsListener,
                               IClientFactory<C> clientFactory, int port) throws IOException
    {
        super(registry, eventsListener, clientFactory, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        clientTimeOutTrackerMap = new LockedObject<>(new HashMap<>());
    }
    
    @Override
    public void update()
    {
        super.update();
        
        if(shouldClose())
        {
            return;
        }
        
        clientTimeOutTrackerMap.shared(clientTimeOutTrackerMap1 ->
                clientTimeOutTrackerMap1.values().forEach(TimeOutTracker::update));
    }
    
    @Override
    public void close()
    {
        sendMessageToAllClientsExceptHost(advancedRegistry.serverClosed());
        super.close();
    }
    
    @Override
    public void receivedMessage(Consumer<Optional<C>> message, Optional<C> client)
    {
        super.receivedMessage(message, client);
        
        client.ifPresent(c ->
                clientTimeOutTrackerMap.shared(clientTimeOutTrackerMap1 ->
                {
                    TimeOutTracker tracker = clientTimeOutTrackerMap1.get(c);
                    tracker.reset();
                }));
    }
    
    @Override
    public void addClient(Connection<C> connection, C client)
    {
        super.addClient(connection, client);
        
        clientTimeOutTrackerMap.exclusiveGet(clientTimeOutTrackerMap1 ->
                clientTimeOutTrackerMap1.put(client, new TimeOutTracker(() -> sendPing(client), () -> clientLostConnection(client))));
    }
    
    @Override
    public void removeClient(C client)
    {
        super.removeClient(client);
        
        clientTimeOutTrackerMap.exclusiveGet(clientTimeOutTrackerMap1 ->
                clientTimeOutTrackerMap1.remove(client));
    }
    
    public void sendPing(C client)
    {
        try
        {
            sendMessageToClientUnsafe(client, advancedRegistry.requestPing());
        }
        catch(IOException e)
        {
        }
    }
    
    public void clientLostConnection(C client)
    {
        closeClient(client);
        advancedEventsListener.clientLostConnection(client);
    }
}
