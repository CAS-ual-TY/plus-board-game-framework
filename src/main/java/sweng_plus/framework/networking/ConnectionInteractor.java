package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IConnectionInteractor;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public abstract class ConnectionInteractor<C extends IClient> implements IConnectionInteractor<C>, Runnable, Closeable
{
    public IMessageRegistry<C> registry;
    
    protected ReentrantReadWriteLock closeLock;
    protected boolean close;
    
    protected ReentrantReadWriteLock mainThreadMessagesLock;
    protected List<Runnable> mainThreadMessages;
    
    protected ReentrantReadWriteLock connectionThreadMessagesLock;
    protected List<Runnable> connectionThreadMessages;
    
    public ConnectionInteractor(IMessageRegistry<C> registry)
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
    
    public void runMessages()
    {
        Lock lock = mainThreadMessagesLock.writeLock();
        
        try
        {
            lock.lock();
            
            for(Runnable r : mainThreadMessages)
            {
                r.run();
            }
            
            mainThreadMessages.clear();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public void receivedMessage(Consumer<Optional<C>> message)
    {
        Optional<C> client = getClientForConnThread(Thread.currentThread());
        
        Lock lock = connectionThreadMessagesLock.writeLock();
        
        try
        {
            lock.lock();
            connectionThreadMessages.add(() -> message.accept(client));
        }
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public IMessageRegistry<C> getMessageRegistry()
    {
        return registry;
    }
    
    @Override
    public abstract void connectionSocketClosed();
    
    @Override
    public abstract void connectionSocketClosedWithException(Exception e);
    
    protected abstract Optional<C> getClientForConnThread(Thread thread);
    
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
