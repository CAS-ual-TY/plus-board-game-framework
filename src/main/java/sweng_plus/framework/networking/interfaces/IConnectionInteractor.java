package sweng_plus.framework.networking.interfaces;

import java.util.Optional;
import java.util.function.Consumer;

public interface IConnectionInteractor<C extends IClient>
{
    void receivedMessage(Consumer<Optional<C>> message); // Connection Thread
    
    IMessageRegistry<C> getMessageRegistry(); // Connection Thread
    
    void socketClosed(); // Connection Thread
    
    void socketClosedWithException(Exception e); // Connection Thread
    
    boolean shouldClose(); // Connection Thread
}
