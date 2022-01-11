package sweng_plus.boardgames.mill.gamelogic.networking;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record TellServerFigureNodeSelectedMessage(int figureID, int nodeId)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, TellServerFigureNodeSelectedMessage message)
        {
            buf.writeInt(message.figureID());
            buf.writeInt(message.nodeId());
        }
        
        public static TellServerFigureNodeSelectedMessage decodeMessage(CircularBuffer buf)
        {
            return new TellServerFigureNodeSelectedMessage(buf.readInt(), buf.readInt());
        }
        
        public static void handleMessage(Optional<MillClient> clientOptional, TellServerFigureNodeSelectedMessage message)
        {
            clientOptional.ifPresent(client ->
            {
                if(client.getTeamIndex() == Mill.instance().getGameLogic().getCurrentTeamIndex())
                {
                    Mill.instance().getGameLogic().tellClientsFigureNodeSelected(message.figureID(), message.nodeId());
                }
            });
        }
    }
}
