package sweng_plus.framework.networking.interfaces;

public interface IHostEventsListener<C extends IClient> extends IClientEventsListener
{
    void clientConnected(C client);
    
    void clientDisconnected(C client);
    
    @Override
    default void disconnectedWithException(Exception e)
    {
        disconnected();
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
            public void clientDisconnected(C client)
            {
            
            }
            
            @Override
            public void disconnected()
            {
            
            }
        };
    }
}
