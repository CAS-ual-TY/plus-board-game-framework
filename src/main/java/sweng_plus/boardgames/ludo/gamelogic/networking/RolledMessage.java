package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record RolledMessage(int roll)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, RolledMessage message)
        {
            buf.writeInt(message.roll());
        }
        
        public static RolledMessage decodeMessage(CircularBuffer buf)
        {
            return new RolledMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, RolledMessage message)
        {
            //TODO interact with client interface
        }
    }
}
