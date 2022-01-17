package sweng_plus.framework_test.networking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sweng_plus.framework.networking.util.ClientSessionManager;
import sweng_plus.framework.networking.util.HostSessionManager;

import java.io.IOException;
import java.util.UUID;

public class MiscTests
{
    @Test
    public void testSessionSerialization() throws IOException
    {
        UUID hostIdentifier = UUID.randomUUID();
        
        HostSessionManager host = new HostSessionManager(hostIdentifier);
        
        ClientSessionManager client = new ClientSessionManager();
        UUID clientIdentifier1 = client.getIdentifierForSession(hostIdentifier);
        
        client.writeToFile();
        
        client = new ClientSessionManager();
        UUID clientIdentifier2 = client.getIdentifierForSession(hostIdentifier);
    
        Assertions.assertEquals(clientIdentifier1, clientIdentifier2);
    }
}
