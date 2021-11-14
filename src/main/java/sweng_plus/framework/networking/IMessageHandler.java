package sweng_plus.framework.networking;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.ByteBuffer;

public interface IMessageHandler<M>
{
    M receiveBytes(CircularBuffer buf);
    
    void sendBytes(CircularBuffer buf, M msg);
    
    void handleMessage(M msg);
}
