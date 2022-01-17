package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IHostSessionManager extends IClientSessionManager
{
    UUID getSessionIdentifier();
    
    @Override
    default UUID getIdentifierForSession(UUID session)
    {
        return getSessionIdentifier();
    }
}
