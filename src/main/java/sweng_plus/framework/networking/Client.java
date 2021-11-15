package sweng_plus.framework.networking;

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
    
    // TODO Thread-Safe machen
    public Client changeStatus(ClientStatus status)
    {
        this.status = status;
        return this;
    }
    
    public static Client makeHost()
    {
        return new Client("host", NetworkRole.HOST, ClientStatus.CONNECTED);
    }
}
