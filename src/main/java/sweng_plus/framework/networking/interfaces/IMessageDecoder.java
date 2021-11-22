package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.CircularBuffer;

public interface IMessageDecoder<M>
{
    M decodeMessage(CircularBuffer buf);
}
