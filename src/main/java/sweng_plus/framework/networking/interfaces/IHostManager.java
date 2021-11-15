package sweng_plus.framework.networking.interfaces;

import java.util.List;

public interface IHostManager extends IClientManager
{
    <M> void sendPacketToClient(IClient client, M m); // Main Thread
    
    <M> void sendPacketToAllClients(M m); // Main Thread
    
    List<? extends IClient> getAllClients(); // Not Thread-Safe
    
    IClient getHostClient();
}
