package sweng_plus.framework_test.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IMessageHandler;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TestMessage
{
    public String message;
    public long timestamp;
    
    public TestMessage(String message, long timestamp)
    {
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public static class Handler implements IMessageHandler<TestMessage, IClient>
    {
        @Override
        public TestMessage decodeMessage(CircularBuffer buf)
        {
            String message = buf.readString(StandardCharsets.UTF_8);
            long timestamp = buf.readLong();
            
            return new TestMessage(message, timestamp);
        }
        
        @Override
        public void encodeMessage(CircularBuffer buf, TestMessage msg)
        {
            buf.writeString(msg.message, StandardCharsets.UTF_8);
            buf.writeLong(msg.timestamp);
        }
        
        @Override
        public void handleMessage(Optional<IClient> clientOptional, TestMessage msg)
        {
            System.out.println(msg.timestamp + ": " + msg.message);
        }
    }
}
