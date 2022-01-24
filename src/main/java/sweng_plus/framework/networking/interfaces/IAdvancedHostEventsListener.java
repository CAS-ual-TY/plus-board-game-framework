package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedHostEventsListener<C extends IAdvancedClient> extends IHostEventsListener<C>, IAdvancedClientEventsListener
{
    @Override
    default void lostConnection() {}
    
    @Override
    default void clientConnected(C client) {}
    
    /**
     * Called after a client has disconnected orderly
     * (it notified the server about the disconnect).
     * Their connection is about to be or has already been closed.
     *
     * @param client The {@link C} instance representing the client that has disconnected.
     */
    void clientDisconnectedOrderly(C client);
    
    /**
     * Called after a client has disconnected due to an exception
     * (it notified the server about the disconnect due to exception).
     * Their connection is about to be or has already been closed.
     *
     * @param client The {@link C} instance representing the client that has disconnected.
     */
    default void clientDisconnectedDueToException(C client)
    {
        clientDisconnectedOrderly(client);
    }
    
    /**
     * Called if the connection to a client has been lost (connection timed out).
     *
     * @param client The {@link C} instance representing the client that the connection has been lost to.
     */
    default void clientLostConnection(C client)
    {
        clientDisconnectedOrderly(client);
    }
    
    /**
     * Called after a client has successfully authenticated and connected for the first time.
     * This means that the client's sent identifier and name
     * did pass the predicate defined by {@link #authPredicate(UUID, String)}.
     *
     * @param client The {@link C} instance representing the client that has successfully authenticated.
     */
    void clientAuthSuccessful(C client);
    
    /**
     * Called after a client has successfully authenticated, and it is revealed that the identifier
     * of this client was in use by another client before that has disconnected earlier.
     *
     * @param oldClient The {@link C} instance representing the client that has disconnected earlier.
     * @param newClient The {@link C} instance representing the client that has successfully authenticated.
     */
    default void clientReconnected(C oldClient, C newClient)
    {
        newClient.setName(oldClient.getName());
    }
    
    /**
     * Called after a client has unsuccessfully authenticated.
     * This means that the client's sent identifier and name
     * did not pass the predicate defined by {@link #authPredicate(UUID, String)}.
     * They were already or are about to be kicked.
     *
     * @param client The {@link C} instance representing the client that has unsuccessfully authenticated.
     */
    default void clientAuthUnsuccessful(C client) {}
    
    /**
     * The authentication predicate used in
     * {@link #clientAuthSuccessful(IAdvancedClient)} and {@link #clientAuthUnsuccessful(IAdvancedClient)}.
     *
     * @return <t>true</t> if the given identifier and name are accepted,
     * <t>false</t> if they are not accepted and the client is to be kicked.
     */
    default boolean authPredicate(UUID identifier, String name)
    {
        return name.length() >= 3;
    }
}
