package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;

import java.io.IOException;
import java.util.List;

public interface IHostManager extends IClientManager
{
    /**
     * Sends a message to the given {@link IClient} object. The message must be registered
     * in the used {@link MessageRegistry} (= protocol).
     *
     * @param client  The {@link IClient} object to send a message to.
     * @param message The message to be sent to the given client.
     * @param <M>     The class of the message.
     * @throws IOException
     */
    <M> void sendMessageToClient(IClient client, M message) throws IOException; // Main Thread
    
    /**
     * Sends a message to all {@link IClient} objects which are currently connected to the server.
     *
     * @param message The message to be sent to all clients.
     * @param <M>     The class of the message.
     * @throws IOException
     * @see #getAllClients()
     */
    <M> void sendMessageToAllClients(M message) throws IOException; // Main Thread
    
    /**
     * @return all clients represented as {@link IClient} objects which have ever connected to this server.
     * @see IClient#getStatus()
     */
    List<? extends IClient> getAllClients(); // Main Thread
    
    /**
     * @return The {@link IClient} object representing the host.
     */
    IClient getHostClient(); // Main Thread
}
