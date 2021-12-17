package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IConnectionInteractor;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.LockedObject;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class ConnectionInteractor<C extends IClient> implements IConnectionInteractor<C>, Runnable, Closeable
{
    public IMessageRegistry<C> registry;
    
    protected LockedObject<Boolean> close;
    
    protected LockedObject<List<Runnable>> mainThreadMessages;
    protected LockedObject<List<Runnable>> connectionThreadMessages;
    
    public ConnectionInteractor(IMessageRegistry<C> registry)
    {
        this.registry = registry;
        
        close = new LockedObject<>(false);
        
        mainThreadMessages = new LockedObject<>(new ArrayList<>(16));
        connectionThreadMessages = new LockedObject<>(new ArrayList<>(16));
    }
    
    @Override
    public void run()
    {
        try
        {
            close.readLock().lock();
            
            while(!close.getUnsafe())
            {
                close.readLock().unlock();
                
                movePackets();
                
                close.readLock().lock();
            }
        }
        finally
        {
            close.readLock().unlock();
        }
    }
    
    public void movePackets() // Network Manager Thread
    {
        connectionThreadMessages.exclusiveGet(connectionThreadMessages1 ->
                mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
                {
                    mainThreadMessages1.addAll(connectionThreadMessages1);
                    connectionThreadMessages1.clear();
                }));
    }
    
    public void runMessages()
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
        {
            for(Runnable r : mainThreadMessages1)
            {
                r.run();
            }
            
            mainThreadMessages1.clear();
        });
    }
    
    @Override
    public void receivedMessage(Consumer<Optional<C>> message)
    {
        getClientForConnThread(Thread.currentThread(), client -> receivedMessage(message, client));
    }
    
    public void receivedMessage(Consumer<Optional<C>> message, Optional<C> client)
    {
        connectionThreadMessages.exclusiveGet(connectionThreadMessages1 ->
                connectionThreadMessages1.add(() -> message.accept(client)));
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
    
    protected abstract void getClientForConnThread(Thread thread, Consumer<Optional<C>> consumer);
    
    @Override
    public boolean shouldClose()
    {
        try
        {
            close.readLock().lock();
            return close.getUnsafe();
        }
        finally
        {
            close.readLock().unlock();
        }
    }
    
    @Override
    public void close()
    {
        close.write(true);
    }
}
