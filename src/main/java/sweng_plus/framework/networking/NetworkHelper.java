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
     * <p>The {@link IHostManager} object extends {@link IClientManager} to also simulate
     * a client connected to a server. So it also allows usage as client for sending messages to the server
     * and receiving messages from the server.</p>
     *
     * <p>In the case of this server sending messages to the host client or vice versa, the messages are not encoded
     * but simply run immediately.</p>
     *
     * @param registry      The {@link MessageRegistry}, basically a representation of the used protocol which must be
     *                      the same on both client and server.
     * @param clientFactory A factory creating instances of {@link IClient}.
     * @param port          The port to host on.
     * @param <C>           The type extending {@link IClient} used for representing clients.
     * @return An {@link IHostManager} object for interacting with clients.
     * @throws IOException
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
    
    public static <C extends IAdvancedClient> IAdvancedHostManager<C> advancedHost(IAdvancedMessageRegistry<C> registry,
                                                                                   IAdvancedHostEventsListener<C> eventsListener,
                                                                                   IClientFactory<C> clientFactory,
                                                                                   IHostSessionManager sessionManager,
                                                                                   String clientName, int port) throws IOException
    {
        AdvancedHostManager<C> hostManager = new AdvancedHostManager<>(registry, eventsListener, clientFactory, sessionManager, clientName, port);
        Thread thread = new Thread(hostManager);
        thread.start();
        return hostManager;
    }
    
    public static <C extends IAdvancedClient> IAdvancedHostManager<C> advancedHost(IAdvancedMessageRegistry<C> registry,
                                                                                   IAdvancedHostEventsListener<C> eventsListener,
                                                                                   IClientFactory<C> clientFactory,
                                                                                   String clientName, int port) throws IOException
    {
        return advancedHost(registry, eventsListener, clientFactory, new HostSessionManager(), clientName, port);
    }
    
    /**
     * Connects to a server socket and returns an {@link IClientManager} object for sending and receiving messages.
     *
     * @param registry The {@link MessageRegistry}, basically a representation of the used protocol which must be
     *                 the same on both client and server.
     * @param ip       The IP to connect to.
     * @param port     The port to connect to.
     * @param <C>      The type extending {@link IClient} used for representing clients.
     * @return An {@link IClientManager} object for interacting with the server.
     * @throws IOException
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
    
    public static <C extends IAdvancedClient> IAdvancedClientManager advancedConnect(IAdvancedMessageRegistry<C> registry,
                                                                                     IAdvancedClientEventsListener eventsListener,
                                                                                     IClientSessionManager sessionManager,
                                                                                     String name, String ip, int port) throws IOException
    {
        AdvancedClientManager<C> clientManager = new AdvancedClientManager<>(registry, eventsListener, sessionManager, name, ip, port);
        Thread thread = new Thread(clientManager);
        thread.start();
        return clientManager;
    }
    
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
