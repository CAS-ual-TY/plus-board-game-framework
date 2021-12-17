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
    <M> void sendMessageToServerUnsafe(M message) throws IOException; // Main Thread
    
    default <M> void sendMessageToServer(M message) // Main Thread
    {
        try
        {
            sendMessageToServerUnsafe(message);
        }
        catch(IOException e)
        {
            failedToSendMessage();
        }
    }
    
    /**
     * All received messages are decoded according to the used {@link MessageRegistry}
     * and turned into a {@link Runnable}. This method then runs them all.
     */
    void update(); // Main Thread
    
    void runOnMainThreadSafely(Runnable r);
    
    default void disconnect()
    {
        close();
    }
    
    default void failedToSendMessage()
    {
        disconnect();
    }
    
    @Override
    void close(); // Main Thread
}
