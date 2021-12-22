package sweng_plus.boardgames.mill.gamelogic.networking;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record StartGameMessage(int playerID, int teamCount, int startTeam)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, StartGameMessage message)
        {
            buf.writeInt(message.playerID());
            buf.writeInt(message.teamCount());
            buf.writeInt(message.startTeam());
        }
        
        public static StartGameMessage decodeMessage(CircularBuffer buf)
        {
            return new StartGameMessage(buf.readInt(), buf.readInt(), buf.readInt());
        }
        
        public static void handleMessage(Optional<MillClient> clientOptional, StartGameMessage message)
        {
            Mill.instance().startGame(false, message.teamCount());
            ((MillScreen) Mill.instance().getScreen()).thisPlayerID = message.playerID();
            ((MillScreen) Mill.instance().getScreen()).newTurn(message.startTeam());
        }
    }
}
