package sweng_plus.boardgames.mill.gamelogic.networking;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.boardgames.mill.gui.IMillScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record FigureNodeSelectedMessage(int figureID, int nodeId)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, FigureNodeSelectedMessage message)
        {
            buf.writeInt(message.figureID());
            buf.writeInt(message.nodeId());
        }
        
        public static FigureNodeSelectedMessage decodeMessage(CircularBuffer buf)
        {
            return new FigureNodeSelectedMessage(buf.readInt(), buf.readInt());
        }
        
        public static void handleMessage(Optional<MillClient> clientOptional, FigureNodeSelectedMessage message)
        {
            ((IMillScreen) Mill.instance().getScreen()).figureNodeSelected(message.figureID(), message.nodeId());
        }
    }
}
