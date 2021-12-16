package sweng_plus.framework_test.networking.manual_chat;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.interfaces.IAdvancedClientEventsListener;
import sweng_plus.framework.networking.interfaces.IAdvancedHostEventsListener;

public class NetTestEventsListener implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<Client>
{
    @Override
    public void disconnected()
    {
        NetTestGame.instance().setScreen(new NetTestMessageScreen(NetTestGame.instance(), "Disconnected"));
    }
    
    @Override
    public void lostConnection()
    {
        NetTestGame.instance().setScreen(new NetTestMessageScreen(NetTestGame.instance(), "Lost Connection"));
    }
    
    @Override
    public void disconnectedWithException(Exception e)
    {
        disconnected();
    }
    
    @Override
    public void clientConnected(Client client)
    {
    
    }
    
    @Override
    public void clientDisconnected(Client client)
    {
    
    }
}
