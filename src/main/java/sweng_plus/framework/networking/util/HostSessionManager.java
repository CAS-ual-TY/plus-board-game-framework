package sweng_plus.framework.networking.util;

import sweng_plus.framework.networking.interfaces.IHostSessionManager;

import java.util.UUID;

public class HostSessionManager implements IHostSessionManager
{
    protected UUID sessionIdentifier;
    
    public HostSessionManager(UUID sessionIdentifier)
    {
        this.sessionIdentifier = sessionIdentifier;
    }
    
    public HostSessionManager()
    {
        this(UUID.randomUUID());
    }
    
    @Override
    public UUID getSessionIdentifier()
    {
        return sessionIdentifier;
    }
}
