package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.util.Buffer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

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
        Assertions.assertFalse(buffer.canWrite());
        Assertions.assertThrows(BufferOverflowException.class, buffer::startWriting);
    }
}
