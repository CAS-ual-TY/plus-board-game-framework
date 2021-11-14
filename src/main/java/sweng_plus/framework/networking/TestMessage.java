package sweng_plus.framework.networking;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;

public class TestMessage
{
    public String message;
    public long timestamp;
    
    public TestMessage(String message, long timestamp)
    {
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public static class Handler implements IMessageHandler<TestMessage>
    {
        @Override
        public TestMessage receiveBytes(CircularBuffer buf)
        {
            //String message = buf.readString(StandardCharsets.UTF_8);
            long timestamp = buf.readLong();
            
            return new TestMessage("", timestamp);
        }
        
        @Override
        public void sendBytes(CircularBuffer buf, TestMessage msg)
        {
            //buf.writeString(msg.message, StandardCharsets.UTF_8);
            buf.writeLong(msg.timestamp);
        }
        
        @Override
        public void handleMessage(TestMessage msg)
        {
            System.out.println(msg.timestamp + ": " + msg.message);
        }
    }
}
