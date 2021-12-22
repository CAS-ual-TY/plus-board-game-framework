package sweng_plus.demos.chat;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.NetworkRole;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ChatMessage
{
    public String sender;
    public String message;
    public long timestamp;
    
    public ChatMessage(String sender, String message, long timestamp)
    {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public ChatMessage(String message, long timestamp)
    {
        this("", message, timestamp);
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, ChatMessage msg)
        {
            buf.writeString(msg.sender, StandardCharsets.UTF_8);
            buf.writeString(msg.message, StandardCharsets.UTF_8);
            buf.writeLong(msg.timestamp);
        }
        
        public static ChatMessage decodeMessage(CircularBuffer buf)
        {
            String sender = buf.readString(StandardCharsets.UTF_8);
            String message = buf.readString(StandardCharsets.UTF_8);
            long timestamp = buf.readLong();
            
            return new ChatMessage(sender, message, timestamp);
        }
        
        public static void handleMessage(Optional<Client> clientOptional, ChatMessage msg)
        {
            clientOptional.ifPresentOrElse(
                    (client) ->
                    {
                        msg.sender = client.getRole() == NetworkRole.HOST ? "Host" : "Client";
                        ChatGame.instance().hostManager.sendMessageToAllClients(msg);
                    },
                    () -> ((ChatScreen) ChatGame.instance().getScreen())
                            .addMessage(msg.sender, msg.message, msg.timestamp)
            );
        }
    }
}
