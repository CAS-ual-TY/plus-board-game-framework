package sweng_plus.framework.networking.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

public class CircularBuffer
{
    private static final int DEFAULT_SIZE = 1024;
    
    private final byte[] buffer;
    private final int capacity;
    private int numReads;
    private int numWrites;
    
    private int startNumReads;
    private int startNumWrites;
    
    public CircularBuffer(int size)
    {
        capacity = (size > 0) ? size : DEFAULT_SIZE;
        buffer = new byte[size];
        numReads = 0;
        startNumReads = 0;
        
        numWrites = 0;
        startNumWrites = 0;
    }
    
    public CircularBuffer()
    {
        this(DEFAULT_SIZE);
    }
    
    public void writeByte(byte b)
    {
        buffer[numWrites % capacity] = b;
        numWrites++;
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
    
    public byte readByte()
    {
        byte read = buffer[numReads % capacity];
        numReads++;
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
        int length = readShort();
        return new String(readBytes(length), charset);
    }
    
    public byte[] readBytes(int numBytes)
    {
        byte[] read = new byte[numBytes];
        
        for(int i = 0; i < numBytes; i++)
        {
            read[i] = readByte();
        }
        return read;
    }
    
    public void startWriting()
    {
        startNumWrites = numWrites;
        if(numWrites < numReads || (numWrites - numReads) >= capacity)
        {
            throw new BufferOverflowException();
        }
    }
    
    public void endWriting()
    {
        if(numWrites - startNumWrites > capacity || (numWrites - numReads) > capacity)
        {
            throw new BufferOverflowException();
        }
        else
        {
            int reduceCountersBy = Math.floorDiv(numReads, capacity) * capacity;
            numWrites -= reduceCountersBy;
            numReads -= reduceCountersBy;
        }
    }
    
    public void startReading()
    {
        startNumReads = numReads;
        if(numReads >= numWrites)
        {
            throw new BufferUnderflowException();
        }
    }
    
    public void endReading()
    {
        if(numReads > numWrites)
        {
            throw new BufferUnderflowException();
        }
        else
        {
            int reduceCountersBy = Math.floorDiv(numReads, capacity) * capacity;
            numWrites -= reduceCountersBy;
            numReads -= reduceCountersBy;
        }
    }
}
