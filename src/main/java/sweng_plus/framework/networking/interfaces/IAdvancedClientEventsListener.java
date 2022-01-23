package sweng_plus.framework.networking.interfaces;

public interface IAdvancedClientEventsListener extends IClientEventsListener
{
    /**
     * Called if the connection to the server has been lost (connection timed out).
     */
    void lostConnection();
    
    /**
     * Called if this client specifically has been forcibly disconnected from the server
     * (but the server did not specify this action as a kick).
     */
    default void forcedDisconnected()
    {
        lostConnection();
    }
    
    /**
     * Called if the server has been closed.
     */
    default void serverClosed()
    {
        lostConnection();
    }
    
    /**
     * Called if this client specifically has been kicked from the server without a message supplied
     * representing a reason or justification.
     */
    default void kickedFromServer()
    {
        lostConnection();
    }
    
    /**
     * Called if this client specifically has been kicked from the server with a message supplied
     * representing a reason or justification.
     */
    default void kickedFromServerWithMessage(String message)
    {
        kickedFromServer();
    }
}
