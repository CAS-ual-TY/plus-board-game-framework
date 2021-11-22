package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

@FunctionalInterface
public interface IMessageHandler<M, C extends IClient>
{
    /**
     * This method will be called from the main thread. Decoded messages should interact with any game logic
     * using this method (and not immediately in {@link IMessageDecoder#decodeMessage(CircularBuffer)}.
     *
     * @param clientOptional On client-side this {@link Optional} will be empty, on server-side it will contain
     *                       the client the message was sent from.
     * @param message        The decoded message which should be used to interact with any game logic now.
     */
    void handleMessage(Optional<C> clientOptional, M message);
}
