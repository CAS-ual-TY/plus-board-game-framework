package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedHostEventsListener<C extends IAdvancedClient> extends IHostEventsListener<C>, IAdvancedClientEventsListener
{
    @Override
    default void lostConnection() {}
    
    @Override
    default void clientConnected(C client) {}
    
    void clientDisconnectedOrderly(C client);
    
    default void clientDisconnectedDueToException(C client)
    {
        clientDisconnectedOrderly(client);
    }
    
    default void clientLostConnection(C client)
    {
        clientDisconnectedOrderly(client);
    }
    
    void clientAuthSuccessful(C client);
    
    default void clientReconnected(C oldClient, C newClient)
    {
        newClient.setName(oldClient.getName());
    }
    
    default void clientAuthUnsuccessful(C client) {}
    
    default boolean authPredicate(UUID identifier, String name)
    {
        return name.length() >= 3;
    }
}
