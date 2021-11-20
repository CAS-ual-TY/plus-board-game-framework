package sweng_plus.framework.networking.interfaces;

public interface IConnectionInteractor
{
    void receivedMessage(Runnable message); // Connection Thread
    
    IMessageRegistry getMessageRegistry(); // Connection Thread
    
    void socketClosed(); // Connection Thread
    
    void socketClosedWithException(Exception e); // Connection Thread
    
    boolean shouldClose(); // Connection Thread
}
