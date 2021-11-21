package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.function.BiConsumer;

/**
 * Used as a client-server protocol.
 */
public interface IMessageRegistry<C extends IClient>
{
    /**
     * Registers a new message to the protocol.
     *
     * @param id           The ID of the message. Must be unique, no other message may use the same ID.
     * @param handler      The {@link IMessageHandler} of the message.
     * @param messageClass The class of the message.
     * @param <M>          The type of the message.
     * @return The {@link IMessageRegistry} itself.
     * @throws IllegalArgumentException In case the given ID is already in use.
     */
    <M> MessageRegistry<C> registerMessage(byte id, IMessageHandler<M, C> handler, Class<M> messageClass) throws IllegalArgumentException;
    
    /**
     * Encode a message and write it to the given {@link CircularBuffer}.
     *
     * @param writeBuffer            The {@link CircularBuffer} to write to.
     * @param message                The message to encode.
     * @param <M>                    The type of the message.
     * @param messageHandlerConsumer A way to access the involved {@link IMessageHandler} without having to
     *                               call the potentially costly {@link #getHandlerForMessage(M)}.
     */
    <M> void encodeMessage(CircularBuffer writeBuffer, M message, BiConsumer<M, IMessageHandler<M, C>> messageHandlerConsumer);
    
    /**
     * Simplified version of {@link #encodeMessage(CircularBuffer, Object, BiConsumer)}.
     */
    default <M> void encodeMessage(CircularBuffer writeBuffer, M message)
    {
        encodeMessage(writeBuffer, message, (msg, handler) -> {});
    }
    
    /**
     * Decodes a message by reading from the given {@link CircularBuffer} and returns it.
     *
     * @param readBuffer             The {@link CircularBuffer} to read from.
     * @param <M>                    The type of the message.
     * @param messageHandlerConsumer A way to access the involved {@link IMessageHandler} without having to
     *                               call the potentially costly {@link #getHandlerForMessage(M)}.
     * @return The message.
     */
    <M> M decodeMessage(CircularBuffer readBuffer, BiConsumer<M, IMessageHandler<M, C>> messageHandlerConsumer);
    
    /**
     * Simplified version of {@link #decodeMessage(CircularBuffer, BiConsumer)}.
     */
    default <M> M decodeMessage(CircularBuffer readBuffer)
    {
        return decodeMessage(readBuffer, (msg, handler) -> {});
    }
    
    /**
     * Utility method to obtain the {@link IMessageHandler} of a given message.
     *
     * @param message The message to get the {@link IMessageHandler} from.
     * @param <M>     The type of the message.
     * @return The {@link IMessageHandler} to be used for the given message.
     */
    <M> IMessageHandler<M, C> getHandlerForMessage(M message);
}
