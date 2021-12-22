package sweng_plus.demos.chat;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;

import java.util.List;

public class ChatEventsListener implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<Client>
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
        displayScreen("Lost Connection...");
    }
    
    @Override
    public void serverClosed()
    {
        displayScreen("Server Closed.");
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
    public void clientConnected(Client client)
    {
        ChatGame.instance().hostManager.sendMessageToAllClients(new ChatMessage("Someone connected!", System.currentTimeMillis()));
    }
    
    @Override
    public void clientDisconnectedOrderly(Client client)
    {
        ChatGame.instance().hostManager.sendMessageToAllClients(new ChatMessage("Someone disconnected!", System.currentTimeMillis()));
    }
}
