package sweng_plus.demos.chat;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public record ChatMessage(byte type, String sender, String message, long timestamp)
{
    public static byte SERVER_TO_CLIENT = 0;
    public static byte CLIENT_TO_SERVER = 1;
    public static byte ANNOUNCEMENT = 2;
    
    public static ChatMessage serverToClient(ChatClient client, String message)
    {
        return new ChatMessage(SERVER_TO_CLIENT, client.getName(), message, System.currentTimeMillis());
    }
    
    public static ChatMessage clientToServer(String message)
    {
        return new ChatMessage(CLIENT_TO_SERVER, "", message, 0);
    }
    
    public static ChatMessage announcement(String message)
    {
        return new ChatMessage(ANNOUNCEMENT, "", message, System.currentTimeMillis());
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, ChatMessage msg)
        {
            buf.writeByte(msg.type());
            
            if(msg.type() == CLIENT_TO_SERVER)
            {
                buf.writeString(msg.message(), StandardCharsets.UTF_8);
            }
            else if(msg.type() == ANNOUNCEMENT)
            {
                buf.writeString(msg.message(), StandardCharsets.UTF_8);
                buf.writeLong(msg.timestamp());
            }
            else
            {
                buf.writeString(msg.sender(), StandardCharsets.UTF_8);
                buf.writeString(msg.message(), StandardCharsets.UTF_8);
                buf.writeLong(msg.timestamp());
            }
        }
        
        public static ChatMessage decodeMessage(CircularBuffer buf)
        {
            byte type = buf.readByte();
            
            if(type == CLIENT_TO_SERVER)
            {
                String message = buf.readString(StandardCharsets.UTF_8);
                
                return new ChatMessage(type, "", message, 0);
            }
            else if(type == ANNOUNCEMENT)
            {
                String message = buf.readString(StandardCharsets.UTF_8);
                long timestamp = buf.readLong();
                
                return new ChatMessage(type, "", message, timestamp);
            }
            else
            {
                String sender = buf.readString(StandardCharsets.UTF_8);
                String message = buf.readString(StandardCharsets.UTF_8);
                long timestamp = buf.readLong();
                
                return new ChatMessage(type, sender, message, timestamp);
            }
        }
        
        public static void handleMessage(Optional<ChatClient> clientOptional, ChatMessage msg)
        {
            clientOptional.ifPresentOrElse(
                    (client) -> ChatMain.instance().hostManager.sendMessageToAllClients(serverToClient(client, msg.message())),
                    () -> ((ChatScreen) ChatMain.instance().getScreen())
                            .addMessage(msg.sender(), msg.message(), msg.timestamp())
            );
        }
    }
}
