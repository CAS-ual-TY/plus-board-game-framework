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
    
    public void write(byte b)
    {
        buffer[numWrites % capacity] = b;
        numWrites++;
    }
    
    public void writeInt(int i)
    {
        write((byte) ((i >> 0x18) & 0xFF));
        write((byte) ((i >> 0x10) & 0xFF));
        write((byte) ((i >> 0x08) & 0xFF));
        write((byte) (i & 0xFF));
    }
    
    public void writeLong(long i)
    {
        write((byte) ((i >> 0x38) & 0xFF));
        write((byte) ((i >> 0x30) & 0xFF));
        write((byte) ((i >> 0x28) & 0xFF));
        write((byte) ((i >> 0x20) & 0xFF));
        write((byte) ((i >> 0x18) & 0xFF));
        write((byte) ((i >> 0x10) & 0xFF));
        write((byte) ((i >> 0x08) & 0xFF));
        write((byte) (i & 0xFF));
    }
    
    public void writeChar(char c, Charset charset)
    {
        writeString(String.valueOf(c), charset);
    }
    
    public void writeString(String s, Charset charset)
    {
        byte[] bs = s.getBytes(charset);
        writeInt(bs.length);
        for(byte b : bs)
        {
            write(b);
        }
    }
    
    public byte read()
    {
        byte read = buffer[numReads % capacity];
        numReads++;
        return read;
    }
    
    public int readInt()
    {
        return (read() << 0x18) |
                (read() << 0x10) |
                (read() << 0x08) |
                (read());
    }
    
    public long readLong()
    {
        return ((long) read() << 0x38) |
                ((long) read() << 0x30) |
                ((long) read() << 0x28) |
                ((long) read() << 0x20) |
                (read() << 0x18) |
                (read() << 0x10) |
                (read() << 0x08) |
                (read());
    }
    
    public char readChar(Charset charset)
    {
        return readString(charset).charAt(0);
    }
    
    public String readString(Charset charset)
    {
        int length = readInt();
        byte[] readBytes = new byte[length];
        for(int i = 0; i < length; i++)
        {
            readBytes[i] = read();
        }
        
        return new String(readBytes, charset);
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
