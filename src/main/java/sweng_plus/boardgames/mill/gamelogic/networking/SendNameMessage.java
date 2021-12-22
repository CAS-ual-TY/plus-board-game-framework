package sweng_plus.boardgames.mill.gamelogic.networking;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.framework.networking.util.CircularBuffer;

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
        
        public static void handleMessage(Optional<MillClient> clientOptional, SendNameMessage message)
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
                
                Mill.instance().getNetworking().getHostManager().sendMessageToClient(client, SendNamesMessage.makeNamesMessage());
                Mill.instance().getNetworking().getHostManager().sendMessageToAllClientsExcept(client, message);
            }, () ->
                    Mill.instance().names.add(message.name()));
        }
    }
}
