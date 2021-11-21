package sweng_plus.framework.networking.interfaces;

import java.util.Optional;
import java.util.function.Consumer;

public interface IConnectionInteractor
{
    void receivedMessage(Consumer<Optional<IClient>> message); // Connection Thread
    
    IMessageRegistry getMessageRegistry(); // Connection Thread
    
    void socketClosed(); // Connection Thread
    
    void socketClosedWithException(Exception e); // Connection Thread
    
    boolean shouldClose(); // Connection Thread
}
