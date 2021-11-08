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
        Assertions.assertTrue(buffer.canWrite());
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'c'));
        Assertions.assertTrue(buffer.canRead());
        Assertions.assertEquals((byte) 'c', buffer.read());
        Assertions.assertFalse(buffer.canRead());
    }
    
    @Test
    void testBufferCyclicAccess() {
        Buffer buffer = new Buffer(3);
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'a'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'b'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'c'));
        
        Assertions.assertEquals((byte) 'a', buffer.read());
        Assertions.assertEquals((byte) 'b', buffer.read());
        
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'd'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'e'));
    
        Assertions.assertEquals((byte) 'c', buffer.read());
        Assertions.assertEquals((byte) 'd', buffer.read());
        Assertions.assertEquals((byte) 'e', buffer.read());
    }
    
    @Test
    void testBufferInvalidRead() {
        Buffer buffer = new Buffer(3);
        Assertions.assertFalse(buffer.canRead());
        Assertions.assertThrows(BufferUnderflowException.class, buffer::read);
    }
    
    @Test
    void testBufferInvalidWrite() {
        Buffer buffer = new Buffer(3);
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'a'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'b'));
        Assertions.assertDoesNotThrow(() -> buffer.write((byte) 'c'));
        Assertions.assertFalse(buffer.canWrite());
        Assertions.assertThrows(BufferOverflowException.class, () -> buffer.write((byte) 'd'));
    }
}
