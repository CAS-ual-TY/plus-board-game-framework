package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;

import java.io.IOException;

public class AdvancedClientManager<C extends IClient> extends ClientManager<C>
{
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedClientEventsListener advancedEventsListener;
    
    public AdvancedClientManager(IAdvancedMessageRegistry<C> registry, IAdvancedClientEventsListener eventsListener,
                                 String ip, int port) throws IOException
    {
        super(registry, eventsListener, ip, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
    }
}
