package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record NewTurnMessage(int turnTeam)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, NewTurnMessage message)
        {
            buf.writeInt(message.turnTeam());
        }
        
        public static NewTurnMessage decodeMessage(CircularBuffer buf)
        {
            return new NewTurnMessage(buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, NewTurnMessage message)
        {
            //TODO interact with interface instead of game logic directly
            Ludo.instance().gameLogic.endPhaseRoll();
        }
    }
}
