package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IHostManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.IClientFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class HostManager<C extends IClient> extends ConnectionInteractor<C> implements IHostManager<C>
{
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
    
    public HostManager(IMessageRegistry<C> registry, IClientFactory<C> clientFactory, int port) throws IOException
    {
        super(registry);
        this.clientFactory = clientFactory;
        
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout((int) TimeUnit.MILLISECONDS.toMillis(100));
        
        hostClient = clientFactory.makeHost();
        
        clientsListLock = new ReentrantReadWriteLock();
        clientsList = new LinkedList<>();
        clientsList.add(hostClient);
        
        threadClientMapLock = new ReentrantReadWriteLock();
        threadClientMap = new HashMap<>();
        
        clientConnectionMapLock = new ReentrantReadWriteLock();
        clientConnectionMap = new HashMap<>();
        
        writeBuffer = new CircularBuffer();
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
            getMessageRegistry().getHandlerForMessage(message).handleMessage(Optional.of(getHostClient()), message);
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
            
            getMessageRegistry().encodeMessage(writeBuffer, message);
            
            writeBuffer.writeToOutputStream(connection.out);
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
    public void runMessages(Consumer<Runnable> consumer) // Main Thread
    {
        super.runMessages(consumer);
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
                
                return socket;
            }
            catch(SocketTimeoutException ignored) {}
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
    public void socketClosed() // Connection Thread
    {
        // TODO callback zur Engine?
        
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
    }
    
    @Override
    public void socketClosedWithException(Exception e) // Connection Thread
    {
        e.printStackTrace();
        socketClosed();
    }
    
    @Override
    public void close() // Main Thread
    {
        // close = true setzen
        
        super.close();
        
        // auf den connection thread warten
        // dieser sollte terminieren, sobald shouldClose == true ist
        
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
        
        // socket schließen
        
        try
        {
            serverSocket.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected Optional<C> getClientForConnThread(Thread thread)
    {
        return Optional.of(threadClientMap.get(thread));
    }
}
