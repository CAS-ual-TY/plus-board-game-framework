package sweng_plus.framework.networking.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

public class Buffer
{
    private final static int DEFAULT_SIZE = 1024;
    
    private final byte[] buffer;
    private final int size;
    private int numReads;
    private int numWrites;
    
    
    public Buffer(int size)
    {
        this.size = (size > 0) ? size : DEFAULT_SIZE;
        buffer = new byte[size];
        numReads = 0;
        numWrites = 0;
    }
    
    public Buffer()
    {
        this(DEFAULT_SIZE);
    }
    
    public void write(byte c)
    {
        validateWrite();
        
        buffer[numWrites % size] = c;
        numWrites++;
    }
    
    public byte read()
    {
        validateRead();
        
        byte read = buffer[numReads % size];
        numReads++;
        return read;
    }
    
    public boolean canWrite() {
        return numWrites >= numReads && (numWrites-numReads) < size;
    }
    
    public boolean canRead() {
        return numReads < numWrites;
    }
    
    private void validateWrite()
    {
        if(!canWrite()) {
            throw new BufferOverflowException();
        }
    }
    
    private void validateRead()
    {
        if(!canRead()) {
            throw new BufferUnderflowException();
        }
    }
}
