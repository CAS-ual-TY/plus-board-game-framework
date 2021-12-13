package sweng_plus.framework.networking.messages;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;

public record KickClientMessage(byte code, String message)
{
    public static final byte UNKNOWN = 0;
    public static final byte SERVER_CLOSED = 1;
    public static final byte CLIENT_KICKED = 2;
    public static final byte CLIENT_KICKED_MESSAGE = 3;
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, KickClientMessage message)
        {
            buf.writeByte(message.code());
            
            if(message.code() == CLIENT_KICKED_MESSAGE)
            {
                buf.writeString(message.message(), StandardCharsets.UTF_8);
            }
        }
        
        public static KickClientMessage decodeMessage(CircularBuffer buf)
        {
            byte code = buf.readByte();
            String message = code == CLIENT_KICKED_MESSAGE ? buf.readString(StandardCharsets.UTF_8) : "";
            
            return new KickClientMessage(code, message);
        }
    }
}
