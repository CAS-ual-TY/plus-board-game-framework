package sweng_plus.framework.networking;

public interface IConnectionInteractor
{
    void receivedMessage(Runnable message); // Connection Thread
    
    MessageRegistry getMessageRegistry(); // Connection Thread
    
    boolean shouldClose(); // Connection Thread
}
