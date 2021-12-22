package sweng_plus.demos.chat;

import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.NetworkRole;

public class ChatClient extends Client
{
    protected String name;
    
    protected ChatClient(String ip, NetworkRole role, ClientStatus status)
    {
        super(ip, role, status);
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
}
