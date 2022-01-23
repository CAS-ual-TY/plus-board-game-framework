package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    /**
     * Called after the connection socket has closed orderly without an exception thrown.
     */
    default void socketClosed() // Called from main thread
    {
    
    }
    
    /**
     * Called after the connection socket has closed with an exception thrown.
     *
     * @param e The thrown exception.
     */
    default void socketClosedWithException(Exception e) // Called from main thread
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
