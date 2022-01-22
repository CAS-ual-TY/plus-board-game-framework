package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.util.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class AdvancedHostManager<C extends IAdvancedClient> extends HostManager<C> implements IAdvancedHostManager<C>
{
    protected IAdvancedMessageRegistry<C> advancedRegistry;
    protected IAdvancedHostEventsListener<C> advancedEventsListener;
    protected IHostSessionManager sessionManager;
    
    protected String clientName;
    
    protected HashMap<UUID, C> uuidClientsMap;
    
    protected LockedObject<HashMap<C, TimeOutTracker>> clientTimeOutTrackerMap;
    protected LockedObject<HashMap<C, AuthTracker<C>>> clientAuthTrackerMap;
    protected LinkedList<C> authTrackersToRemove;
    
    public AdvancedHostManager(IAdvancedMessageRegistry<C> registry, IAdvancedHostEventsListener<C> eventsListener,
                               IClientFactory<C> clientFactory, IHostSessionManager sessionManager, String clientName, int port) throws IOException
    {
        super(registry, eventsListener, clientFactory, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        this.sessionManager = sessionManager;
        
        this.clientName = clientName;
        
        uuidClientsMap = new HashMap<>();
        
        clientTimeOutTrackerMap = new LockedObject<>(new HashMap<>());
        clientAuthTrackerMap = new LockedObject<>(new HashMap<>());
        authTrackersToRemove = new LinkedList<>();
    }
    
    @Override
    public void update()
    {
        super.update();
        
        clientTimeOutTrackerMap.shared(clientTimeOutTrackerMap1 ->
                clientTimeOutTrackerMap1.values().forEach(TimeOutTracker::update));
        
        if(authTrackersToRemove.size() > 0)
        {
            clientAuthTrackerMap.exclusiveGet(clientAuthTrackerMap1 ->
            {
                authTrackersToRemove.forEach(clientAuthTrackerMap1::remove);
                clientAuthTrackerMap1.values().forEach(AuthTracker::update);
            });
        }
        else
        {
            clientAuthTrackerMap.shared(clientAuthTrackerMap1 ->
                    clientAuthTrackerMap1.values().forEach(AuthTracker::update));
        }
    }
    
    @Override
    public void close()
    {
        sendMessageToAllClientsExceptHost(advancedRegistry.serverClosed());
        
        super.close();
    }
    
    @Override
    public <M> void receivedMessage(M msg, IMessageHandler<M, C> handler, C client)
    {
        super.receivedMessage(msg, handler, client);
        
        clientTimeOutTrackerMap.shared(clientTimeOutTrackerMap1 ->
        {
            TimeOutTracker tracker = clientTimeOutTrackerMap1.get(client);
            tracker.reset();
        });
    }
    
    @Override
    public void connectionSocketCreated()
    {
        super.connectionSocketCreated();
    }
    
    @Override
    protected void clientConnected(C client)
    {
        clientAuthTrackerMap.exclusiveGet(authTrackers1 ->
                authTrackers1.put(client, new AuthTracker<>(client, authTrackersToRemove)));
        super.clientConnected(client);
        System.out.println("sending!");
        sendMessageToClient(client, advancedRegistry.authRequest());
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
        catch(IOException ignored)
        {
        }
    }
    
    public void clientLostConnection(C client)
    {
        if(client.getStatus() == ClientStatus.CONNECTED)
        {
            closeClient(client);
            advancedEventsListener.clientLostConnection(client);
        }
    }
    
    @Override
    public String getName()
    {
        return clientName;
    }
    
    @Override
    public void kickClient(C client)
    {
        sendMessageToClient(client, advancedRegistry.kickClient());
        closeClient(client);
    }
    
    @Override
    public void kickClient(C client, String message)
    {
        sendMessageToClient(client, advancedRegistry.kickClient(message));
        closeClient(client);
    }
    
    @Override
    public void authenticate(C client, String name, UUID identifier)
    {
        System.out.println("auth!!");
        
        name = name.trim();
        
        authTrackersToRemove.add(client);
        
        if(advancedEventsListener.authPredicate(identifier, name))
        {
            client.setName(name);
            client.setUUID(identifier);
            
            C old = uuidClientsMap.put(identifier, client);
            
            if(old != null)
            {
                advancedEventsListener.clientReconnected(old, client);
            }
            else
            {
                advancedEventsListener.clientAuthSuccessful(client);
            }
        }
        else
        {
            closeClient(client);
            kickClient(client, "Authentication failed.");
            advancedEventsListener.clientAuthUnsuccessful(client);
        }
    }
    
    @Override
    public UUID getSessionIdentifier()
    {
        return sessionManager.getSessionIdentifier();
    }
}
