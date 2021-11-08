package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.util.Buffer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;

public class BufferTest
{
    @Test
    void testBufferAccess() {
        Buffer buffer = new Buffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'c'));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals((byte) 'c', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testBufferCyclicAccess() {
        Buffer buffer = new Buffer(3);
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
        Buffer buffer = new Buffer(3);
        Assertions.assertThrows(BufferUnderflowException.class, buffer::startReading);
    }
    
    @Test
    void testBufferInvalidWrite() {
        Buffer buffer = new Buffer(3);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        buffer.write((byte) 'a');
        buffer.write((byte) 'b');
        buffer.write((byte) 'c');
        Assertions.assertDoesNotThrow(buffer::endWriting);
        Assertions.assertThrows(BufferOverflowException.class, buffer::startWriting);
    }
    
    @Test
    void testWriteInteger() {
        Buffer buffer = new Buffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write(1));
        Assertions.assertDoesNotThrow(() -> buffer.write(0xFF));
        Assertions.assertDoesNotThrow(() -> buffer.write(0xFFFF));
        Assertions.assertDoesNotThrow(() -> buffer.write(0xFFFFFF));
        Assertions.assertDoesNotThrow(() -> buffer.write(0xFFFFFFFF));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    }
    
    @Test
    void testWriteCharacter() {
        Buffer buffer = new Buffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write('a', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.write('b', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(() -> buffer.write('c', StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
    
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals('a', buffer.read());
        Assertions.assertEquals('b', buffer.read());
        Assertions.assertEquals('c', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
    
    @Test
    void testWriteString() {
        Buffer buffer = new Buffer(1024);
        Assertions.assertDoesNotThrow(buffer::startWriting);
        Assertions.assertDoesNotThrow(() -> buffer.write("abc", StandardCharsets.UTF_8));
        Assertions.assertDoesNotThrow(buffer::endWriting);
        
        Assertions.assertDoesNotThrow(buffer::startReading);
        Assertions.assertEquals('a', buffer.read());
        Assertions.assertEquals('b', buffer.read());
        Assertions.assertEquals('c', buffer.read());
        Assertions.assertDoesNotThrow(buffer::endReading);
    }
}
