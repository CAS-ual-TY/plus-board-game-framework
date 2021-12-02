package sweng_plus.framework.networking.interfaces;

public interface IAdvancedHostEventsListener<C extends IClient> extends IHostEventsListener<C>
{
    default void clientDisconnectedOrderly(C client)
    {
        clientDisconnected(client);
    }
    
    default void clientDisconnectedDueToException(C client)
    {
        clientDisconnected(client);
    }
}
