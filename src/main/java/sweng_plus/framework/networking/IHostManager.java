package sweng_plus.framework.networking;

public interface IHostManager extends IClientManager
{
    <M> void sendPacketToClient(IClientManager client, M m);
    
    <M> void sendPacketToAllClients(M m);
}
