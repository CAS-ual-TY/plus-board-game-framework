package sweng_plus.framework.networking.util;

import org.lwjgl.system.CallbackI;

import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
    
    public void write(byte b)
    {
        buffer[numWrites % size] = b;
        numWrites++;
    }
    
    public byte read()
    {
        byte read = buffer[numReads % size];
        numReads++;
        return read;
    }
    
    public void write(int i) {
        System.out.println(BigInteger.valueOf(i).toByteArray().length);
        for(byte b : BigInteger.valueOf(i).toByteArray()) {
            write(b);
        }
    }
    
    public void write(char c, Charset charset) {
        write(String.valueOf(c), charset);
    }
    
    public void write(String s, Charset charset) {
        for(byte b : s.getBytes(charset)) {
            write(b);
        }
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
