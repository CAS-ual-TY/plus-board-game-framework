package sweng_plus.boardgames.mill.gamelogic.networking;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.IMillScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record FigureTakenMessage(int figureID)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, FigureTakenMessage message)
        {
            buf.writeInt(message.figureID());
        }
        
        public static FigureTakenMessage decodeMessage(CircularBuffer buf)
        {
            return new FigureTakenMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<MillClient> clientOptional, FigureTakenMessage message)
        {
            ((IMillScreen) Mill.instance().getScreen()).figureTaken(message.figureID());
        }
    }
}
