package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.util.LockedObject;
import sweng_plus.framework.networking.util.TimeOutTracker;

import java.io.IOException;
import java.util.UUID;

public class AdvancedClientManager<C extends IClient> extends ClientManager<C> implements IAdvancedClientManager
{
    protected IAdvancedMessageRegistry<C> advancedRegistry;
    protected IAdvancedClientEventsListener advancedEventsListener;
    protected IClientSessionManager sessionManager;
    
    protected String name;
    
    protected UUID sessionIdentifier;
    protected UUID clientIdentifier;
    
    protected LockedObject<TimeOutTracker> timeOutTracker;
    
    public AdvancedClientManager(IAdvancedMessageRegistry<C> registry, IAdvancedClientEventsListener eventsListener,
                                 IClientSessionManager sessionManager, String name, String ip, int port) throws IOException
    {
        super(registry, eventsListener, ip, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        this.sessionManager = sessionManager;
        this.name = name;
        
        timeOutTracker = new LockedObject<>(new TimeOutTracker(this::sendPing, this::lostConnection));
    }
    
    @Override
    public void update()
    {
        super.update();
        
        timeOutTracker.exclusiveGet(TimeOutTracker::update);
    }
    
    @Override
    public void close()
    {
        try
        {
            sendMessageToServerUnsafe(advancedRegistry.orderlyDisconnected());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        super.close();
    }
    
    @Override
    public <M> void receivedMessage(M msg, byte uMsgPosition, IMessageHandler<M, C> handler)
    {
        timeOutTracker.exclusiveGet(TimeOutTracker::reset);
        super.receivedMessage(msg, uMsgPosition, handler);
    }
    
    public void sendPing()
    {
        try
        {
            sendMessageToServerUnsafe(advancedRegistry.requestPing());
        }
        catch(IOException ignored)
        {
        }
    }
    
    public void lostConnection()
    {
        if(!shouldClose())
        {
            close();
            advancedEventsListener.lostConnection();
        }
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public void setSessionIdentifier(UUID sessionIdentifier)
    {
        this.sessionIdentifier = sessionIdentifier;
        
        clientIdentifier = UUID.randomUUID(); //TODO
    }
    
    @Override
    public UUID getClientIdentifierForSession()
    {
        return sessionManager.getIdentifierForSession(sessionIdentifier);
    }
}
