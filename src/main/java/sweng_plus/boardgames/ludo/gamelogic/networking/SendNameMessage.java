package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public record SendNameMessage(String name)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, SendNameMessage message)
        {
            buf.writeString(message.name(), StandardCharsets.UTF_8);
        }
        
        public static SendNameMessage decodeMessage(CircularBuffer buf)
        {
            return new SendNameMessage(buf.readString(StandardCharsets.UTF_8));
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, SendNameMessage message)
        {
            clientOptional.ifPresentOrElse((client) ->
            {
                // Server
                client.setName(message.name());
                
                //TODO problem:
                // client1 connects
                // client1 sends name
                // client2 connects
                // client1 receives names
                // client2 sends name
                // client1 received empty name of client 2
    
                Ludo.instance().getHostManager().sendMessageToClient(client, SendNamesMessage.makeNamesMessage());
                Ludo.instance().getHostManager().sendMessageToAllClientsExcept(client, message);
            }, () ->
                    Ludo.instance().names.add(message.name()));
        }
    }
}
