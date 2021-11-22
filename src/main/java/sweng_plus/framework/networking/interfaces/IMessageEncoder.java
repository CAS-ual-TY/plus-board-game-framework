package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.CircularBuffer;

@FunctionalInterface
public interface IMessageEncoder<M>
{
    /**
     * Encode a message into bytes.
     *
     * @param buf     The {@link CircularBuffer} the bytes need to be written to.
     * @param message The message to encode.
     */
    void encodeMessage(CircularBuffer buf, M message);
}
