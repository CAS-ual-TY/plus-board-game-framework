package sweng_plus.framework.networking.interfaces;

public interface IHostEventsListener<C extends IClient>
{
    void clientConnected(C client); // Called from main thread
    
    default void clientSocketClosed(C client) // Called from main thread
    {
    
    }
    
    default void clientSocketClosedWithException(C client, Exception e) // Called from main thread
    {
        clientSocketClosed(client);
    }
    
    static <C extends IClient> IHostEventsListener<C> emptyHostListener()
    {
        return new IHostEventsListener<>()
        {
            
            @Override
            public void clientConnected(C client)
            {
            
            }
            
            @Override
            public void clientSocketClosed(C client)
            {
            
            }
        };
    }
}
