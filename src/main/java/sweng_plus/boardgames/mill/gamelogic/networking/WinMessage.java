package sweng_plus.boardgames.mill.gamelogic.networking;


import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.IMillScreen;
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
        
        public static void handleMessage(Optional<MillClient> clientOptional, WinMessage message)
        {
            ((IMillScreen) Mill.instance().getScreen()).gameWon(message.winningTeam());
        }
    }
}
