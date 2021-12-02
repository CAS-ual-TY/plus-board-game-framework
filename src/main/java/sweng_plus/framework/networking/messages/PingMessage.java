package sweng_plus.framework.networking.messages;

import sweng_plus.framework.networking.util.CircularBuffer;

public record PingMessage(byte code)
{
    public static final byte REQUEST = 0;
    public static final byte RESPONSE = 1;
    
    public static PingMessage requestPing()
    {
        return new PingMessage(REQUEST);
    }
    
    public static PingMessage respondToPing()
    {
        return new PingMessage(RESPONSE);
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, PingMessage message)
        {
            buf.writeByte(message.code());
        }
        
        public static PingMessage decodeMessage(CircularBuffer buf)
        {
            return new PingMessage(buf.readByte());
        }
    }
}
