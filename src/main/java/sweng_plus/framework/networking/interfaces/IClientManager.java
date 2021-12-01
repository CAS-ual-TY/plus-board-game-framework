package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface IClientManager extends Closeable
{
    /**
     * Sends a message to the server. The message must be registered in the used {@link MessageRegistry} (= protocol).
     *
     * @param message The message to be sent to the server.
     * @param <M>     The type of the message.
     * @throws IOException
     */
    <M> void sendMessageToServer(M message) throws IOException; // Main Thread
    
    /**
     * All received messages are decoded according to the used {@link MessageRegistry}
     * and turned into a {@link Runnable} and then passed to the given {@link Consumer} for execution.
     *
     * @param consumer The {@link Consumer} that receives every {@link Runnable} representing a decoded message
     *                 from the server.
     */
    void runMessages(Consumer<Runnable> consumer); // Main Thread
    
    @Override
    void close(); // Main Thread
}
