package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;

import java.io.IOException;
import java.util.List;

public interface IHostManager<C extends IClient> extends IClientManager
{
    /**
     * Sends a message to the given {@link IClient} object. The message must be registered
     * in the used {@link MessageRegistry} (= protocol).
     *
     * @param client  The {@link IClient} object to send a message to.
     * @param message The message to be sent to the given client.
     * @param <M>     The type of the message.
     * @throws IOException
     */
    <M> void sendMessageToClientUnsafe(C client, M message) throws IOException; // Main Thread
    
    default <M> void sendMessageToClient(C client, M message) // Main Thread
    {
        try
        {
            sendMessageToClientUnsafe(client, message);
        }
        catch(IOException e)
        {
            failedToSendMessageToClient(client);
        }
    }
    
    /**
     * Sends a message to all {@link IClient} objects which are currently connected to the server.
     *
     * @param message The message to be sent to all clients.
     * @param <M>     The type of the message.
     * @throws IOException
     * @see #getAllClients()
     */
    default <M> void sendMessageToAllClients(M message) // Main Thread
    {
        for(C c : getAllClients())
        {
            sendMessageToClient(c, message);
        }
    }
    
    default <M> void sendMessageToAllClientsExcept(C client, M message) // Main Thread
    {
        for(C c : getAllClients())
        {
            if(c != client)
            {
                sendMessageToClient(c, message);
            }
        }
    }
    
    default <M> void sendMessageToAllClientsExceptHost(M message) // Main Thread
    {
        sendMessageToAllClientsExcept(getHostClient(), message);
    }
    
    void closeClient(C client);
    
    default void failedToSendMessageToClient(C client)
    {
        closeClient(client);
    }
    
    /**
     * @return all clients represented as {@link IClient} objects which have ever connected to this server.
     * @see IClient#getStatus()
     */
    List<C> getAllClients(); // Main Thread
    
    /**
     * @return The {@link IClient} object representing the host.
     */
    C getHostClient(); // Main Thread
}
