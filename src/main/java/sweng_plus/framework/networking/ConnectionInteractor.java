package sweng_plus.framework.networking;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class ConnectionInteractor implements IConnectionInteractor, Runnable, Closeable
{
    public MessageRegistry registry;
    
    protected ReentrantReadWriteLock closeLock;
    protected boolean close;
    
    protected ReentrantReadWriteLock mainThreadMessagesLock;
    protected List<Runnable> mainThreadMessages;
    
    protected ReentrantReadWriteLock connectionThreadMessagesLock;
    protected List<Runnable> connectionThreadMessages;
    
    public ConnectionInteractor(MessageRegistry registry)
    {
        this.registry = registry;
        
        closeLock = new ReentrantReadWriteLock();
        close = false;
        
        mainThreadMessagesLock = new ReentrantReadWriteLock();
        mainThreadMessages = new ArrayList<>(64);
        
        connectionThreadMessagesLock = new ReentrantReadWriteLock();
        connectionThreadMessages = new ArrayList<>(64);
    }
    
    @Override
    public void run()
    {
        ReentrantReadWriteLock.ReadLock lock = closeLock.readLock();
        
        try
        {
            lock.lock();
            
            while(!close)
            {
                lock.unlock();
                
                movePackets();
                
                lock.lock();
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void movePackets() // Network Manager Thread
    {
        Lock lock1 = connectionThreadMessagesLock.writeLock();
        Lock lock2 = mainThreadMessagesLock.writeLock();
        
        try
        {
            lock1.lock();
            
            try
            {
                lock2.lock();
                
                mainThreadMessages.addAll(connectionThreadMessages);
                connectionThreadMessages.clear();
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
    
    public void runPackets(Consumer<Runnable> consumer)
    {
        for(Runnable r : mainThreadMessages)
        {
            consumer.accept(r);
        }
        
        mainThreadMessages.clear();
    }
    
    @Override
    public void receivedMessage(Runnable message)
    {
        Lock lock = connectionThreadMessagesLock.writeLock();
        
        try
        {
            lock.lock();
            connectionThreadMessages.add(message);
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public MessageRegistry getMessageRegistry()
    {
        return registry;
    }
    
    @Override
    public boolean shouldClose()
    {
        Lock lock = closeLock.readLock();
        
        try
        {
            lock.lock();
            return close;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public void close()
    {
        Lock lock = closeLock.writeLock();
        
        try
        {
            lock.lock();
            close = true;
        }
        finally
        {
            lock.unlock();
        }
    }
}
