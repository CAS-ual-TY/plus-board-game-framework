package sweng_plus.framework.networking;

public interface IHostManager extends IClientManager
{
    <M> void sendPacketToClient(IClient client, M m); // Main Thread
    
    <M> void sendPacketToAllClients(M m); // Main Thread
}
