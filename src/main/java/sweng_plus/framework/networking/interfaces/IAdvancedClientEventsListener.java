package sweng_plus.framework.networking.interfaces;

public interface IAdvancedClientEventsListener extends IClientEventsListener
{
    void lostConnection();
    
    default void forcedDisconnected()
    {
        lostConnection();
    }
    
    default void serverClosed()
    {
        lostConnection();
    }
    
    default void kickedFromServer()
    {
        lostConnection();
    }
    
    default void kickedFromServerWithMessage(String message)
    {
        kickedFromServer();
    }
}
