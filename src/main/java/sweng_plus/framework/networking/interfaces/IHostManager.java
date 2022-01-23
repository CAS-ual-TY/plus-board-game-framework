package sweng_plus.framework.networking.interfaces;

import java.io.IOException;
import java.util.List;

public interface IHostManager<C extends IClient> extends IClientManager
{
    /**
     * Sends a message to the given {@link IClient}. The message must be registered
     * in the used {@link IMessageRegistry} (= protocol).
     *
     * @param client  The {@link IClient} to send a message to.
     * @param message The message to be sent to the given client.
     * @param <M>     The type of the message.
     * @throws IOException
     * @see #sendMessageToClient(IClient, Object)
     */
    <M> void sendMessageToClientUnsafe(C client, M message) throws IOException; // Main Thread
    
    /**
     * <p>Sends a message to the given {@link IClient}. The message must be registered
     * in the used {@link IMessageRegistry} (= protocol).</p>
     *
     * <p>If an {@link IOException} is thrown, it is caught and then {@link #failedToSendMessageToClient(IClient, IOException)} is run.</p>
     *
     * @param client  The {@link IClient} to send a message to.
     * @param message The message to be sent to the given client.
     * @param <M>     The type of the message.
     * @throws IOException
     * @see #sendMessageToClient(IClient, Object)
     */
    default <M> void sendMessageToClient(C client, M message) // Main Thread
    {
        try
        {
            sendMessageToClientUnsafe(client, message);
        }
        catch(IOException e)
        {
            failedToSendMessageToClient(client, e);
        }
    }
    
    /**
     * <p>Sends a message to all {@link IClient}s which are currently connected to the server.</p>
     *
     * <p>If an {@link IOException} is thrown, it is caught and then {@link #failedToSendMessageToClient(IClient, IOException)} is run.
     * This is done for each client individually, meaning that it may fail for a single client, but work for all others.</p>
     *
     * @param message The message to be sent to all clients.
     * @param <M>     The type of the message.
     * @throws IOException
     */
    default <M> void sendMessageToAllClients(M message) // Main Thread
    {
        for(C c : getAllClients())
        {
            sendMessageToClient(c, message);
        }
    }
    
    /**
     * <p>Sends a message to all {@link IClient}s which are currently connected to the server except a given one.</p>
     *
     * <p>If an {@link IOException} is thrown, it is caught and then {@link #failedToSendMessageToClient(IClient, IOException)} is run.
     * This is done for each client individually, meaning that it may fail for a single client, but work for all others.</p>
     *
     * @param client  The client to not send a message to.
     * @param message The message to be sent to all clients except the given one.
     * @param <M>     The type of the message.
     * @throws IOException
     * @see #sendMessageToAllClients(Object)
     * @see #sendMessageToAllClientsExceptHost(Object)
     */
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
    
    /**
     * <p>Sends a message to all {@link IClient}s which are currently connected to the server except the host-client.</p>
     *
     * <p>If an {@link IOException} is thrown, it is caught and then {@link #failedToSendMessageToClient(IClient, IOException)} is run.
     * This is done for each client individually, meaning that it may fail for a single client, but work for all others.</p>
     *
     * @param message The message to be sent to all clients except the given one.
     * @param <M>     The type of the message.
     * @throws IOException
     * @see #sendMessageToAllClients(Object)
     * @see #sendMessageToAllClientsExcept(IClient, Object)
     */
    default <M> void sendMessageToAllClientsExceptHost(M message) // Main Thread
    {
        sendMessageToAllClientsExcept(getHostClient(), message);
    }
    
    /**
     * Closes the connection to a given client server-side and closes the socket and all associated resources and threads.
     *
     * @param client The client to close the connection of.
     */
    void closeClient(C client);
    
    /**
     * Called by {@link #sendMessageToClient(IClient, Object)}.
     *
     * @param e The thrown exception after failing to send a message.
     */
    default void failedToSendMessageToClient(C client, IOException e)
    {
        closeClient(client);
    }
    
    /**
     * @return all clients represented as {@link IClient} objects which are connected to this server.
     */
    List<C> getAllClients(); // Main Thread
    
    /**
     * @return The {@link IClient} representing the host.
     */
    C getHostClient(); // Main Thread
    
    /**
     * Closes all connections server-side and all associated resources and threads.
     */
    @Override
    void close();
}
