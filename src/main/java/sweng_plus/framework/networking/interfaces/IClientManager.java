package sweng_plus.framework.networking.interfaces;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface IClientManager extends Closeable
{
    <M> void sendMessageToServer(M message) throws IOException; // Main Thread
    
    void runMessages(Consumer<Runnable> consumer); // Main Thread
    
    @Override
    void close(); // Main Thread
}
