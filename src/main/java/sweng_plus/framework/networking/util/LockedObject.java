package sweng_plus.framework.networking.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class LockedObject<O>
{
    protected final O object;
    protected final ReentrantReadWriteLock lock;
    
    public LockedObject(O object)
    {
        this.object = object;
        lock = new ReentrantReadWriteLock();
    }
    
    public void shared(Consumer<O> consumer)
    {
        try
        {
            lock.readLock().lock();
            consumer.accept(object);
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public void exclusive(Consumer<O> consumer)
    {
        try
        {
            lock.writeLock().lock();
            consumer.accept(object);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }
}
