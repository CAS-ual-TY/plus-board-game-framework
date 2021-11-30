package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class MessageRegistry<C extends IClient> implements IMessageRegistry<C>
{
    private final IMessageEncoder<?>[] encoders;
    private final IMessageDecoder<?>[] decoders;
    private final IMessageHandler<?, C>[] handlers;
    private final Class<?>[] messageClasses; //TODO HashMap stattdessen vllt?
    
    public MessageRegistry(int messages)
    {
        if(messages > Byte.MAX_VALUE)
        {
            throw new IllegalArgumentException("Max 128 message types allowed");
        }
        
        encoders = new IMessageEncoder[messages];
        decoders = new IMessageDecoder[messages];
        handlers = new IMessageHandler[messages];
        messageClasses = new Class<?>[messages];
    }
    
    @Override
    public <M> MessageRegistry<C> registerMessage(byte id, IMessageEncoder<M> encoder, IMessageDecoder<M> decoder, IMessageHandler<M, C> handler, Class<M> messageClass)
    {
        if(id < 0 || id >= handlers.length || handlers[id] != null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            encoders[id] = encoder;
            decoders[id] = decoder;
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
        
        throw new IllegalArgumentException("Message not registered: " + message.getClass());
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
        
        IMessageEncoder<M> encoder = (IMessageEncoder<M>) encoders[messageID];
        encoder.encodeMessage(writeBuffer, message);
        
        writeBuffer.endWriting();
        
        short newSize = (short) writeBuffer.size();
        writeBuffer.setShort(oldPos, (short) (newSize - oldSize));
        
        messageHandlerConsumer.accept(message, (IMessageHandler<M, C>) handlers[messageID]);
    }
    
    @Override
    public <M> M decodeMessage(CircularBuffer readBuffer, BiConsumer<M, IMessageHandler<M, C>> messageHandlerConsumer)
    {
        readBuffer.startReading();
        
        short size = readBuffer.readShort();
        byte messageID = readBuffer.readByte();
        
        IMessageDecoder<M> decoder = (IMessageDecoder<M>) decoders[messageID];
        M message = decoder.decodeMessage(readBuffer);
        
        readBuffer.endReading();
        
        messageHandlerConsumer.accept(message, (IMessageHandler<M, C>) handlers[messageID]);
        
        return message;
    }
    
    @Override
    public boolean canDecodeMessage(CircularBuffer buffer)
    {
        return buffer.size() > Short.BYTES && buffer.size() >= buffer.peekShort();
    }
}
