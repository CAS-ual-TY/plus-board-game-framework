package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedHostManager<C extends IAdvancedClient> extends IHostManager<C>, IAdvancedClientManager
{
    void kickClient(C client);
    
    default void kickClient(C client, String message)
    {
        kickClient(client);
    }
    
    default void authenticate(C client, String name, UUID identifier)
    {
        client.setName(name.trim());
        client.setUUID(identifier);
    }
    
    UUID getSessionIdentifier();
    
    @Override
    default void setSessionIdentifier(UUID sessionIdentifier) {}
    
    @Override
    default UUID getClientIdentifierForSession()
    {
        return getSessionIdentifier();
    }
}
