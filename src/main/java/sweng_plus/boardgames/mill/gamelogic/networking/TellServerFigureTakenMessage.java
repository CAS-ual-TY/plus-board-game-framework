package sweng_plus.boardgames.mill.gamelogic.networking;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record TellServerFigureTakenMessage(int figureID)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, TellServerFigureTakenMessage message)
        {
            buf.writeInt(message.figureID());
        }
        
        public static TellServerFigureTakenMessage decodeMessage(CircularBuffer buf)
        {
            return new TellServerFigureTakenMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<MillClient> clientOptional, TellServerFigureTakenMessage message)
        {
            clientOptional.ifPresent(client ->
            {
                if(client.getTeamIndex() == Mill.instance().getGameLogic().getCurrentTeamIndex())
                {
                    Mill.instance().getGameLogic().tellClientsFigureTaken(message.figureID());
                }
            });
        }
    }
}
