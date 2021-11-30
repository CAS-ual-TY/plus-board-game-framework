package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    void disconnected();
    
    void disconnectedWithException(Exception e);
    
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
