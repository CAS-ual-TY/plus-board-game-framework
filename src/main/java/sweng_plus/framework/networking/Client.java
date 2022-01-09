package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.IClientFactory;
import sweng_plus.framework.networking.util.NetworkRole;

public class Client implements IClient
{
    public String ip;
    public NetworkRole role;
    public ClientStatus status;
    
    protected Client(String ip, NetworkRole role, ClientStatus status)
    {
        this.ip = ip;
        this.role = role;
        this.status = status;
    }
    
    public Client(String ip)
    {
        this(ip, NetworkRole.CLIENT, ClientStatus.CONNECTED);
    }
    
    @Override
    public String getIP()
    {
        return ip;
    }
    
    @Override
    public NetworkRole getRole()
    {
        return role;
    }
    
    @Override
    public ClientStatus getStatus()
    {
        return status;
    }
    
    @Override
    public void setStatus(ClientStatus status)
    {
        this.status = status;
    }
    
    public static IClientFactory<Client> createFactory()
    {
        return Client::new;
    }
}
