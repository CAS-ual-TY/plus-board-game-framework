package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public class PacketTests
{
    @Test
    void testEncodingDecoding()
    {
        CircularBuffer buffer = new CircularBuffer(1024 * 4);
        TestMessage msg = new TestMessage("Hello World!", System.currentTimeMillis());
        MessageRegistry<IClient> r = new MessageRegistry(2);
        r.registerMessage((byte) 0, TestMessage.Handler::encodeMessage, TestMessage.Handler::decodeMessage,
                TestMessage.Handler::handleMessage, TestMessage.class);
        r.encodeMessage(buffer, msg, (byte)0);
        
        r.<TestMessage>decodeMessage(buffer, (msg2, uMsgPosition, handler) ->
        {
            Assertions.assertTrue(buffer.isEmpty());
            Assertions.assertEquals(msg.message, msg2.message);
            Assertions.assertEquals(Long.toBinaryString(msg.timestamp), Long.toBinaryString(msg2.timestamp));
            handler.handleMessage(Optional.empty(), msg2);
        });
    }
}
