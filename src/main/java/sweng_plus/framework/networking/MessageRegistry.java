package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IMessageHandler;
import sweng_plus.framework.networking.util.CircularBuffer;

@SuppressWarnings("unchecked")
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
        
        handlers = new IMessageHandler<?>[messages];
        messageClasses = new Class<?>[messages];
    }
    
    public <M> MessageRegistry registerMessage(byte id, IMessageHandler<M> handler, Class<M> messageClass)
    {
        if(id < 0 || id >= handlers.length || handlers[id] != null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            handlers[id] = handler;
            messageClasses[id] = messageClass;
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
    
    public <M> IMessageHandler<M> getHandlerForMessage(M message)
    {
        return (IMessageHandler<M>) handlers[getIDForMessage(message)];
    }
    
    public <M> void encodeMessage(CircularBuffer writeBuffer, M message)
    {
        writeBuffer.startWriting();
        
        int oldPos = writeBuffer.getWriteIndex();
        short oldSize = (short) writeBuffer.size();
        writeBuffer.writeShort((short) 0);
        
        byte messageID = getIDForMessage(message);
        writeBuffer.writeByte(messageID);
        
        IMessageHandler<M> handler = (IMessageHandler<M>) handlers[messageID];
        encodeMessage(writeBuffer, message, handler);
        
        writeBuffer.endWriting();
        
        short newSize = (short) writeBuffer.size();
        writeBuffer.setShort(oldPos, (short) (newSize - oldSize));
    }
    
    public <M> void encodeMessage(CircularBuffer writeBuffer, M message, IMessageHandler<M> handler)
    {
        handler.sendBytes(writeBuffer, message);
    }
    
    /**
     * @param readBuffer
     * @param <M>
     * @return Eine {@link Runnable}, welche beim Ausführen {@link IMessageHandler#handleMessage(M)} ausführt
     */
    public <M> Runnable decodeMessage(CircularBuffer readBuffer)
    {
        readBuffer.startReading();
        
        short size = readBuffer.readShort();
        byte messageID = readBuffer.readByte();
        
        IMessageHandler<M> handler = (IMessageHandler<M>) handlers[messageID];
        M msg = decodeMessage(readBuffer, handler);
        
        readBuffer.endReading();
        
        return () -> handler.handleMessage(msg);
    }
    
    public <M> M decodeMessage(CircularBuffer readBuffer, IMessageHandler<M> handler)
    {
        return handler.receiveBytes(readBuffer);
    }
    
    public <M> void decodeMessage(CircularBuffer readBuffer, MessageInfoConsumer<M> consumer)
    {
        short size = readBuffer.readShort();
        byte messageID = readBuffer.readByte();
        IMessageHandler<M> handler = (IMessageHandler<M>) handlers[messageID];
        M msg = decodeMessage(readBuffer, handler);
        consumer.accept(handler, msg);
    }
    
    public <M> void runMessage(M message)
    {
    
    }
    
    public interface MessageInfoConsumer<M>
    {
        void accept(IMessageHandler<M> handler, M msg);
    }
}
