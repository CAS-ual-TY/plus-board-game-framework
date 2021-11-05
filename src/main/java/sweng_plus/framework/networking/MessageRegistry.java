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
        int oldPos = writeBuffer.position();
        
        byte messageID = getIDForMessage(message);
        IMessageHandler<M> handler = (IMessageHandler<M>) this.handlers[messageID];
        writeBuffer.put(messageID);
        writeBuffer.putInt(0);
        encodeMessage(writeBuffer, message, handler);
        int newPos = writeBuffer.position();
        writeBuffer.putInt(oldPos, newPos - oldPos);
    }
    
    public <M> void encodeMessage(ByteBuffer writeBuffer, M message, IMessageHandler<M> handler)
    {
        handler.sendBytes(writeBuffer, message);
    }
    
    /**
     * @param readBuffer
     * @param <M>
     * @return Eine {@link Runnable}, welche beim Ausführen {@link IMessageHandler#handleMessage(M)} ausführt
     */
    public <M> Runnable decodeMessage(ByteBuffer readBuffer)
    {
        int size = readBuffer.getInt();
        byte messageID = readBuffer.get();
        IMessageHandler<M> handler = (IMessageHandler<M>) this.handlers[messageID];
        M msg = decodeMessage(readBuffer, handler);
        
        return () -> handler.handleMessage(msg);
    }
    
    public <M> M decodeMessage(ByteBuffer readBuffer, IMessageHandler<M> handler)
    {
        return handler.receiveBytes(readBuffer);
    }
    
    public <M> void decodeMessage(ByteBuffer readBuffer, MessageInfoConsumer<M> consumer)
    {
        int size = readBuffer.getInt();
        byte messageID = readBuffer.get();
        IMessageHandler<M> handler = (IMessageHandler<M>) this.handlers[messageID];
        M msg = decodeMessage(readBuffer, handler);
        consumer.accept(handler, msg);
    }
    
    public static interface MessageInfoConsumer<M>
    {
        void accept(IMessageHandler<M> handler, M msg);
    }
}
