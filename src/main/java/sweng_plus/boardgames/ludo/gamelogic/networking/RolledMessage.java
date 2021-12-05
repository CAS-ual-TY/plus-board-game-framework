package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.ILudoScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record RolledMessage(int diceResult)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, RolledMessage message)
        {
            buf.writeInt(message.diceResult());
        }
        
        public static RolledMessage decodeMessage(CircularBuffer buf)
        {
            return new RolledMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, RolledMessage message)
        {
            ((ILudoScreen) Ludo.instance().getScreen()).diceResult(message.diceResult());
        }
    }
}
