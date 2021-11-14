package sweng_plus.framework.networking.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CircularBuffer
{
    private static final int DEFAULT_SIZE = 1024;
    
    private final byte[] buffer;
    private final int capacity;
    
    private int writeIndex;
    private int readIndex;
    
    private int size;
    private int tempCounter;
    
    public CircularBuffer(int size)
    {
        capacity = (size > 0) ? size : DEFAULT_SIZE;
        buffer = new byte[size];
        
        writeIndex = 0;
        readIndex = 0;
        
        size = 0;
    }
    
    public CircularBuffer()
    {
        this(DEFAULT_SIZE);
    }
    
    public int getCapacity()
    {
        return capacity;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public void writeByte(byte b)
    {
        buffer[writeIndex] = b;
        writeIndex = ++writeIndex % capacity;
        size++;
    }
    
    public void writeShort(short i)
    {
        writeByte((byte) ((i >> 0x08) & 0xFF));
        writeByte((byte) (i & 0xFF));
    }
    
    public void writeInt(int i)
    {
        writeShort((short) ((i >> 0x10) & 0xFFFF));
        writeShort((short) (i & 0xFFFF));
    }
    
    public void writeLong(long i)
    {
        writeInt((int) ((i >> 0x20)));
        writeInt((int) (i));
    }
    
    public void writeChar(char c, Charset charset)
    {
        writeString(String.valueOf(c), charset);
    }
    
    public void writeString(String s, Charset charset)
    {
        byte[] bs = s.getBytes(charset);
        writeShort((short) bs.length);
        writeBytes(bs);
    }
    
    public void writeBytes(byte[] arr)
    {
        for(byte b : arr)
        {
            writeByte(b);
        }
    }
    
    public <T> void writeList(List<T> list, BiConsumer<CircularBuffer, T> encoder)
    {
        writeShort((short) list.size());
        for(T t : list)
        {
            encoder.accept(this, t);
        }
    }
    
    public byte readByte()
    {
        byte read = buffer[readIndex];
        readIndex = ++readIndex % capacity;
        size--;
        return read;
    }
    
    public short readShort()
    {
        return (short) ((readByte() << 0x08) |
                (readByte()));
    }
    
    public int readInt()
    {
        return (readShort() << 0x10) |
                (readShort());
    }
    
    public long readLong()
    {
        return ((long) readInt() << 0x20) |
                (readInt());
    }
    
    public char readChar(Charset charset)
    {
        return readString(charset).charAt(0);
    }
    
    public String readString(Charset charset)
    {
        short length = readShort();
        return new String(readBytes(length), charset);
    }
    
    public byte[] readBytes(short numBytes)
    {
        byte[] read = new byte[numBytes];
        
        for(int i = 0; i < numBytes; i++)
        {
            read[i] = readByte();
        }
        return read;
    }
    
    public <T> List<T> readList(Function<CircularBuffer, T> decoder)
    {
        short length = readShort();
        
        List<T> read = new LinkedList<>();
        for(int i = 0; i < length; i++)
        {
            read.add(decoder.apply(this));
        }
        return read;
    }
    
    public void startWriting()
    {
        tempCounter = size;
    }
    
    public void endWriting()
    {
        if(size > capacity)
        {
            throw new BufferOverflowException();
        }
    }
    
    public void startReading()
    {
        tempCounter = size;
    }
    
    public void endReading()
    {
        if(size < 0)
        {
            throw new BufferUnderflowException();
        }
    }
}
