package sweng_plus.framework.networking;

import sweng_plus.framework.networking.util.CircularBuffer;

public interface IMessageHandler<M>
{
    M receiveBytes(CircularBuffer buf);
    
    void sendBytes(CircularBuffer buf, M msg);
    
    void handleMessage(M msg);
}
