package sweng_plus.framework.networking;

import java.nio.ByteBuffer;

public class MessageRegistry
{
    private final IMessageHandler<?>[] handlers;
    private final Class<?>[] messageClasses; //TODO HashMap stattdessen vllt?
    
    public MessageRegistry(int messages)
    {
        if(messages > Byte.MAX_VALUE)
        {
            throw new IllegalArgumentException("Max 128 message types allowed");
        }
        
        this.handlers = new IMessageHandler<?>[messages];
        this.messageClasses = new Class<?>[messages];
    }
    
    public <M> MessageRegistry registerMessage(byte id, IMessageHandler<M> handler, Class<M> messageClass)
    {
        if(id < 0 || id >= handlers.length || this.handlers[id] != null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            this.handlers[id] = handler;
            this.messageClasses[id] = messageClass;
        }
        
        return this;
    }
    
    public <M> byte getIDForMessage(M message)
    {
        Class<M> c = (Class<M>) message.getClass();
        
        for(byte i = 0; i < messageClasses.length; ++i)
        {
            if(c == messageClasses[i])
            {
                return i;
            }
        }
        
        throw new IllegalArgumentException("Message not registered");
    }
    
    public <M> void encodeMessage(ByteBuffer writeBuffer, M message)
    {
        byte messageID = getIDForMessage(message);
        IMessageHandler<M> handler = (IMessageHandler<M>) this.handlers[messageID];
        writeBuffer.put(messageID);
        handler.sendBytes(writeBuffer, message);
    }
    
    /**
     * @param readBuffer
     * @param <M>
     * @return Eine {@link Runnable}, welche beim Ausführen {@link IMessageHandler#handleMessage(M)} ausführt
     */
    public <M> Runnable decodeMessage(ByteBuffer readBuffer)
    {
        byte messageID = readBuffer.get();
        IMessageHandler<M> handler = (IMessageHandler<M>) this.handlers[messageID];
        M message = handler.receiveBytes(readBuffer);
        return () -> handler.handleMessage(message);
    }
}