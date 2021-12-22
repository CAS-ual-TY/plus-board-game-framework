package sweng_plus.demos.chat;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public record ChatNameMessage(byte type, String name)
{
    public static byte REQUEST = 0;
    public static byte RESPONSE = 1;
    
    public static ChatNameMessage request()
    {
        return new ChatNameMessage(REQUEST, "");
    }
    
    public static ChatNameMessage response(String name)
    {
        return new ChatNameMessage(RESPONSE, name);
    }
    
    public static class Handler
    {
        public static void encodeMessage(CircularBuffer buf, ChatNameMessage msg)
        {
            buf.writeByte(msg.type());
            
            if(msg.type() == RESPONSE)
            {
                buf.writeString(msg.name(), StandardCharsets.UTF_8);
            }
        }
        
        public static ChatNameMessage decodeMessage(CircularBuffer buf)
        {
            byte type = buf.readByte();
            String message;
            
            if(type == RESPONSE)
            {
                message = buf.readString(StandardCharsets.UTF_8);
            }
            else
            {
                message = "";
            }
            
            return new ChatNameMessage(type, message);
        }
        
        public static void handleMessage(Optional<ChatClient> clientOptional, ChatNameMessage msg)
        {
            clientOptional.ifPresentOrElse(
                    (client) ->
                    {
                        String name = msg.name().trim();
                        
                        if(name.isEmpty())
                        {
                            ChatGame.instance().hostManager.sendMessageToAllClientsExcept(client, ChatMessage.announcement("Client kicked for empty name."));
                            ChatGame.instance().hostManager.sendMessageToClient(client, ChatGame.instance().protocol.kickClient("Empty name"));
                            ChatGame.instance().hostManager.closeClient(client);
                        }
                        else
                        {
                            ChatGame.instance().hostManager.sendMessageToAllClients(ChatMessage.announcement(name + " connected."));
                            client.setName(msg.name());
                        }
                    },
                    () -> ChatGame.instance().clientManager.sendMessageToServer(response(ChatGame.instance().name))
            );
        }
    }
}
