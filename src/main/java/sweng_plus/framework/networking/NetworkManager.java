package sweng_plus.framework.networking;

import java.io.IOException;

public class NetworkManager
{
    public static IHostManager host(MessageRegistry registry, int port) throws IOException
    {
        HostManager hostManager = new HostManager(registry, port);
        Thread thread = new Thread(hostManager);
        thread.start();
        return hostManager;
    }
    
    public static IClientManager connect(MessageRegistry registry, String ip, int port) throws IOException
    {
        ClientManager clientManager = new ClientManager(registry, ip, port);
        Thread thread = new Thread(clientManager);
        thread.start();
        return clientManager;
    }
}
