package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    void disconnected(); // Called from main thread
    
    void disconnectedWithException(Exception e); // Called from main thread
    
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
