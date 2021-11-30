package sweng_plus.framework.networking.interfaces;

public interface IHostEventsListener<C extends IClient>
{
    void clientConnected(C client); // Not called from main thread
    
    void clientDisconnected(C client); // Not called from main thread
    
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
        };
    }
}
