package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedHostManager<C extends IAdvancedClient> extends IHostManager<C>, IAdvancedClientManager<C>
{
    /**
     * @return The used protocol. Must be the same on all clients and the server.
     */
    @Override
    IAdvancedMessageRegistry<C> getMessageRegistry();
    
    /**
     * Orderly kicks a client from the server (the client is notified about the kick)
     * and closes the connection to the client server-side.
     *
     * @param client The {@link C} instance representing the client to be kicked.
     */
    void kickClient(C client);
    
    /**
     * Orderly kicks a client from the server (the client is notified about the kick with a given message)
     * and closes the connection to the client server-side.
     *
     * @param client  The {@link C} instance representing the client to be kicked.
     * @param message The message which can represent the kick reason or kick justification.
     */
    default void kickClient(C client, String message)
    {
        kickClient(client);
    }
    
    /**
     * Called when the client answers the authentication request of the server.
     *
     * @param client     The {@link C} instance representing the client that authenticated.
     * @param name       The name of the client.
     * @param identifier The identifier of the client.
     */
    default void authenticate(C client, String name, UUID identifier)
    {
        client.setName(name.trim());
        client.setUUID(identifier);
    }
    
    /**
     * @return The server's session identifier which is sent to the clients for server/session authentication.
     */
    UUID getSessionIdentifier();
    
    @Override
    default void setSessionIdentifier(UUID sessionIdentifier) {}
    
    @Override
    default UUID getClientIdentifierForSession()
    {
        return getSessionIdentifier();
    }
}
