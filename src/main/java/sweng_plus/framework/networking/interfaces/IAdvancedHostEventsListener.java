package sweng_plus.framework.networking.interfaces;

public interface IAdvancedHostEventsListener<C extends IClient> extends IHostEventsListener<C>
{
    void clientDisconnectedOrderly(C client);
    
    default void clientDisconnectedDueToException(C client)
    {
        clientDisconnectedOrderly(client);
    }
    
    default void clientLostConnection(C client)
    {
        clientDisconnectedOrderly(client);
    }
}
