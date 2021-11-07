package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.TestMessage;

import java.nio.ByteBuffer;

public class PacketTests
{
    @Test
    void testEncodingDecoding()
    {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        TestMessage msg = new TestMessage("Hello World!", System.currentTimeMillis());
        MessageRegistry r = new MessageRegistry(2);
        r.registerMessage((byte) 0, new TestMessage.Handler(), TestMessage.class);
        r.encodeMessage(buffer, msg);
        buffer.flip();
        
        r.<TestMessage>decodeMessage(buffer, (handler, msg2) ->
        {
            Assertions.assertFalse(buffer.hasRemaining());
            Assertions.assertEquals(msg.message, msg2.message);
            Assertions.assertEquals(msg.timestamp, msg2.timestamp);
            handler.handleMessage(msg2);
        });
    }
}
