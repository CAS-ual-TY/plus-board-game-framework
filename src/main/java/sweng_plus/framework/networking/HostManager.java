package sweng_plus.framework.networking;

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
    
    protected ReentrantReadWriteLock clientThreadMapLock;
    public HashMap<Client, Thread> clientThreadMap;
    
    public HostManager(MessageRegistry registry, int port) throws IOException
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
        
        clientThreadMapLock = new ReentrantReadWriteLock();
        clientThreadMap = new HashMap<>();
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        // TODO start thread
        
        super.run();
    }
    
    @Override
    public List<? extends IClient> getAllClients()
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
    public IClient getHostClient()
    {
        return hostClient;
    }
    
    @Override
    public <M> void sendPacketToClient(IClient client, M m) // Main Thread
    {
        if(client.getRole() == NetworkRole.HOST)
        {
            getMessageRegistry().getHandlerForMessage(m).handleMessage(m);
        }
        else
        {
            // TODO
        }
    }
    
    @Override
    public <M> void sendPacketToAllClients(M m) // Main Thread
    {
        Lock lock = clientsListLock.readLock();
        
        try
        {
            lock.lock();
            
            for(Client c : clientsList)
            {
                sendPacketToClient(c, m);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public <M> void sendPacketToServer(M m) // Main Thread
    {
        // Wird vom Client der auch Host ist gecallt
        // Deshalb kanns auch direkt ausgeführt werden
        getMessageRegistry().getHandlerForMessage(m).handleMessage(m);
    }
    
    @Override
    public void runPackets(Consumer<Runnable> consumer) // Main Thread
    {
        super.runPackets(consumer);
    }
    
    public Socket acceptNewConnection() // Connection Thread
    {
        while(!shouldClose())
        {
            try
            {
                // Blockt
                // Wirft SocketTimeoutException falls nicht erfolgreich
                Socket socket = serverSocket.accept();
                
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
                
                lock = clientThreadMapLock.writeLock();
                try
                {
                    lock.lock();
                    clientThreadMap.put(client, Thread.currentThread());
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
                
                // Auf neue Connection hören
                new Thread(new Connection(this::acceptNewConnection, this)).start();
                
                return socket;
            }
            catch(SocketTimeoutException ignored) {}
            catch(IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        
        return null;
    }
    
    @Override
    public void socketClosed() // Connection Thread
    {
        // TODO
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
        
        lock = clientThreadMapLock.writeLock();
        try
        {
            lock.lock();
            clientThreadMap.remove(client);
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
        // TODO
        socketClosed();
    }
    
    @Override
    public void close() // Main Thread
    {
        // close = true setzen
        
        super.close();
        
        // auf den connection thread warten
        // dieser sollte terminieren, sobald shouldClose == true ist
        
        while(true)
        {
            //try
            //{
            // TODO Allen Threads joinen
            break;
            //}
            //catch(InterruptedException ignored) {}
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
