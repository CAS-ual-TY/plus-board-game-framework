package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

/**
 * Used as a client-server protocol.
 */
public interface IMessageRegistry<C extends IClient>
{
    /**
     * Registers a new message to the protocol.
     *
     * @param id           The ID of the message. Must be unique, no other message may use the same ID.
     * @param encoder      The {@link IMessageEncoder} of the message.
     * @param decoder      The {@link IMessageDecoder} of the message.
     * @param handler      The {@link IMessageHandler} of the message.
     * @param messageClass The class of the message.
     * @param <M>          The type of the message.
     * @return The {@link IMessageRegistry} itself.
     * @throws IllegalArgumentException In case the given ID is already in use.
     */
    <M> MessageRegistry<C> registerMessage(byte id, IMessageEncoder<M> encoder, IMessageDecoder<M> decoder, IMessageHandler<M, C> handler, Class<M> messageClass) throws IllegalArgumentException;
    
    /**
     * Encode a message and write it to the given {@link CircularBuffer}.
     *
     * @param writeBuffer            The {@link CircularBuffer} to write to.
     * @param message                The message to encode.
     * @param <M>                    The type of the message.
     * @param messageHandlerConsumer A way to access the involved {@link IMessageHandler} without having to
     *                               call the potentially costly {@link #getHandlerForMessage(M)}.
     */
    <M> void encodeMessage(CircularBuffer writeBuffer, M message, byte uMsgPosition, IMessageHandlerConsumer<M, C> messageHandlerConsumer);
    
    /**
     * Simplified version of {@link #encodeMessage(CircularBuffer, M, byte, IMessageHandlerConsumer)}.
     */
    default <M> void encodeMessage(CircularBuffer writeBuffer, M message, byte uMsgPosition)
    {
        encodeMessage(writeBuffer, message, uMsgPosition, (msg, uMsgPosition1, handler) -> {});
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
    <M> M decodeMessage(CircularBuffer readBuffer, IMessageHandlerConsumer<M, C> messageHandlerConsumer);
    
    /**
     * Simplified version of {@link #decodeMessage(CircularBuffer, IMessageHandlerConsumer)}.
     */
    default <M> M decodeMessage(CircularBuffer readBuffer)
    {
        return decodeMessage(readBuffer, (msg, uMsgPosition, handler) -> {});
    }
    
    /**
     * Utility method to obtain the {@link IMessageHandler} of a given message.
     *
     * @param message The message to get the {@link IMessageHandler} from.
     * @param <M>     The type of the message.
     * @return The {@link IMessageHandler} to be used for the given message.
     */
    <M> IMessageHandler<M, C> getHandlerForMessage(M message);
    
    /**
     * Checks a given {@link CircularBuffer} if it contains bytes which can be read and decoded into a message.
     * Its state is not changed by this process.
     *
     * @param buffer The {@link CircularBuffer} to check.
     * @return True if the given {@link CircularBuffer} contains bytes which can be read and decoded into a message.
     */
    boolean canDecodeMessage(CircularBuffer buffer);
    
    @FunctionalInterface
    interface IMessageHandlerConsumer<M, C extends IClient>
    {
        void accept(M msg, byte uMsgPosition, IMessageHandler<M, C> handler);
    }
}
