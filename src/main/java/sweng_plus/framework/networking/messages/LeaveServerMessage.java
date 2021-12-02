package sweng_plus.framework.networking.messages;

import sweng_plus.framework.networking.util.CircularBuffer;

public record LeaveServerMessage(byte code)
{
    public static final byte ORDERLY_DISCONNECTED = 0;
    public static final byte DISCONNECTED_DUE_TO_EXCEPTION = 1;
    
    public static LeaveServerMessage orderlyDisconnected()
    {
        return new LeaveServerMessage(ORDERLY_DISCONNECTED);
    }
    
    public static LeaveServerMessage disconnectedDueToException()
    {
        return new LeaveServerMessage(DISCONNECTED_DUE_TO_EXCEPTION);
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, LeaveServerMessage message)
        {
            buf.writeByte(message.code());
        }
        
        public static LeaveServerMessage decodeMessage(CircularBuffer buf)
        {
            return new LeaveServerMessage(buf.readByte());
        }
    }
}
