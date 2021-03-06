package sweng_plus.framework.networking.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("PointlessBitwiseExpression")
public class CircularBuffer
{
    private static final int DEFAULT_SIZE = 1024;
    
    private final byte[] buffer;
    private final byte[] bufferCopy;
    private final int capacity;
    
    private int writeIndex;
    private int readIndex;
    
    private int size;
    private int tempIndex;
    
    public CircularBuffer(int capacity)
    {
        this.capacity = (capacity > 0) ? capacity : DEFAULT_SIZE;
        buffer = new byte[capacity];
        bufferCopy = new byte[capacity];
        
        writeIndex = 0;
        readIndex = 0;
        
        tempIndex = 0;
    }
    
    public CircularBuffer()
    {
        this(DEFAULT_SIZE);
    }
    
    public int getCapacity()
    {
        return capacity;
    }
    
    public int getWriteIndex()
    {
        return writeIndex;
    }
    
    public int getReadIndex()
    {
        return readIndex;
    }
    
    public int size()
    {
        return size;
    }
    
    public boolean isEmpty()
    {
        return size() == 0;
    }
    
    public void writeByte(byte b)
    {
        buffer[writeIndex] = b;
        writeIndex = (writeIndex + 1) % capacity;
        size++;
    }
    
    public void writeShort(short s)
    {
        writeByte((byte) ((s >> Byte.BYTES * 8) & 0xFF));
        writeByte((byte) (s & 0xFF));
    }
    
    public void writeInt(int i)
    {
        writeShort((short) ((i >> Short.BYTES * 8) & 0xFFFF));
        writeShort((short) (i & 0xFFFF));
    }
    
    public void writeLong(long l)
    {
        writeInt((int) ((l >> Integer.BYTES * 8) & 0xFFFFFFFF));
        writeInt((int) (l & 0xFFFFFFFF));
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
        readIndex = (readIndex + 1) % capacity;
        size--;
        return read;
    }
    
    public short readShort()
    {
        return (short) (((readByte() << Byte.BYTES * 8) & 0xFF00) |
                (readByte() & 0x00FF));
    }
    
    public int readInt()
    {
        return ((readShort() << Short.BYTES * 8) & 0xFFFF0000) |
                (readShort() & 0x0000FFFF);
    }
    
    public long readLong()
    {
        return (((long) readInt() << Integer.BYTES * 8) & 0xFFFFFFFF00000000L) |
                (readInt() & 0x00000000FFFFFFFFL);
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
    
    public void setByte(int index, byte b)
    {
        buffer[index % capacity] = b;
    }
    
    public void setShort(int index, short s)
    {
        setByte(index, (byte) ((s >> Byte.BYTES * 8) & 0xFF));
        setByte(index + Byte.BYTES, (byte) (s & 0xFF));
    }
    
    public void setInt(int index, int i)
    {
        setShort(index, (short) ((i >> Short.BYTES * 8) & 0xFFFF));
        setShort(index + Short.BYTES, (short) (i & 0xFFFF));
    }
    
    public void setLong(int index, long l)
    {
        setInt(index, (int) ((l >> Integer.BYTES * 8) & 0xFFFFFFFF));
        setInt(index + Integer.BYTES, (int) (l & 0xFFFFFFFF));
    }
    
    public byte getByte(int index)
    {
        return buffer[index % capacity];
    }
    
    public short getShort(int index)
    {
        return (short) (((getByte(index) << Byte.BYTES * 8) & 0xFF00) |
                (getByte(index + Byte.BYTES) & 0x00FF));
    }
    
    public int getInt(int index)
    {
        return ((getShort(index) << Short.BYTES * 8) & 0xFFFF0000) |
                (getShort(index + Short.BYTES) & 0x0000FFFF);
    }
    
    public long getLong(int index)
    {
        return (((long) getInt(index) << Integer.BYTES * 8) & 0xFFFFFFFF00000000L) |
                (getInt(index + Integer.BYTES) & 0x00000000FFFFFFFFL);
    }
    
    public byte peekByte()
    {
        return getByte(readIndex);
    }
    
    public short peekShort()
    {
        return getShort(readIndex);
    }
    
    public int peekInt()
    {
        return getInt(readIndex);
    }
    
    public long peekLong()
    {
        return getLong(readIndex);
    }
    
    public void writeToOutputStream(OutputStream out) throws IOException
    {
        if(readIndex <= writeIndex)
        {
            out.write(Arrays.copyOfRange(buffer, readIndex, writeIndex));
        }
        else //if(readIndex > writeIndex)
        {
            out.write(Arrays.copyOfRange(buffer, readIndex, capacity));
            out.write(Arrays.copyOfRange(buffer, 0, writeIndex));
        }
        
        clear();
    }
    
    public void readFrominputStream(InputStream in) throws IOException
    {
        writeBytes(in.readNBytes(in.available()));
    }
    
    public void startWriting()
    {
        tempIndex = writeIndex;
    }
    
    /**
     * Checkt, ob ??ber den Lesekopf geschrieben wurde
     *
     * @throws BufferOverflowException wenn ??ber den Lesekopf geschrieben wurde und setzt den State auf den State zur??ck, der beim startWriting call gegeben war
     */
    public void endWriting() throws BufferOverflowException
    {
        if(size > capacity)
        {
            System.arraycopy(bufferCopy, 0, buffer, 0, capacity);
            
            size -= (writeIndex < tempIndex ? writeIndex + capacity : writeIndex) - tempIndex;
            
            writeIndex = tempIndex;
            
            throw new BufferOverflowException();
        }
        else
        {
            System.arraycopy(buffer, 0, bufferCopy, 0, capacity);
        }
    }
    
    public void startReading()
    {
        tempIndex = readIndex;
    }
    
    /**
     * Checkt, ob ??ber den Schreibkopf gelesen wurde
     *
     * @throws BufferUnderflowException wenn ??ber den Schreibkopf gelesen wurde und leert daraufhin den Buffer komplett
     */
    public void endReading() throws BufferUnderflowException
    {
        if(size < 0)
        {
            clear();
            
            throw new BufferUnderflowException();
        }
    }
    
    public void clear()
    {
        writeIndex = 0;
        readIndex = writeIndex;
        size = 0;
    }
}
