package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.LudoScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public record StartGameMessage(int playerID, int teamCount)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, StartGameMessage message)
        {
            buf.writeInt(message.playerID());
            buf.writeInt(message.teamCount());
        }
        
        public static StartGameMessage decodeMessage(CircularBuffer buf)
        {
            return new StartGameMessage(buf.readInt(), buf.readInt());
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, StartGameMessage message)
        {
            Ludo.instance().startGame(false, message.teamCount());
            ((LudoScreen)Ludo.instance().getScreen()).thisPlayerID = message.playerID();
        }
    }
}
