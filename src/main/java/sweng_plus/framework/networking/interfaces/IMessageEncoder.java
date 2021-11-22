package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.CircularBuffer;

public interface IMessageEncoder<M>
{
    void encodeMessage(CircularBuffer buf, M msg);
}
