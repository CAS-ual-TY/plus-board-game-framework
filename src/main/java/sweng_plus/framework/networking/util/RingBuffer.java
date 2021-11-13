package sweng_plus.framework.networking.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

public class RingBuffer
{
    private final static int DEFAULT_SIZE = 1024;
    
    private final byte[] buffer;
    private final int size;
    private int numReads;
    private int numWrites;
    
    private int startNumReads;
    private int startNumWrites;
    
    
    public RingBuffer(int size)
    {
        this.size = (size > 0) ? size : DEFAULT_SIZE;
        buffer = new byte[size];
        numReads = 0;
        startNumReads = 0;
        
        numWrites = 0;
        startNumWrites = 0;
    }
    
    public RingBuffer()
    {
        this(DEFAULT_SIZE);
    }
    
    public void write(byte b)
    {
        buffer[numWrites % size] = b;
        numWrites++;
    }
    
    public void writeInt(int i) {
        write((byte)((i >> 24) & 0xFF));
        write((byte)((i >> 16) & 0xFF));
        write((byte)((i >> 8) & 0xFF));
        write((byte)((i) & 0xFF));
    }
    
    public void writeLong(long i) {
        write((byte)((i >> 56) & 0xFF));
        write((byte)((i >> 48) & 0xFF));
        write((byte)((i >> 40) & 0xFF));
        write((byte)((i >> 32) & 0xFF));
        write((byte)((i >> 24) & 0xFF));
        write((byte)((i >> 16) & 0xFF));
        write((byte)((i >> 8) & 0xFF));
        write((byte)((i) & 0xFF));
    }
    
    public void writeChar(char c, Charset charset) {
        writeString(String.valueOf(c), charset);
    }
    
    public void writeString(String s, Charset charset) {
        writeInt(s.getBytes(charset).length);
        for(byte b : s.getBytes(charset)) {
            write(b);
        }
    }
    
    public byte read()
    {
        byte read = buffer[numReads % size];
        numReads++;
        return read;
    }
    
    public int readInt()
    {
        return  ((read() & 0xFF) << 24) |
                ((read() & 0xFF) << 16) |
                ((read() & 0xFF) << 8 ) |
                ((read() & 0xFF) << 0 );
    }
    
    public long readLong()
    {
        return ((long) (read() & 0xFF) << 56) |
               ((long) (read() & 0xFF) << 48) |
               ((long) (read() & 0xFF) << 40) |
               ((long) (read() & 0xFF) << 32) |
               ((long) (read() & 0xFF) << 24) |
               ((long) (read() & 0xFF) << 16) |
               ((long) (read() & 0xFF) << 8 ) |
               ((long) (read() & 0xFF) << 0 );
    }
    
    public char readChar(Charset charset)
    {
        return readString(charset).charAt(0);
    }
    
    public String readString(Charset charset)
    {
        int length = readInt();
        byte[] readBytes = new byte[length];
        for(int i = 0 ; i < length; i++) {
            readBytes[i] = read();
        }
        
        return new String(readBytes, charset);
    }
    
    public void startWriting()
    {
        startNumWrites = numWrites;
        if(numWrites < numReads || (numWrites-numReads) >= size) {
            throw new BufferOverflowException();
        }
    }
    public void endWriting()
    {
        if(numWrites - startNumWrites > size || (numWrites-numReads) > size) {
            throw new BufferOverflowException();
        } else {
            int reduceCountersBy = Math.floorDiv(numReads,size)*size;
            numWrites -= reduceCountersBy;
            numReads  -= reduceCountersBy;
        }
    }
    
    public void startReading()
    {
        startNumReads = numReads;
        if(numReads >= numWrites) {
            throw new BufferUnderflowException();
        }
    }
    public void endReading()
    {
        if(numReads > numWrites) {
            throw new BufferUnderflowException();
        } else {
            int reduceCountersBy = Math.floorDiv(numReads,size)*size;
            numWrites -= reduceCountersBy;
            numReads  -= reduceCountersBy;
        }
    }
}
