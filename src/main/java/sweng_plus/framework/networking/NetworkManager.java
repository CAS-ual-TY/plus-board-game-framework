package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IClientManager;
import sweng_plus.framework.networking.interfaces.IHostManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;

import java.io.IOException;

public class NetworkManager
{
    /**
     * <p>Starts a server socket and returns an {@link IHostManager} object for interacting with clients.
     * The {@link IHostManager} allows you to obtain a list of {@link IClient} which represent all clients that have
     * ever connected to this server including the host itself.
     * These {@link IClient} object are used for interaction.</p>
     *
     * <p>The {@link IHostManager} object extends {@link IClientManager} to also simulate
     * a client connected to a server. So it also allows usage as client for sending messages to the server
     * and receiving messages from the server.</p>
     *
     * <p>In the case of this server sending messages to the host client or vice versa, the messages are not encoded
     * but simply run immediately.</p>
     *
     * @param registry The {@link MessageRegistry}, basically a representation of the used protocol which must be
     *                 the same on both client and server.
     * @param port     The port to host on.
     * @return An {@link IHostManager} object for interacting with clients.
     * @throws IOException
     */
    public static IHostManager host(IMessageRegistry registry, int port) throws IOException
    {
        HostManager hostManager = new HostManager(registry, port);
        Thread thread = new Thread(hostManager);
        thread.start();
        return hostManager;
    }
    
    /**
     * Connects to a server socket and returns an {@link IClientManager} object for sending and receiving messages.
     *
     * @param registry The {@link MessageRegistry}, basically a representation of the used protocol which must be
     *                 the same on both client and server.
     * @param ip       The IP to connect to.
     * @param port     The port to connect to.
     * @return An {@link IClientManager} object for interacting with the server.
     * @throws IOException
     */
    public static IClientManager connect(IMessageRegistry registry, String ip, int port) throws IOException
    {
        ClientManager clientManager = new ClientManager(registry, ip, port);
        Thread thread = new Thread(clientManager);
        thread.start();
        return clientManager;
    }
}
