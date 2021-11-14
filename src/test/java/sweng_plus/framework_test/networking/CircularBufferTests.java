package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CircularBufferTests
{
    @Test
    void testBufferAccess()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeByte((byte) 'c'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'c', buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testBufferCyclicAccess()
    {
        CircularBuffer buffer = new CircularBuffer(3);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeByte((byte) 'a'));
        Assertions.assertDoesNotThrow(() -> buffer.writeByte((byte) 'b'));
        Assertions.assertDoesNotThrow(() -> buffer.writeByte((byte) 'c'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'a', buffer.readByte());
        Assertions.assertEquals((byte) 'b', buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
        
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeByte((byte) 'd'));
        Assertions.assertDoesNotThrow(() -> buffer.writeByte((byte) 'e'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'c', buffer.readByte());
        Assertions.assertEquals((byte) 'd', buffer.readByte());
        Assertions.assertEquals((byte) 'e', buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testBufferInvalidRead()
    {
        CircularBuffer buffer = new CircularBuffer(3);
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertDoesNotThrow(buffer::readByte);
        Assertions.assertThrows(BufferUnderflowException.class, buffer::endReading);
    }
    
    @Test
    void testBufferInvalidWrite()
    {
        CircularBuffer buffer = new CircularBuffer(3);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        buffer.writeByte((byte) 'a');
        buffer.writeByte((byte) 'b');
        buffer.writeByte((byte) 'c');
        buffer.writeByte((byte) 'd');
        Assertions.assertThrows(BufferOverflowException.class, buffer::endWriting);
    }
    
    @Test
    void testWriteInteger()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeInt(0x01020304));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(1, buffer.readByte());
        Assertions.assertEquals(2, buffer.readByte());
        Assertions.assertEquals(3, buffer.readByte());
        Assertions.assertEquals(4, buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadInteger()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeInt(0x01020304));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(0x01020304, buffer.readInt());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteLong()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeLong(0x0102030405060708L));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(1, buffer.readByte());
        Assertions.assertEquals(2, buffer.readByte());
        Assertions.assertEquals(3, buffer.readByte());
        Assertions.assertEquals(4, buffer.readByte());
        Assertions.assertEquals(5, buffer.readByte());
        Assertions.assertEquals(6, buffer.readByte());
        Assertions.assertEquals(7, buffer.readByte());
        Assertions.assertEquals(8, buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadLong()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeLong(0x0102030405060708L));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(0x0102030405060708L, buffer.readLong());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteCharacter()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('a', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('b', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('c', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('€', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(1, buffer.readShort());
        Assertions.assertEquals('a', buffer.readByte());
        Assertions.assertEquals(1, buffer.readShort());
        Assertions.assertEquals('b', buffer.readByte());
        Assertions.assertEquals(1, buffer.readShort());
        Assertions.assertEquals('c', buffer.readByte());
        Assertions.assertEquals(3, buffer.readShort());
        Assertions.assertEquals(String.valueOf('€').getBytes(StandardCharsets.UTF_8)[0], buffer.readByte());
        Assertions.assertEquals(String.valueOf('€').getBytes(StandardCharsets.UTF_8)[1], buffer.readByte());
        Assertions.assertEquals(String.valueOf('€').getBytes(StandardCharsets.UTF_8)[2], buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadCharacter()
    {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('a', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('b', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('c', StandardCharsets.UTF_16));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('€', StandardCharsets.UTF_16));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals('a', buffer.readChar(StandardCharsets.UTF_8));
        Assertions.assertEquals('b', buffer.readChar(StandardCharsets.UTF_8));
        Assertions.assertEquals('c', buffer.readChar(StandardCharsets.UTF_16));
        Assertions.assertEquals('€', buffer.readChar(StandardCharsets.UTF_16));
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteString()
    {
        CircularBuffer buffer = new CircularBuffer(16);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeString("abc", StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(3, buffer.readShort());
        Assertions.assertEquals('a', buffer.readByte());
        Assertions.assertEquals('b', buffer.readByte());
        Assertions.assertEquals('c', buffer.readByte());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadString()
    {
        CircularBuffer buffer = new CircularBuffer(16);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeString("abc", StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals("abc", buffer.readString(StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endReading);
        
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeString("abc", StandardCharsets.UTF_16));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals("abc", buffer.readString(StandardCharsets.UTF_16));
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteReadList()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("abc");
        list.add("def");
        list.add("abdecf");
        
        BiConsumer<CircularBuffer, String> encoder = (buf, s) -> buf.writeString(s, StandardCharsets.UTF_8);
        Function<CircularBuffer, String> decoder = (buf) -> buf.readString(StandardCharsets.UTF_8);
        
        CircularBuffer buffer = new CircularBuffer(1024);
        
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeList(list, encoder));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(list, buffer.readList(decoder));
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadIntegrity()
    {
        CircularBuffer buffer = new CircularBuffer(4);
        
        buffer.startWriting();
        buffer.writeByte((byte) 0);
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        buffer.endWriting();
        
        buffer.startReading();
        Assertions.assertEquals((byte) 0, buffer.readByte());
        Assertions.assertEquals((byte) 1, buffer.readByte());
        buffer.endReading();
        
        Assertions.assertEquals(2, buffer.getSize());
        
        buffer.startReading();
        Assertions.assertEquals((byte) 2, buffer.readByte());
        Assertions.assertEquals((byte) 3, buffer.readByte());
        Assertions.assertEquals((byte) 0, buffer.readByte());
        Assertions.assertThrows(BufferUnderflowException.class, () -> buffer.endReading());
        
        Assertions.assertEquals(buffer.getSize(), 0);
        
        buffer.startWriting();
        buffer.writeByte((byte) 4);
        buffer.writeByte((byte) 5);
        buffer.endWriting();
        
        buffer.startReading();
        Assertions.assertEquals((byte) 4, buffer.readByte());
        Assertions.assertEquals((byte) 5, buffer.readByte());
        Assertions.assertDoesNotThrow(() -> buffer.endReading());
    }
    
    @Test
    void testWriteIntegrity()
    {
        CircularBuffer buffer = new CircularBuffer(4);
        
        buffer.startWriting();
        buffer.writeByte((byte) 0);
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        buffer.endWriting();
        
        buffer.startReading();
        Assertions.assertEquals((byte) 0, buffer.readByte());
        Assertions.assertEquals((byte) 1, buffer.readByte());
        buffer.endReading();
        
        buffer.startWriting();
        buffer.writeByte((byte) 4);
        buffer.writeByte((byte) 5);
        buffer.writeByte((byte) 6);
        Assertions.assertThrows(BufferOverflowException.class, () -> buffer.endWriting());
        
        Assertions.assertEquals(2, buffer.getSize());
        
        buffer.startReading();
        Assertions.assertEquals((byte) 2, buffer.readByte());
        Assertions.assertEquals((byte) 3, buffer.readByte());
        Assertions.assertDoesNotThrow(() -> buffer.endReading());
        
        Assertions.assertEquals(buffer.getSize(), 0);
        
        buffer.startWriting();
        buffer.writeByte((byte) 4);
        buffer.writeByte((byte) 5);
        buffer.endWriting();
        
        buffer.startReading();
        Assertions.assertEquals((byte) 4, buffer.readByte());
        Assertions.assertEquals((byte) 5, buffer.readByte());
        Assertions.assertDoesNotThrow(() -> buffer.endReading());
    }
}
