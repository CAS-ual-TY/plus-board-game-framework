package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public record SendNamesMessage(List<String> names)
{
    public static SendNamesMessage makeNamesMessage()
    {
        return new SendNamesMessage(Ludo.instance().names);
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, SendNamesMessage message)
        {
            buf.writeList(message.names(), (buf2, name) -> buf2.writeString(name, StandardCharsets.UTF_8));
        }
        
        public static SendNamesMessage decodeMessage(CircularBuffer buf)
        {
            return new SendNamesMessage(buf.readList((buf2) -> buf.readString(StandardCharsets.UTF_8)));
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, SendNamesMessage message)
        {
            //TODO interact with interface instead of game logic directly
            Ludo.instance().getGameLogic().endPhaseRoll();
        }
    }
}
