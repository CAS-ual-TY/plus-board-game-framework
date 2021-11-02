package sweng_plus.framework.networking;

import java.nio.ByteBuffer;

public interface IMessageHandler<M>
{
    M receiveBytes(ByteBuffer buf);
    
    void sendBytes(ByteBuffer buf, M msg);
    
    void handleMessage(M msg);
}
