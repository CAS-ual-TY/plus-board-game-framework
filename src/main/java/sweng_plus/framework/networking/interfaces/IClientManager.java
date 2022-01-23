package sweng_plus.framework.networking.interfaces;

import java.io.Closeable;
import java.io.IOException;

public interface IClientManager<C extends IClient> extends Closeable
{
    /**
     * @return The used protocol. Must be the same on all clients and the server.
     */
    IMessageRegistry<C> getMessageRegistry();
    
    /**
     * Sends a message to the server. The message must be registered in the used {@link IMessageRegistry} (= protocol).
     *
     * @param message The message to be sent to the server.
     * @param <M>     The type of the message.
     * @throws IOException
     * @see #sendMessageToServer(Object)
     */
    <M> void sendMessageToServerUnsafe(M message) throws IOException; // Main Thread
    
    /**
     * <p>Sends a message to the server. The message must be registered in the used {@link IMessageRegistry} (= protocol).</p>
     *
     * <p>If an {@link IOException} is thrown, it is caught and then {@link #failedToSendMessage(IOException)} is run.</p>
     *
     * @param message The message to be sent to the server.
     * @param <M>     The type of the message.
     * @see #sendMessageToServerUnsafe(Object)
     */
    default <M> void sendMessageToServer(M message) // Main Thread
    {
        try
        {
            sendMessageToServerUnsafe(message);
        }
        catch(IOException e)
        {
            failedToSendMessage(e);
        }
    }
    
    /**
     * All received messages are decoded according to the used {@link IMessageRegistry}
     * and turned into a {@link Runnable}. This method then runs them all.
     */
    void update(); // Main Thread
    
    /**
     * Runs a {@link Runnable} on the main thread when {@link #update()} is called next. Used by networking threads.
     *
     * @param r
     */
    void runOnMainThreadSafely(Runnable r); // Any Thread
    
    /**
     * Called by {@link #sendMessageToServer(Object)}
     *
     * @param e The thrown exception after failing to send a message.
     */
    default void failedToSendMessage(IOException e)
    {
        close();
    } // Main Thread
    
    /**
     * Closes the connection client-side and closes the socket and all associated resources and threads.
     */
    @Override
    void close(); // Main Thread
}
