package sweng_plus.framework.networking;

public interface IClientManager
{
    <M> void sendPacketToServer(M m);
}
