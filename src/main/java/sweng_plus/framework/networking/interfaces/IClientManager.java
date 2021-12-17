package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;

import java.io.Closeable;
import java.io.IOException;

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
    
    default <M> void trySendMessageToServer(M message) // Main Thread
    {
        try
        {
            sendMessageToServer(message);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * All received messages are decoded according to the used {@link MessageRegistry}
     * and turned into a {@link Runnable}. This method then runs them all.
     */
    void update(); // Main Thread
    
    @Override
    void close(); // Main Thread
}
