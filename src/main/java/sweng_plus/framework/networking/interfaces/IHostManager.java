package sweng_plus.framework.networking.interfaces;

import java.io.IOException;
import java.util.List;

public interface IHostManager extends IClientManager
{
    <M> void sendMessageToClient(IClient client, M message) throws IOException; // Main Thread
    
    <M> void sendMessageToAllClients(M message) throws IOException; // Main Thread
    
    List<? extends IClient> getAllClients(); // Main Thread
    
    IClient getHostClient(); // Main Thread
}
