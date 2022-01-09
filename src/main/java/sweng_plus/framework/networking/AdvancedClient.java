package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IAdvancedClient;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.IClientFactory;
import sweng_plus.framework.networking.util.NetworkRole;

import java.util.UUID;

public class AdvancedClient extends Client implements IAdvancedClient
{
    protected String name;
    protected UUID identifier;
    
    protected AdvancedClient(String ip, NetworkRole role, ClientStatus status)
    {
        super(ip, role, status);
    }
    
    public AdvancedClient(String ip)
    {
        super(ip);
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public UUID getUUID()
    {
        return identifier;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public void setUUID(UUID identifier)
    {
        this.identifier = identifier;
    }
    
    public static IClientFactory<AdvancedClient> createAdvancedFactory()
    {
        return AdvancedClient::new;
    }
}
