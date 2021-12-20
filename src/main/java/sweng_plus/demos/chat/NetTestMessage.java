package sweng_plus.demos.chat;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.NetworkRole;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class NetTestMessage
{
    public String sender;
    public String message;
    public long timestamp;
    
    public NetTestMessage(String sender, String message, long timestamp)
    {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public NetTestMessage(String message, long timestamp)
    {
        this("", message, timestamp);
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, NetTestMessage msg)
        {
            buf.writeString(msg.sender, StandardCharsets.UTF_8);
            buf.writeString(msg.message, StandardCharsets.UTF_8);
            buf.writeLong(msg.timestamp);
        }
        
        public static NetTestMessage decodeMessage(CircularBuffer buf)
        {
            String sender = buf.readString(StandardCharsets.UTF_8);
            String message = buf.readString(StandardCharsets.UTF_8);
            long timestamp = buf.readLong();
            
            return new NetTestMessage(sender, message, timestamp);
        }
        
        public static void handleMessage(Optional<Client> clientOptional, NetTestMessage msg)
        {
            clientOptional.ifPresentOrElse(
                    (client) ->
                    {
                        msg.sender = client.getRole() == NetworkRole.HOST ? "Host" : "Client";
                        NetTestGame.instance().hostManager.sendMessageToAllClients(msg);
                    },
                    () -> ((NetTestChatScreen) NetTestGame.instance().getScreen())
                            .addMessage(msg.sender, msg.message, msg.timestamp)
            );
        }
    }
}
