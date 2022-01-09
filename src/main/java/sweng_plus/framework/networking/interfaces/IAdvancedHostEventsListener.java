package sweng_plus.framework.networking.interfaces;

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
    
    default void clientAuthUnsuccessful(C client) {}
}
