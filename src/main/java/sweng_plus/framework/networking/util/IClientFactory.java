package sweng_plus.framework.networking.util;

import sweng_plus.framework.networking.interfaces.IClient;

public interface IClientFactory<C extends IClient>
{
    C makeClient(String ip, NetworkRole role, ClientStatus status);
    
    default C makeClient(String ip)
    {
        return makeClient(ip, NetworkRole.CLIENT, ClientStatus.CONNECTED);
    }
    
    default C makeHost()
    {
        return makeClient("host", NetworkRole.HOST, ClientStatus.CONNECTED);
    }
}
