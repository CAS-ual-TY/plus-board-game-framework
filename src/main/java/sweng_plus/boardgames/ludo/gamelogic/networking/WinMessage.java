package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.ILudoScreen;
import sweng_plus.boardgames.ludo.gui.LudoScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record WinMessage(int winningTeam)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, WinMessage message)
        {
            buf.writeInt(message.winningTeam());
        }
        
        public static WinMessage decodeMessage(CircularBuffer buf)
        {
            return new WinMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, WinMessage message)
        {
            ((ILudoScreen) Ludo.instance().getScreen()).gameWon(message.winningTeam());
        }
    }
}
