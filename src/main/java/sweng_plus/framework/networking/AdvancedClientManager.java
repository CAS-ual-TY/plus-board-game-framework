package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.LockedObject;
import sweng_plus.framework.networking.util.TimeOutTracker;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class AdvancedClientManager<C extends IClient> extends ClientManager<C>
{
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedClientEventsListener advancedEventsListener;
    
    protected LockedObject<TimeOutTracker> timeOutTracker;
    
    public AdvancedClientManager(IAdvancedMessageRegistry<C> registry, IAdvancedClientEventsListener eventsListener,
                                 String ip, int port) throws IOException
    {
        super(registry, eventsListener, ip, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
        
        timeOutTracker = new LockedObject<>(new TimeOutTracker(this::sendPing, this::lostConnection));
    }
    
    @Override
    public void update()
    {
        if(shouldClose())
        {
            return;
        }
        
        super.update();
        
        timeOutTracker.exclusiveGet(TimeOutTracker::update);
    }
    
    @Override
    public void receivedMessage(Consumer<Optional<C>> message)
    {
        timeOutTracker.exclusiveGet(TimeOutTracker::reset);
        super.receivedMessage(message);
    }
    
    public void sendPing()
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages ->
                mainThreadMessages.add(() -> sendMessageToServer(advancedRegistry.requestPing())));
    }
    
    public void lostConnection()
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages ->
                mainThreadMessages.add(() ->
                {
                    close();
                    advancedEventsListener.lostConnection();
                }));
    }
}
