package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

/**
 * Used as a client-server protocol.
 */
public interface IMessageRegistry
{
    /**
     * Registers a new message to the protocol.
     * @param id The ID of the message. Must be unique, no other message may use the same ID.
     * @param handler The {@link IMessageHandler} of the message.
     * @param messageClass The class of the message.
     * @param <M> The type of the message.
     * @return The {@link IMessageRegistry} itself.
     * @throws IllegalArgumentException In case the given ID is already in use.
     */
    <M> MessageRegistry registerMessage(byte id, IMessageHandler<M> handler, Class<M> messageClass) throws IllegalArgumentException;
    
    /**
     * Encode a message and write it to the given {@link CircularBuffer}.
     * @param writeBuffer The {@link CircularBuffer} to write to.
     * @param message The message to encode.
     * @param <M> The type of the message.
     */
    <M> void encodeMessage(CircularBuffer writeBuffer, M message);
    
    /**
     * Decodes a message by reading from the given {@link CircularBuffer} and returns a to-be-executed {@link Runnable}
     * representing a {@link IMessageHandler#handleMessage(M)} call.
     * @param readBuffer The {@link CircularBuffer} to read from.
     * @param <M> The type of the message.
     * @return A {@link Runnable} representing a {@link IMessageHandler#handleMessage(M)} call.
     */
    <M> Runnable decodeMessage(CircularBuffer readBuffer);
    
    /**
     * Utility method to obtain the {@link IMessageHandler} of a given message.
     * @param message The message to get the {@link IMessageHandler} from.
     * @param <M> The type of the message.
     * @return The {@link IMessageHandler} to be used for the given message.
     */
    <M> IMessageHandler<M> getHandlerForMessage(M message);
}
