package sweng_plus.demos.chat;

import sweng_plus.framework.networking.AdvancedClient;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.NetworkRole;

public class ChatClient extends AdvancedClient
{
    protected String name;
    
    protected ChatClient(String ip, NetworkRole role, ClientStatus status)
    {
        super(ip, role, status);
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
}
