package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IHostEventsListener;
import sweng_plus.framework.networking.interfaces.IHostManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.IClientFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HostManager<C extends IClient> extends ConnectionInteractor<C> implements IHostManager<C>
{
    public IHostEventsListener<C> eventsListener;
    public IClientFactory<C> clientFactory;
    
    public ServerSocket serverSocket;
    
    public C hostClient;
    
    protected ReentrantReadWriteLock clientsListLock;
    // Enthält alle Clients, auch die, die disconnected sind oder gekickt wurden
    public LinkedList<C> clientsList;
    
    protected ReentrantReadWriteLock threadClientMapLock;
    public HashMap<Thread, C> threadClientMap;
    
    protected ReentrantReadWriteLock clientConnectionMapLock;
    public HashMap<C, Connection<C>> clientConnectionMap;
    
    public CircularBuffer writeBuffer;
    
    public HostManager(IMessageRegistry<C> registry, IHostEventsListener<C> eventsListener, IClientFactory<C> clientFactory, int port) throws IOException
    {
        super(registry);
        this.eventsListener = eventsListener;
        this.clientFactory = clientFactory;
        
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(100);
        
        hostClient = clientFactory.makeHost();
        
        clientsListLock = new ReentrantReadWriteLock();
        clientsList = new LinkedList<>();
        clientsList.add(hostClient);
        
        threadClientMapLock = new ReentrantReadWriteLock();
        threadClientMap = new HashMap<>();
        
        clientConnectionMapLock = new ReentrantReadWriteLock();
        clientConnectionMap = new HashMap<>();
        
        writeBuffer = new CircularBuffer();
        
        mainThreadMessages.add(() -> eventsListener.clientConnected(hostClient));
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
        Lock lock = clientsListLock.readLock();
        
        try
        {
            lock.lock();
            return clientsList;
        }
        finally
        {
            // TODO wie siehts aus, wenn man über getAllClients direkt drüber-iteriert? Lock funktioniert?
            lock.unlock();
        }
    }
    
    @Override
    public C getHostClient() // Main Thread
    {
        return hostClient;
    }
    
    @Override
    public <M> void sendMessageToClient(C client, M message) throws IOException // Main Thread
    {
        if(client == getHostClient())
        {
            getMessageRegistry().getHandlerForMessage(message).handleMessage(Optional.empty(), message);
        }
        else
        {
            Connection<C> connection;
            
            Lock lock = clientConnectionMapLock.readLock();
            
            try
            {
                lock.lock();
                connection = clientConnectionMap.get(client);
            }
            finally
            {
                lock.unlock();
            }
            
            if(!connection.socket.isClosed())
            {
                getMessageRegistry().encodeMessage(writeBuffer, message);
                writeBuffer.writeToOutputStream(connection.out);
            }
        }
    }
    
    @Override
    public <M> void sendMessageToAllClients(M message) throws IOException // Main Thread
    {
        Lock lock = clientsListLock.readLock();
        
        try
        {
            lock.lock();
            
            for(C c : clientsList)
            {
                sendMessageToClient(c, message);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public <M> void sendMessageToServer(M message) // Main Thread
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
                
                C client;
                String ip = socket.getRemoteSocketAddress().toString();
                
                Lock lock = clientsListLock.writeLock();
                try
                {
                    lock.lock();
                    
                    client = clientsList.stream().filter(c -> c.getIP().equals(ip)).findFirst().orElse(clientFactory.makeClient(ip));
                    
                    if(!clientsList.contains(client))
                    {
                        clientsList.add(client);
                    }
                    else
                    {
                        client.changeStatus(ClientStatus.CONNECTED);
                    }
                }
                finally
                {
                    lock.unlock();
                }
                
                lock = clientConnectionMapLock.writeLock();
                try
                {
                    lock.lock();
                    clientConnectionMap.put(client, connection);
                }
                finally
                {
                    lock.unlock();
                }
                
                lock = threadClientMapLock.writeLock();
                try
                {
                    lock.lock();
                    threadClientMap.put(Thread.currentThread(), client);
                }
                finally
                {
                    lock.unlock();
                }
                
                mainThreadMessages.add(() -> eventsListener.clientConnected(client));
                
                return socket;
            }
            catch(SocketTimeoutException ignored) {}
            catch(SocketException e)
            {
                if(!e.getMessage().equals("Socket closed"))
                {
                    e.printStackTrace();
                }
                
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
    public void connectionSocketClosed() // Connection Thread
    {
        // no need to do all this cleanup as it's done anyways since the server is closed
        if(shouldClose())
        {
            return;
        }
        
        C client;
        
        Lock lock = threadClientMapLock.writeLock();
        try
        {
            lock.lock();
            client = threadClientMap.remove(Thread.currentThread());
        }
        finally
        {
            lock.unlock();
        }
        
        lock = clientConnectionMapLock.writeLock();
        try
        {
            lock.lock();
            clientConnectionMap.remove(client);
        }
        finally
        {
            lock.unlock();
        }
        
        client.changeStatus(ClientStatus.DISCONNECTED);
        
        // Callback to Engine
        
        lock = mainThreadMessagesLock.writeLock();
        
        try
        {
            lock.lock();
            mainThreadMessages.add(() -> eventsListener.clientDisconnected(client));
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public void connectionSocketClosedWithException(Exception e) // Connection Thread
    {
        e.printStackTrace();
        connectionSocketClosed();
    }
    
    @Override
    public void close() // Main Thread
    {
        // close = true setzen
        
        super.close();
        
        // auf den connection thread warten
        // dieser sollte terminieren, sobald shouldClose == true ist
        
        Lock lock = threadClientMapLock.readLock();
        
        try
        {
            lock.lock();
            
            for(Thread t : threadClientMap.keySet())
            {
                while(true)
                {
                    try
                    {
                        t.join();
                        break;
                    }
                    catch(InterruptedException ignored) {}
                }
            }
        }
        finally
        {
            lock.unlock();
        }
        
        // socket schließen
        
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
    protected Optional<C> getClientForConnThread(Thread thread)
    {
        return Optional.of(threadClientMap.get(thread));
    }
    
    @Override
    public boolean shouldClose()
    {
        return serverSocket.isClosed() || super.shouldClose();
    }
}
