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
    
    private int startNumReads;
    private int startNumWrites;
    
    
    public Buffer(int size)
    {
        this.size = (size > 0) ? size : DEFAULT_SIZE;
        buffer = new byte[size];
        numReads = 0;
        startNumReads = 0;
        
        numWrites = 0;
        startNumWrites = 0;
    }
    
    public Buffer()
    {
        this(DEFAULT_SIZE);
    }
    
    public void write(byte c)
    {
        buffer[numWrites % size] = c;
        numWrites++;
    }
    
    public byte read()
    {
        byte read = buffer[numReads % size];
        numReads++;
        return read;
    }
    
    public boolean canWrite() {
        return numWrites >= numReads && (numWrites-numReads) < size;
    }
    public void startWriting()
    {
        "".b
        startNumWrites = numWrites;
        if(numWrites < numReads || (numWrites-numReads) >= size) {
            throw new BufferOverflowException();
        }
    }
    public void endWriting()
    {
        if(numWrites - startNumWrites > size || (numWrites-numReads) > size) {
            throw new BufferOverflowException();
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
        }
    }
}
