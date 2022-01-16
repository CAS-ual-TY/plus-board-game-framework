package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.ILudoScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public record WinMessage(String winnerName)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, WinMessage message)
        {
            buf.writeString(message.winnerName(), StandardCharsets.UTF_8);
        }
        
        public static WinMessage decodeMessage(CircularBuffer buf)
        {
            return new WinMessage(buf.readString(StandardCharsets.UTF_8));
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, WinMessage message)
        {
            ((ILudoScreen) Ludo.instance().getScreen()).gameWon(message.winnerName());
        }
    }
}
