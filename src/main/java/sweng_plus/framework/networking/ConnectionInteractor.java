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
    
    protected LockedObject<List<Runnable>> mainThreadMessages;
    protected LockedObject<List<Runnable>> connectionThreadMessages;
    
    public ConnectionInteractor(IMessageRegistry<C> registry)
    {
        this.registry = registry;
        
        mainThreadMessages = new LockedObject<>(new ArrayList<>(16));
        connectionThreadMessages = new LockedObject<>(new ArrayList<>(16));
    }
    
    @Override
    public void run()
    {
        while(!shouldClose())
        {
            movePackets();
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
        List<Runnable> list = new ArrayList<>(16);
        
        mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
        {
            list.addAll(mainThreadMessages1);
            mainThreadMessages1.clear();
        });
        
        for(Runnable r : list)
        {
            r.run();
        }
    }
    
    @Override
    public void connectionSocketCreated()
    {
    
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
    
    public void runOnMainThreadSafely(Runnable r)
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages1 -> mainThreadMessages1.add(r));
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
}
