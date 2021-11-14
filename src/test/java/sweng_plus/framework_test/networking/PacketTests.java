package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.TestMessage;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.math.BigInteger;

public class PacketTests
{
    @Test
    void testEncodingDecoding()
    {
        CircularBuffer buffer = new CircularBuffer(1024*4);
        TestMessage msg = new TestMessage("Hello World!", System.currentTimeMillis());
        MessageRegistry r = new MessageRegistry(2);
        r.registerMessage((byte) 0, new TestMessage.Handler(), TestMessage.class);
        r.encodeMessage(buffer, msg);
        
        r.<TestMessage>decodeMessage(buffer, (handler, msg2) ->
        {
            Assertions.assertTrue(buffer.isEmpty());
            Assertions.assertEquals(msg.message, msg2.message);
            Assertions.assertEquals(Long.toBinaryString(msg.timestamp), Long.toBinaryString(msg2.timestamp));
            handler.handleMessage(msg2);
        });
    }
}
