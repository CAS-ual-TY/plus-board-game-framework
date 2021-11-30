package sweng_plus.framework.networking.interfaces;

public interface IClientEventsListener
{
    void disconnected();
    
    void disconnectedWithException(Exception e);
    
    IClientEventsListener EMPTY = new IClientEventsListener()
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
