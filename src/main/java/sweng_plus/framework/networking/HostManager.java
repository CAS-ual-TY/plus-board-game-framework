package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IHostEventsListener;
import sweng_plus.framework.networking.interfaces.IHostManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class HostManager<C extends IClient> extends ConnectionInteractor<C> implements IHostManager<C>
{
    protected IHostEventsListener<C> eventsListener;
    protected IClientFactory<C> clientFactory;
    
    protected ServerSocket serverSocket;
    
    protected C hostClient;
    
    protected LockedObject<LinkedList<C>> clientsList;
    protected LockedObject<HashMap<Thread, C>> threadClientMap;
    protected LockedObject<HashMap<C, Connection<C>>> clientConnectionMap;
    
    protected CircularBuffer writeBuffer;
    
    public HostManager(IMessageRegistry<C> registry, IHostEventsListener<C> eventsListener, IClientFactory<C> clientFactory, int port) throws IOException
    {
        super(registry);
        this.eventsListener = eventsListener;
        this.clientFactory = clientFactory;
        
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(100);
    
        hostClient = clientFactory.makeHost();
        hostClient.setStatus(ClientStatus.CONNECTED);
        
        clientsList = new LockedObject<>(new LinkedList<>());
        threadClientMap = new LockedObject<>(new HashMap<>());
        clientConnectionMap = new LockedObject<>(new HashMap<>());
        
        writeBuffer = new CircularBuffer();
        
        clientsList.getUnsafe().add(hostClient);
        mainThreadMessages.getUnsafe().add(() -> clientConnected(hostClient));
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        listenToNewConnection();
        
        super.run();
    }
    
    public void listenToNewConnection() // NetworkManager Thread or Connection Thread
    {
        new Thread(new Connection<>(this::acceptNewConnection, this)).start();
    }
    
    @Override
    public List<C> getAllClients() // Main Thread
    {
        try
        {
            clientsList.readLock().lock();
            return clientsList.getUnsafe();
        }
        finally
        {
            clientsList.readLock().unlock();
        }
    }
    
    @Override
    public C getHostClient() // Main Thread
    {
        return hostClient;
    }
    
    @Override
    public <M> void sendMessageToClientUnsafe(C client, M message) throws IOException // Main Thread
    {
        if(client == getHostClient())
        {
            getMessageRegistry().getHandlerForMessage(message).handleMessage(Optional.empty(), message);
        }
        else
        {
            clientConnectionMap.sharedIO(clientConnectionMap1 ->
            {
                Connection<C> connection = clientConnectionMap1.get(client);
                
                if(!connection.socket.isClosed())
                {
                    getMessageRegistry().encodeMessage(writeBuffer, message);
                    writeBuffer.writeToOutputStream(connection.out);
                }
            });
        }
    }
    
    @Override
    public <M> void sendMessageToServerUnsafe(M message) // Main Thread
    {
        // Wird vom Client der auch Host ist gecallt
        // Deshalb kanns auch direkt ausgeführt werden
        getMessageRegistry().getHandlerForMessage(message).handleMessage(Optional.of(getHostClient()), message);
    }
    
    @Override
    public void update() // Main Thread
    {
        super.runMessages();
    }
    
    public Socket acceptNewConnection(Connection<C> connection) // Connection Thread
    {
        while(!shouldClose())
        {
            try
            {
                // Blockt
                // Wirft SocketTimeoutException falls nicht erfolgreich
                Socket socket = serverSocket.accept();
                
                // Connection reingekommen => Auf neue Connection hören
                listenToNewConnection();
                
                String ip = socket.getRemoteSocketAddress().toString();
                C client = clientFactory.makeClient(ip);
    
                clientConnectionMap.exclusiveGet(clientConnectionMap1 ->
                        clientConnectionMap1.put(client, connection));
    
                threadClientMap.exclusiveGet(threadClientMap1 ->
                        threadClientMap1.put(Thread.currentThread(), client));
                
                addClient(connection, client);
                
                return socket;
            }
            catch(SocketTimeoutException ignored) {}
            catch(SocketException e)
            {
                // Thrown when the server socket is closed
                return null;
            }
            catch(IOException e)
            {
                // Connection ist fehlgeschlagen => Auf neue Connection hören
                listenToNewConnection();
                
                e.printStackTrace();
                return null;
            }
        }
        
        return null;
    }
    
    @Override
    public void connectionSocketCreated()
    {
        Thread t = Thread.currentThread();
        threadClientMap.shared(threadClientMap1 ->
                runOnMainThreadSafely(() -> clientConnected(threadClientMap1.get(t))));
    }
    
    protected void clientConnected(C client)
    {
        client.setStatus(ClientStatus.CONNECTED);
        eventsListener.clientConnected(client);
    }
    
    public void addClient(Connection<C> connection, C client)
    {
        clientsList.exclusiveGet(clientsList1 ->
                clientsList1.add(client));
    }
    
    public void removeClientByThread(Consumer<C> consumer)
    {
        threadClientMap.exclusiveGet(threadClientMap1 ->
                consumer.accept(threadClientMap1.remove(Thread.currentThread())));
    }
    
    public void removeClient(C client)
    {
        clientConnectionMap.exclusiveGet(clientConnectionMap1 ->
                clientConnectionMap1.remove(client));
        
        clientsList.exclusiveGet(clientsList1 ->
                clientsList1.remove(client));
    }
    
    @Override
    public void connectionSocketClosed() // Connection Thread
    {
        removeClientByThread(client ->
        {
            removeClient(client);
            
            mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
                    mainThreadMessages1.add(() ->
                    {
                        client.setStatus(ClientStatus.DISCONNECTED);
                        eventsListener.clientSocketClosed(client);
                    }));
        });
    }
    
    @Override
    public void connectionSocketClosedWithException(Exception e) // Connection Thread
    {
        removeClientByThread(client ->
        {
            removeClient(client);
            
            mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
                    mainThreadMessages1.add(() -> eventsListener.clientSocketClosedWithException(client, e)));
        });
    }
    
    @Override
    public void closeClient(C client)
    {
        if(client.getRole() == NetworkRole.HOST)
        {
            close();
        }
        else
        {
            try
            {
                clientConnectionMap.sharedIO(clientConnectionMap1 ->
                        clientConnectionMap1.get(client).socket.close());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    protected void getClientForConnThread(Thread thread, Consumer<Optional<C>> consumer)
    {
        threadClientMap.shared(threadClientMap1 -> consumer.accept(Optional.of(threadClientMap1.get(thread))));
    }
    
    @Override
    public void close() // Main Thread
    {
        try
        {
            serverSocket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean shouldClose()
    {
        return serverSocket.isClosed();
    }
}
