package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.util.Optional;

public class RollMessage
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, RollMessage message)
        {
        }
        
        public static RollMessage decodeMessage(CircularBuffer buf)
        {
            return new RollMessage();
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, RollMessage message)
        {
            clientOptional.ifPresent(client ->
            {
                System.out.println(client.getTeamIndex() + " " + Ludo.instance().getGameLogic().currentTeamIndex);
                
                if(client.getTeamIndex() == Ludo.instance().getGameLogic().currentTeamIndex)
                    Ludo.instance().getGameLogic().tellClientsRoll();
            });
        }
    }
}
