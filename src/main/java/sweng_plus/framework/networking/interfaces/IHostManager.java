package sweng_plus.framework.networking.interfaces;

import java.io.IOException;
import java.util.List;

public interface IHostManager extends IClientManager
{
    <M> void sendPacketToClient(IClient client, M m) throws IOException; // Main Thread
    
    <M> void sendPacketToAllClients(M m) throws IOException; // Main Thread
    
    List<? extends IClient> getAllClients(); // Not Thread-Safe
    
    IClient getHostClient();
}
