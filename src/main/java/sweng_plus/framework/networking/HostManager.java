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
        
        mainThreadMessages.getUnsafe().add(() -> eventsListener.clientConnected(hostClient));
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
                if(c.getStatus() == ClientStatus.CONNECTED)
                {
                    sendMessageToClient(c, message);
                }
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
                
                String ip = socket.getRemoteSocketAddress().toString();
                C client = clientFactory.makeClient(ip);
                
                addClient(connection, client);
                
                mainThreadMessages.exclusive(mainThreadMessages1 ->
                {
                    mainThreadMessages1.add(() -> eventsListener.clientConnected(client));
                });
                
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
    
    public void addClient(Connection<C> connection, C client)
    {
        Lock lock = clientsListLock.writeLock();
        try
        {
            lock.lock();
            clientsList.add(client);
            client.changeStatus(ClientStatus.CONNECTED);
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
    }
    
    public C removeClientByThread()
    {
        Lock lock = threadClientMapLock.writeLock();
        try
        {
            lock.lock();
            return threadClientMap.remove(Thread.currentThread());
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void removeClient(C client)
    {
        Lock lock = clientConnectionMapLock.writeLock();
        try
        {
            lock.lock();
            clientConnectionMap.remove(client);
        }
        finally
        {
            lock.unlock();
        }
        
        lock = clientsListLock.writeLock();
        try
        {
            lock.lock();
            clientsList.remove(client);
        }
        finally
        {
            lock.unlock();
        }
        
        client.changeStatus(ClientStatus.DISCONNECTED);
    }
    
    @Override
    public void connectionSocketClosed() // Connection Thread
    {
        C client = removeClientByThread();
        removeClient(client);
        
        mainThreadMessages.exclusive(mainThreadMessages1 ->
        {
            mainThreadMessages1.add(() -> eventsListener.clientSocketClosed(client));
        });
    }
    
    @Override
    public void connectionSocketClosedWithException(Exception e) // Connection Thread
    {
        C client = removeClientByThread();
        removeClient(client);
        
        mainThreadMessages.exclusive(mainThreadMessages1 ->
        {
            mainThreadMessages1.add(() -> eventsListener.clientSocketClosedWithException(client, e));
        });
    }
    
    @Override
    public void close() // Main Thread
    {
        // close = true setzen
        
        super.close();
        
        System.out.println("close2");
        
        // auf den connection thread warten
        // dieser sollte terminieren, sobald shouldClose == true ist
        
        List<Thread> threads;
        
        Lock lock = threadClientMapLock.readLock();
        try
        {
            lock.lock();
            threads = threadClientMap.keySet().stream().toList();
        }
        finally
        {
            lock.unlock();
        }
        
        for(Thread t : threads)
        {
            while(true)
            {
                try
                {
                    System.out.println("Joining");
                    t.join();
                    System.out.println("Joined!");
                    break;
                }
                catch(InterruptedException ignored) {}
            }
        }
        
        System.out.println("Joined all!");
        
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
