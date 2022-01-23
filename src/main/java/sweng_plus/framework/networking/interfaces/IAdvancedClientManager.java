package sweng_plus.framework.networking.interfaces;

import java.util.UUID;

public interface IAdvancedClientManager extends IClientManager
{
    /**
     * @return The name of this client.
     */
    String getName();
    
    /**
     * Sets the session identifier received from the server (authentication).
     *
     * @param sessionIdentifier The session identifier.
     */
    void setSessionIdentifier(UUID sessionIdentifier);
    
    /**
     * @return The client identifier based on the session identifier set in {@link #setSessionIdentifier(UUID)}.
     */
    UUID getClientIdentifierForSession();
}
