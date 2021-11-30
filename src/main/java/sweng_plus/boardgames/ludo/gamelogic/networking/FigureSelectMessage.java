package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record FigureSelectMessage(int figureID)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, FigureSelectMessage message)
        {
            buf.writeInt(message.figureID());
        }
        
        public static FigureSelectMessage decodeMessage(CircularBuffer buf)
        {
            return new FigureSelectMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, FigureSelectMessage message)
        {
            clientOptional.ifPresent(client ->
            {
                if(client.getTeamIndex() == Ludo.instance().getGameLogic().currentTeamIndex)
                    Ludo.instance().getGameLogic().tellClientsFigureSelected(message.figureID());
            });
        }
    }
}
