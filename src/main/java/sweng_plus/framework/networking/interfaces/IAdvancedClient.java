package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedClient extends IClient
{
    String getName();
    
    UUID getUUID();
    
    void setName(String name);
    
    void setUUID(UUID identifier);
}
