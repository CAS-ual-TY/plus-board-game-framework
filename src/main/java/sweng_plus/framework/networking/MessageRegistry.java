package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IMessageHandler;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class MessageRegistry<C extends IClient> implements IMessageRegistry<C>
{
    private final IMessageHandler<?, C>[] handlers;
    private final Class<?>[] messageClasses; //TODO HashMap stattdessen vllt?
    
    public MessageRegistry(int messages)
    {
        if(messages > Byte.MAX_VALUE)
        {
            throw new IllegalArgumentException("Max 128 message types allowed");
        }
        
        handlers = new IMessageHandler[messages];
        messageClasses = new Class<?>[messages];
    }
    
    @Override
    public <M> MessageRegistry<C> registerMessage(byte id, IMessageHandler<M, C> handler, Class<M> messageClass)
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
    
    @Override
    public <M> IMessageHandler<M, C> getHandlerForMessage(M message)
    {
        return (IMessageHandler<M, C>) handlers[getIDForMessage(message)];
    }
    
    @Override
    public <M> void encodeMessage(CircularBuffer writeBuffer, M message, BiConsumer<M, IMessageHandler<M, C>> messageHandlerConsumer)
    {
        writeBuffer.startWriting();
        
        int oldPos = writeBuffer.getWriteIndex();
        short oldSize = (short) writeBuffer.size();
        writeBuffer.writeShort((short) 0);
        
        byte messageID = getIDForMessage(message);
        writeBuffer.writeByte(messageID);
        
        IMessageHandler<M, C> handler = (IMessageHandler<M, C>) handlers[messageID];
        handler.sendBytes(writeBuffer, message);
        
        writeBuffer.endWriting();
        
        short newSize = (short) writeBuffer.size();
        writeBuffer.setShort(oldPos, (short) (newSize - oldSize));
        
        messageHandlerConsumer.accept(message, handler);
    }
    
    @Override
    public <M> M decodeMessage(CircularBuffer readBuffer, BiConsumer<M, IMessageHandler<M, C>> messageHandlerConsumer)
    {
        readBuffer.startReading();
        
        short size = readBuffer.readShort();
        byte messageID = readBuffer.readByte();
        
        IMessageHandler<M, C> handler = (IMessageHandler<M, C>) handlers[messageID];
        M msg = handler.receiveBytes(readBuffer);
        
        readBuffer.endReading();
        
        messageHandlerConsumer.accept(msg, handler);
        
        return msg;
    }
}
