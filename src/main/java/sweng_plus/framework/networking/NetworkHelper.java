package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.util.ClientSessionManager;
import sweng_plus.framework.networking.util.HostSessionManager;
import sweng_plus.framework.networking.util.IClientFactory;

import java.io.IOException;

public class NetworkHelper
{
    /**
     * <p>Starts a server socket and returns an {@link IHostManager} object for interacting with clients.
     * The {@link IHostManager} allows you to obtain a list of {@link IClient} which represent all clients that have
     * ever connected to this server including the host itself.
     * These {@link IClient} object are used for interaction.</p>
     *
     * <p>Messages received are automatically ran the same order they were sent in.</p>
     *
     * <p>The {@link IHostManager} object extends {@link IClientManager} to also simulate
     * a client connected to a server. So it also allows usage as client for sending messages to the server
     * and receiving messages from the server.</p>
     *
     * <p>In the case of this server sending messages to the host client or vice versa, the messages are not encoded
     * but simply run immediately.</p>
     *
     * @param registry      The used message protocol which must be the same on both client and server.
     * @param clientFactory A factory creating instances of {@link IClient}.
     * @param port          The port to host on.
     * @param <C>           The type extending {@link IClient} used for representing clients.
     * @return An {@link IHostManager} object for interacting with clients.
     * @throws IOException
     * @see #advancedHost(IAdvancedMessageRegistry, IAdvancedHostEventsListener, IClientFactory, String, int)
     */
    public static <C extends IClient> IHostManager<C> host(IMessageRegistry<C> registry,
                                                           IHostEventsListener<C> eventsListener,
                                                           IClientFactory<C> clientFactory, int port) throws IOException
    {
        HostManager<C> hostManager = new HostManager<>(registry, eventsListener, clientFactory, port);
        Thread thread = new Thread(hostManager);
        thread.start();
        return hostManager;
    }
    
    /**
     * <p>Starts a server socket and returns an {@link IAdvancedHostManager} object for interacting with clients.
     * The {@link IAdvancedHostManager} allows you to obtain a list of {@link IAdvancedClient} which represent all
     * clients are connected to this server including the host itself.
     * These {@link IAdvancedClient} object are used for interaction.</p>
     *
     * <p>Messages received are automatically ran the same order they were sent in.</p>
     *
     * <p>Additionally, this implementation contains a built-in advanced protocol to detect if a client has lost connection,
     * simple authentication and capabilities to orderly disconnect or kick clients.
     * Also, the server is orderly closed (all clients are orderly disconnected) when closing the connection.</p>
     *
     * <p>The {@link IAdvancedHostManager} object extends {@link IAdvancedClientManager} to also simulate
     * a client connected to a server. So it also allows usage as client for sending messages to the server
     * and receiving messages from the server.</p>
     *
     * <p>In the case of this server sending messages to the host client or vice versa, the messages are not encoded
     * but simply run immediately.</p>
     *
     * @param registry      The used message protocol which must be the same on both client and server.
     * @param clientFactory A factory creating instances of {@link IAdvancedClient}.
     * @param clientName    The host-client's name.
     * @param port          The port to host on.
     * @param <C>           The type extending {@link IAdvancedClient} used for representing clients.
     * @return An {@link IAdvancedHostManager} object for interacting with clients.
     * @throws IOException
     */
    public static <C extends IAdvancedClient> IAdvancedHostManager<C> advancedHost(IAdvancedMessageRegistry<C> registry,
                                                                                   IAdvancedHostEventsListener<C> eventsListener,
                                                                                   IClientFactory<C> clientFactory,
                                                                                   String clientName, int port) throws IOException
    {
        AdvancedHostManager<C> hostManager = new AdvancedHostManager<>(registry, eventsListener, clientFactory, new HostSessionManager(), clientName, port);
        Thread thread = new Thread(hostManager);
        thread.start();
        return hostManager;
    }
    
    /**
     * <p>Connects to a server socket and returns an {@link IClientManager} object for sending and receiving messages.</p>
     *
     * <p>Messages received are automatically ran the same order they were sent in.</p>
     *
     * @param registry       The used message protocol which must be the same on both client and server.
     * @param eventsListener A listener for any client-networking related events.
     * @param ip             The IP to connect to.
     * @param port           The port to connect to.
     * @param <C>            The type extending {@link IClient} used for representing clients.
     * @return An {@link IClientManager} object for interacting with the server.
     * @throws IOException
     * @see #advancedConnect(IAdvancedMessageRegistry, IAdvancedClientEventsListener, String, String, int)
     */
    public static <C extends IClient> IClientManager connect(IMessageRegistry<C> registry,
                                                             IClientEventsListener eventsListener,
                                                             String ip, int port) throws IOException
    {
        ClientManager<C> clientManager = new ClientManager<>(registry, eventsListener, ip, port);
        Thread thread = new Thread(clientManager);
        thread.start();
        return clientManager;
    }
    
    /**
     * <p>Connects to a server socket and returns an {@link IAdvancedClientManager} object for sending and receiving messages.</p>
     *
     * <p>Messages received are automatically ran the same order they were sent in.</p>
     *
     * <p>Contains a built-in advanced protocol to detect if the server has been closed, the client has been kicked,
     * or the connection has been lost. Also, the client is orderly disconnected when closing the connection.</p>
     *
     * @param registry       The used message protocol which must be the same on both client and server.
     * @param eventsListener A listener for any client-networking related events.
     * @param name           The client's name.
     * @param ip             The IP to connect to.
     * @param port           The port to connect to.
     * @param <C>            The type extending {@link IAdvancedClient} used for representing clients.
     * @return An {@link IAdvancedClientManager} object for interacting with the server.
     * @throws IOException
     */
    public static <C extends IAdvancedClient> IAdvancedClientManager advancedConnect(IAdvancedMessageRegistry<C> registry,
                                                                                     IAdvancedClientEventsListener eventsListener,
                                                                                     String name, String ip, int port) throws IOException
    {
        ClientSessionManager clientSessionManager = new ClientSessionManager();
        AdvancedClientManager<C> clientManager = new AdvancedClientManager<>(registry, eventsListener, clientSessionManager, name, ip, port)
        {
            @Override
            public void close()
            {
                super.close();
                
                try
                {
                    clientSessionManager.writeToFile();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(clientManager);
        thread.start();
        return clientManager;
    }
}
