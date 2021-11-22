package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public interface IMessageHandler<M, C extends IClient>
{
    M decodeMessage(CircularBuffer buf);
    
    void encodeMessage(CircularBuffer buf, M msg);
    
    void handleMessage(Optional<C> clientOptional, M msg);
}
