package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.MessageTracker;
import sweng_plus.framework.networking.util.TrackedMessage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class PacketTests
{
    @Test
    void testEncodingDecoding()
    {
        CircularBuffer buffer = new CircularBuffer(1024 * 4);
        TestMessage msg = new TestMessage("Hello World!", System.currentTimeMillis());
        MessageRegistry<IClient> r = new MessageRegistry<>(2);
        r.registerMessage((byte) 0, TestMessage.Handler::encodeMessage, TestMessage.Handler::decodeMessage,
                TestMessage.Handler::handleMessage, TestMessage.class);
        r.encodeMessage(buffer, msg, (byte) 0);
        
        r.<TestMessage>decodeMessage(buffer, (msg2, uMsgPosition, handler) ->
        {
            Assertions.assertTrue(buffer.isEmpty());
            Assertions.assertEquals(msg.message, msg2.message);
            Assertions.assertEquals(Long.toBinaryString(msg.timestamp), Long.toBinaryString(msg2.timestamp));
            handler.handleMessage(Optional.empty(), msg2);
        });
    }
    
    @Test
    void testMessageOrder()
    {
        ArrayList<TrackedMessage<Integer, ?>> list = new ArrayList<>(256);
        MessageTracker tracker = new MessageTracker();
        
        for(int i = 0; i < 250; ++i)
        {
            tracker.increment();
        }
        
        Random rand = new Random(123);
        
        byte nr = tracker.getByte();
        
        for(int i = 0; i < 10; ++i)
        {
            list.add(rand.nextInt(list.size() + 1), new TrackedMessage<Integer, IClient>(list.size(), null, nr++));
        }
        
        //System.out.println(list.stream().map(TrackedMessage::msg).collect(Collectors.toList()));
        //System.out.println(list.stream().map(TrackedMessage::uMsgPosition).map(Byte::toUnsignedInt).collect(Collectors.toList()));
        //System.out.println(tracker.getByte() + " | u: " + tracker.get());
        
        list.sort(tracker.makeComparator());
        
        //System.out.println(list.stream().map(TrackedMessage::msg).collect(Collectors.toList()));
        //System.out.println(list.stream().map(TrackedMessage::uMsgPosition).map(Byte::toUnsignedInt).collect(Collectors.toList()));
        //System.out.println(tracker.getByte() + " | u: " + tracker.get());
        
        for(int i = 0; i < list.size(); ++i)
        {
            Assertions.assertEquals(list.get(i).msg(), i);
        }
    }
}
