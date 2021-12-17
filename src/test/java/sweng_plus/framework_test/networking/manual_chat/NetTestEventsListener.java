package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;

import java.io.IOException;

public class NetTestEventsListener implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<Client>
{
    @Override
    public void lostConnection()
    {
        NetTestGame.instance().setScreen(new NetTestMessageScreen(NetTestGame.instance(), "Lost Connection"));
    }
    
    @Override
    public void clientConnected(Client client)
    {
        NetTestGame.instance().hostManager.sendMessageToAllClients(new NetTestMessage("Someone connected!", System.currentTimeMillis()));
    }
    
    @Override
    public void clientDisconnectedOrderly(Client client)
    {
        NetTestGame.instance().hostManager.sendMessageToAllClients(new NetTestMessage("Someone disconnected!", System.currentTimeMillis()));
    }
}
