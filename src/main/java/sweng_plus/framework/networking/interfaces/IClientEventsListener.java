package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    default void socketClosed() // Called from main thread
    {
    
    }
    
    default void socketClosedWithException(Exception e) // Called from main thread
    {
        socketClosed();
    }
    
    static IClientEventsListener emptyClientListener()
    {
        return new IClientEventsListener()
        {
            @Override
            public void socketClosed()
            {
            
            }
            
            @Override
            public void socketClosedWithException(Exception e)
            {
            
            }
        };
    }
}
