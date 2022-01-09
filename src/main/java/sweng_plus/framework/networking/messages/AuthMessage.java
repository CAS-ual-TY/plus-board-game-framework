package sweng_plus.framework.networking.messages;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record AuthMessage(byte code, UUID identifier, String name)
{
    public static final byte SERVER_REQUEST = 0;
    public static final byte CLIENT_RESPONSE = 1;
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, AuthMessage message)
        {
            buf.writeByte(message.code());
            buf.writeLong(message.identifier().getLeastSignificantBits());
            buf.writeLong(message.identifier().getMostSignificantBits());
            
            if(message.code() == CLIENT_RESPONSE)
            {
                buf.writeString(message.name(), StandardCharsets.UTF_8);
            }
        }
        
        public static AuthMessage decodeMessage(CircularBuffer buf)
        {
            byte type = buf.readByte();
            long leastSignificantBits = buf.readLong();
            long mostSignificantBits = buf.readLong();
            
            String message = type == CLIENT_RESPONSE ? buf.readString(StandardCharsets.UTF_8) : "";
            
            return new AuthMessage(type, new UUID(mostSignificantBits, leastSignificantBits), message);
        }
    }
}
