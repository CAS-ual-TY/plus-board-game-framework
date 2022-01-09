package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedClientManager extends IClientManager
{
    String getName();
    
    void setSessionIdentifier(UUID sessionIdentifier);
    
    UUID getClientIdentifierForSession();
}
