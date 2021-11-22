package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.CircularBuffer;

@FunctionalInterface
public interface IMessageDecoder<M>
{
    /**
     * Decode bytes into a message.
     *
     * @param buf The {@link CircularBuffer} the bytes need to be read from.
     * @return The decoded message.
     */
    M decodeMessage(CircularBuffer buf);
}
