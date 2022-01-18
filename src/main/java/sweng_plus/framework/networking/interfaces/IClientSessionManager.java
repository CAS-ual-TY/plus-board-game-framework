package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IClientSessionManager
{
    UUID getIdentifierForSession(UUID session);
}
