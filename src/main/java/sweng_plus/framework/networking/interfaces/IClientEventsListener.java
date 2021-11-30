package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    void disconnected(); // Not called from main thread
    
    void disconnectedWithException(Exception e); // Not called from main thread
    
    static IClientEventsListener emptyClientListener()
    {
        return new IClientEventsListener()
        {
            @Override
            public void disconnected()
            {
            
            }
            
            @Override
            public void disconnectedWithException(Exception e)
            {
            
            }
        };
    }
}
