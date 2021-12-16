package sweng_plus.framework.networking;

import sweng_plus.framework.networking.HostManager;
import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.IClientFactory;

import java.io.IOException;

public class AdvancedHostManager<C extends IClient> extends HostManager<C>
{
    public IAdvancedMessageRegistry<C> advancedRegistry;
    public IAdvancedHostEventsListener<C> advancedEventsListener;
    
    public AdvancedHostManager(IAdvancedMessageRegistry<C> registry, IAdvancedHostEventsListener<C> eventsListener,
                               IClientFactory<C> clientFactory, int port) throws IOException
    {
        super(registry, eventsListener, clientFactory, port);
        advancedRegistry = registry;
        advancedEventsListener = eventsListener;
    }
}
