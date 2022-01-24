package sweng_plus.framework.networking.interfaces;

public interface IHostEventsListener<C extends IClient>
{
    /**
     * Called after a client has successfully connected to the server.
     *
     * @param client The {@link C} instance representing the client which has connected to the server.
     */
    void clientConnected(C client);
    
    /**
     * Called after a client connection socket has been closed without an exception thrown.
     *
     * @param client The {@link C} instance representing the client the socket has closed of.
     */
    default void clientSocketClosed(C client)
    {
    
    }
    
    /**
     * Called after a client connection socket has been closed with an exception thrown.
     *
     * @param client The {@link C} instance representing the client the socket has closed of.
     * @param e      The thrown exception.
     */
    default void clientSocketClosedWithException(C client, Exception e)
    {
        clientSocketClosed(client);
    }
    
    /**
     * @return A dummy {@link IHostEventsListener} doing nothing.
     */
    static <C extends IClient> IHostEventsListener<C> emptyHostListener()
    {
        return new IHostEventsListener<>()
        {
            @Override
            public void clientConnected(C client)
            {
            
            }
        };
    }
}
