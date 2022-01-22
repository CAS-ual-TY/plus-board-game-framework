package sweng_plus.framework.networking.interfaces;

public interface IConnectionInteractor<C extends IClient>
{
    void connectionSocketCreated(); // Connection Thread
    
    <M> void receivedMessage(M msg, IMessageHandler<M, C> handler); // Connection Thread
    
    IMessageRegistry<C> getMessageRegistry(); // Connection Thread
    
    void connectionSocketClosed(); // Connection Thread
    
    void connectionSocketClosedWithException(Exception e); // Connection Thread
    
    boolean shouldClose(); // Connection Thread
}
