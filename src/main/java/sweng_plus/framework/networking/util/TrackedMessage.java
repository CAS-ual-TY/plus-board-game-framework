package sweng_plus.framework.networking.util;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IMessageHandler;

public record TrackedMessage<M, C extends IClient>(M msg, IMessageHandler<M, C> handler, byte uMsgPosition)
{
    public int getPosition()
    {
        return Byte.toUnsignedInt(uMsgPosition);
    }
}
