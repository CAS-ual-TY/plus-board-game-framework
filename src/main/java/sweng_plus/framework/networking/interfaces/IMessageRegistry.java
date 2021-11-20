package sweng_plus.framework.networking.interfaces;

import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

public interface IMessageRegistry
{
    <M> MessageRegistry registerMessage(byte id, IMessageHandler<M> handler, Class<M> messageClass);
    
    <M> void encodeMessage(CircularBuffer writeBuffer, M message);
    
    <M> Runnable decodeMessage(CircularBuffer readBuffer);
    
    <M> IMessageHandler<M> getHandlerForMessage(M message);
}
