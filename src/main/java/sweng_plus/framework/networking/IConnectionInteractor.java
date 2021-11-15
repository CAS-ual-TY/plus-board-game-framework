package sweng_plus.framework.networking;

public interface IConnectionInteractor
{
    void receivedMessage(Runnable message); // Connection Thread
    
    MessageRegistry getMessageRegistry(); // Connection Thread
    
    void socketClosed(); // Connection Thread
    
    void socketClosedWithException(Exception e); // Connection Thread
    
    boolean shouldClose(); // Connection Thread
}
