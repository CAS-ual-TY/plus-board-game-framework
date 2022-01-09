package sweng_plus.demos.chat;

import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;

import java.util.List;

public class ChatEventsListener implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<ChatClient>
{
    private void displayScreen(List<String> message)
    {
        ChatGame.instance().setScreen(new ChatDisconnectedScreen(ChatGame.instance(), message));
    }
    
    private void displayScreen(String message)
    {
        displayScreen(List.of(message));
    }
    
    @Override
    public void lostConnection()
    {
        displayScreen("Lost connection...");
    }
    
    @Override
    public void serverClosed()
    {
        displayScreen("Server closed.");
    }
    
    @Override
    public void kickedFromServer()
    {
        displayScreen("You have been kicked.");
    }
    
    @Override
    public void kickedFromServerWithMessage(String message)
    {
        displayScreen(List.of("You have been kicked. Reason:", message));
    }
    
    @Override
    public void clientDisconnectedOrderly(ChatClient client)
    {
        ChatGame.instance().hostManager.sendMessageToAllClients(ChatMessage.announcement(client.getName() + " disconnected."));
    }
    
    @Override
    public void clientAuthSuccessful(ChatClient client)
    {
        ChatGame.instance().hostManager.sendMessageToAllClients(ChatMessage.announcement(client.getName() + " connected."));
    }
}
