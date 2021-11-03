package sweng_plus.framework.networking;

import java.nio.ByteBuffer;

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
        public TestMessage receiveBytes(ByteBuffer buf)
        {
            StringBuilder s = new StringBuilder();
            
            char c;
            while((c = buf.getChar()) != 0)
            {
                s.append(c);
            }
            
            long timestamp = buf.getLong();
            
            return new TestMessage(s.toString(), timestamp);
        }
    
        @Override
        public void sendBytes(ByteBuffer buf, TestMessage msg)
        {
            for(char c : msg.message.toCharArray())
            {
                buf.putChar(c);
            }
            
            buf.putChar((char)0);
            
            buf.putLong(msg.timestamp);
        }
    
        @Override
        public void handleMessage(TestMessage msg)
        {
            System.out.println(msg.timestamp + ": " + msg.message);
        }
    }
}
