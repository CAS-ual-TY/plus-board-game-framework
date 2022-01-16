package sweng_plus.boardgames.mill.gamelogic.networking;


import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.IMillScreen;
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
        
        public static void handleMessage(Optional<MillClient> clientOptional, WinMessage message)
        {
            ((IMillScreen) Mill.instance().getScreen()).gameWon(message.winnerName());
        }
    }
}
