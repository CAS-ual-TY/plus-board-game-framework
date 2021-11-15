package sweng_plus.framework.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class HostManager implements Runnable, IHostManager, IConnectionInteractor
{
    private MessageRegistry registry;
    private ServerSocket socket;
    
    private ReentrantReadWriteLock closeLock;
    private boolean close;
    
    private ReentrantReadWriteLock mainLock;
    private List<Runnable> runnablesForMainThread;
    
    private ReentrantReadWriteLock connectionsLock;
    private List<Runnable> runnablesFromConnectionThreads;
    
    public HostManager(MessageRegistry registry, int port) throws IOException
    {
        this.registry = registry;
        socket = new ServerSocket(port);
        
        closeLock = new ReentrantReadWriteLock();
        close = false;
        
        mainLock = new ReentrantReadWriteLock();
        runnablesForMainThread = new ArrayList<>(32);
        
        connectionsLock = new ReentrantReadWriteLock();
        runnablesFromConnectionThreads = new ArrayList<>(32);
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        ReentrantReadWriteLock.ReadLock lock = closeLock.readLock();
        
        try
        {
            while(!close)
            {
                lock.unlock();
                
                movePackets();
                
                lock = closeLock.readLock();
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void movePackets() // Network Manager Thread
    {
        Lock lock1 = connectionsLock.writeLock();
        
        try
        {
            Lock lock2 = mainLock.writeLock();
            
            try
            {
                runnablesForMainThread.addAll(runnablesFromConnectionThreads);
                runnablesFromConnectionThreads.clear();
            }
            finally
            {
                lock2.unlock();
            }
        }
        finally
        {
            lock1.unlock();
        }
    }
    
    @Override
    public <M> void sendPacketToServer(M m) // Main Thread
    {
    
    }
    
    @Override
    public void runPackets(Consumer<Runnable> consumer) // Main Thread
    {
        Lock lock = mainLock.writeLock();
        
        try
        {
            for(Runnable r : runnablesForMainThread)
            {
                consumer.accept(r);
            }
            
            runnablesForMainThread.clear();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public void close() // Main Thread
    {
    
    }
    
    @Override
    public <M> void sendPacketToClient(IClient client, M m) // Main Thread
    {
    
    }
    
    @Override
    public <M> void sendPacketToAllClients(M m) // Main Thread
    {
    
    }
    
    @Override
    public void receivedMessage(Runnable r) // Connection Thread
    {
        Lock lock = connectionsLock.writeLock();
        
        try
        {
            runnablesFromConnectionThreads.add(r);
        }
        finally
        {
            lock.unlock();
        }
    }
}
