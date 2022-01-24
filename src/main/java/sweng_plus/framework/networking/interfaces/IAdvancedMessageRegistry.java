package sweng_plus.framework.networking.interfaces;

public interface IAdvancedMessageRegistry<C extends IClient> extends IMessageRegistry<C>
{
    /**
     * @return A new "request ping" message instance that can be sent to request a "respond to ping" message.
     * @see #respondToPing()
     */
    <M> M requestPing();
    
    /**
     * @return A new "respond to ping" message instance that can be sent in response to a "request ping" message.
     * @see #requestPing()
     */
    <M> M respondToPing();
    
    /**
     * @return A new "force disconnect client" message instance that can be sent to a client
     * to notify them that they are about to be forcibly disconnected.
     */
    <M> M forceDisconnectClient();
    
    /**
     * @return A new "server closed" message instance that can be sent to a client
     * to notify them that the server is about to be closed and that they are about to be forcibly disconnected.
     */
    <M> M serverClosed();
    
    /**
     * @return A new "kick client" message instance that can be sent to a client
     * to notify them that they are about to be kicked without a reason or justification message supplied.
     */
    <M> M kickClient();
    
    /**
     * @return A new "kick client" message instance that can be sent to a client
     * to notify them that they are about to be kicked for a reason or justification supplied as a message.
     */
    <M> M kickClient(String message);
    
    /**
     * @return A new "orderly disconnect" message instance that can be sent to the server
     * to notify it that the client is about to disconnect.
     */
    <M> M orderlyDisconnected();
    
    /**
     * @return A new "disconnected due to exception" message instance that can be sent to the server
     * to notify it that the client is about to disconnect due to an error.
     */
    <M> M disconnectedDueToException();
    
    /**
     * @return A new "auth request" message instance that can be sent to a client
     * to authenticate the server to the client and to request an "auth response" message from the client.
     */
    <M> M authRequest();
    
    /**
     * @return A new "auth response" message instance that can be sent to the server in response to an "auth request" message.
     * to authenticate the client to the server including identifier and client name.
     */
    <M> M authResponse();
}
