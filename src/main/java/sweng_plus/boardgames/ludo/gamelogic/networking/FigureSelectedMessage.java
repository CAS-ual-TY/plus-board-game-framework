package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.ILudoScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record FigureSelectedMessage(int figureID)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, FigureSelectedMessage message)
        {
            buf.writeInt(message.figureID());
        }
        
        public static FigureSelectedMessage decodeMessage(CircularBuffer buf)
        {
            return new FigureSelectedMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, FigureSelectedMessage message)
        {
            ((ILudoScreen) Ludo.instance().getScreen()).figureSelected(message.figureID());
        }
    }
}
