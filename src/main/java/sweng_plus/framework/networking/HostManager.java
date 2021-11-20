package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IHostManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.ClientStatus;
import sweng_plus.framework.networking.util.NetworkRole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class HostManager extends ConnectionInteractor implements IHostManager
{
    public ServerSocket serverSocket;
    
    public Client hostClient;
    
    protected ReentrantReadWriteLock clientsListLock;
    // Enthält alle Clients, auch die, die disconnected sind oder gekickt wurden
    public LinkedList<Client> clientsList;
    
    protected ReentrantReadWriteLock threadClientMapLock;
    public HashMap<Thread, Client> threadClientMap;
    
    protected ReentrantReadWriteLock clientConnectionMapLock;
    public HashMap<IClient, Connection> clientConnectionMap;
    
    public CircularBuffer writeBuffer;
    
    public HostManager(IMessageRegistry registry, int port) throws IOException
    {
        super(registry);
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout((int) TimeUnit.MILLISECONDS.toMillis(100));
        
        hostClient = Client.makeHost();
        
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
        new Thread(new Connection(this::acceptNewConnection, this)).start();
    }
    
    @Override
    public List<? extends IClient> getAllClients() // Main Thread
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
    public IClient getHostClient() // Main Thread
    {
        return hostClient;
    }
    
    @Override
    public <M> void sendMessageToClient(IClient client, M message) throws IOException // Main Thread
    {
        if(client.getRole() == NetworkRole.HOST)
        {
            getMessageRegistry().getHandlerForMessage(message).handleMessage(message);
        }
        else
        {
            Connection connection;
            
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
            
            for(Client c : clientsList)
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
        getMessageRegistry().getHandlerForMessage(message).handleMessage(message);
    }
    
    @Override
    public void runMessages(Consumer<Runnable> consumer) // Main Thread
    {
        super.runPackets(consumer);
    }
    
    public Socket acceptNewConnection(Connection connection) // Connection Thread
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
                
                Client client;
                String ip = socket.getRemoteSocketAddress().toString();
                
                Lock lock = clientsListLock.writeLock();
                try
                {
                    lock.lock();
                    
                    client = clientsList.stream().filter(c -> c.getIP().equals(ip)).findFirst().orElse(new Client(ip));
                    
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
        
        Client client;
        
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
}
