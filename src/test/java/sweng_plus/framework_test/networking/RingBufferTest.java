package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;

public class RingBufferTest
{
    @Test
    void testBufferAccess() {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'c'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'c', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testBufferCyclicAccess() {
        CircularBuffer buffer = new CircularBuffer(3);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'a'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'b'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'c'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'a', buffer.read());
        Assertions.assertEquals((byte) 'b', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
        
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'd'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'e'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'c', buffer.read());
        Assertions.assertEquals((byte) 'd', buffer.read());
        Assertions.assertEquals((byte) 'e', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testBufferInvalidRead() {
        CircularBuffer buffer = new CircularBuffer(3);
        Assertions.assertThrows(BufferUnderflowException.class, buffer::startReading);
    }
    
    @Test
    void testBufferInvalidWrite() {
        CircularBuffer buffer = new CircularBuffer(3);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        buffer.write((byte) 'a');
        buffer.write((byte) 'b');
        buffer.write((byte) 'c');
        Assertions.assertDoesNotThrow(buffer::endWriting);
        Assertions.assertThrows(BufferOverflowException.class, buffer::startWriting);
    }
    
    @Test
    void testWriteInteger() {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeInt(0x01020304));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(1, buffer.read());
        Assertions.assertEquals(2, buffer.read());
        Assertions.assertEquals(3, buffer.read());
        Assertions.assertEquals(4, buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadInteger() {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeInt(0x01020304));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(0x01020304, buffer.readInt());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteLong() {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeLong(0x0102030405060708L));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(1, buffer.read());
        Assertions.assertEquals(2, buffer.read());
        Assertions.assertEquals(3, buffer.read());
        Assertions.assertEquals(4, buffer.read());
        Assertions.assertEquals(5, buffer.read());
        Assertions.assertEquals(6, buffer.read());
        Assertions.assertEquals(7, buffer.read());
        Assertions.assertEquals(8, buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadLong() {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeLong(0x0102030405060708L));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(0x0102030405060708L, buffer.readLong());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteCharacter() {
        CircularBuffer buffer = new CircularBuffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('a', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('b', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('c', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.writeChar('€', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(1, buffer.readInt());
        Assertions.assertEquals('a', buffer.read());
        Assertions.assertEquals(1, buffer.readInt());
        Assertions.assertEquals('b', buffer.read());
        Assertions.assertEquals(1, buffer.readInt());
        Assertions.assertEquals('c', buffer.read());
        Assertions.assertEquals(3, buffer.readInt());
        Assertions.assertEquals(String.valueOf('€').getBytes(StandardCharsets.UTF_8)[0], buffer.read());
        Assertions.assertEquals(String.valueOf('€').getBytes(StandardCharsets.UTF_8)[1], buffer.read());
        Assertions.assertEquals(String.valueOf('€').getBytes(StandardCharsets.UTF_8)[2], buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadCharacter() {
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
    void testWriteString() {
        CircularBuffer buffer = new CircularBuffer(16);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.writeString("abc", StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals(3, buffer.readInt());
        Assertions.assertEquals('a', buffer.read());
        Assertions.assertEquals('b', buffer.read());
        Assertions.assertEquals('c', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testReadString() {
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
}
