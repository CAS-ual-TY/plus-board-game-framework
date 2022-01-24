package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    /**
     * Called after the connection socket has been closed without an exception thrown.
     */
    default void socketClosed()
    {
    
    }
    
    /**
     * Called after the connection socket has been closed with an exception thrown.
     *
     * @param e The thrown exception.
     */
    default void socketClosedWithException(Exception e)
    {
        socketClosed();
    }
    
    /**
     * @return A dummy {@link IClientEventsListener} doing nothing.
     */
    static IClientEventsListener emptyClientEventsListener()
    {
        return new IClientEventsListener()
        {
        
        };
    }
}
