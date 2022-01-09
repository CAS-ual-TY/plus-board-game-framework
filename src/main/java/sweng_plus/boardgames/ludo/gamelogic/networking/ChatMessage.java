package sweng_plus.boardgames.ludo.gamelogic.networking;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.ILudoScreen;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public record ChatMessage(String sender, String message)
{
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, ChatMessage message)
        {
            buf.writeString(message.sender(), StandardCharsets.UTF_8);
            buf.writeString(message.message(), StandardCharsets.UTF_8);
        }
        
        public static ChatMessage decodeMessage(CircularBuffer buf)
        {
            return new ChatMessage(buf.readString(StandardCharsets.UTF_8), buf.readString(StandardCharsets.UTF_8));
        }
        
        public static void handleMessage(Optional<LudoClient> clientOptional, ChatMessage message)
        {
            clientOptional.ifPresentOrElse(
                    (client) -> Ludo.instance().getNetworking().getHostManager().sendMessageToAllClients(
                            new ChatMessage(client.getName(), message.message())
                    ),
                    () -> ((ILudoScreen) Ludo.instance().getScreen()).chat(message));
        }
    }
}
