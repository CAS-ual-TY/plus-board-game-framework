package sweng_plus.framework.networking.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

public class LockedObject<O>
{
    protected O object;
    protected final ReentrantReadWriteLock lock;
    
    public LockedObject(O object)
    {
        this.object = object;
        lock = new ReentrantReadWriteLock();
    }
    
    public O getUnsafe()
    {
        return object;
    }
    
    public void setUnsafe(O value)
    {
        object = value;
    }
    
    public Lock readLock()
    {
        return lock.readLock();
    }
    
    public Lock writeLock()
    {
        return lock.writeLock();
    }
    
    public O read()
    {
        return shared(o -> o);
    }
    
    public void write(O value)
    {
        exclusive(o -> value);
    }
    
    public void shared(Consumer<O> getter)
    {
        try
        {
            readLock().lock();
            getter.accept(object);
        }
        finally
        {
            readLock().unlock();
        }
    }
    
    public <A> A shared(Function<O, A> returnFunction)
    {
        try
        {
            readLock().lock();
            return returnFunction.apply(object);
        }
        finally
        {
            writeLock().unlock();
        }
    }
    
    public void exclusive(Function<O, O> setter)
    {
        try
        {
            writeLock().lock();
            object = setter.apply(object);
        }
        finally
        {
            writeLock().unlock();
        }
    }
    
    public <A> A exclusive(Function<O, O> setter, Function<O, A> returnFunction)
    {
        try
        {
            writeLock().lock();
            object = setter.apply(object);
            return returnFunction.apply(object);
        }
        finally
        {
            writeLock().unlock();
        }
    }
}
