package sweng_plus.framework.networking.interfaces;

public interface IAdvancedClientEventsListener extends IClientEventsListener
{
    default void forcedDisconnected()
    {
        disconnected();
    }
    
    default void serverClosed()
    {
        disconnected();
    }
    
    default void kickedFromServer()
    {
        disconnected();
    }
    
    default void kickedFromServerWithMessage(String message)
    {
        kickedFromServer();
    }
}
