package sweng_plus.framework.networking.util;

import java.io.IOException;
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
    
    public void write(O value)
    {
        exclusiveSet(o -> value);
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
    
    public void sharedIO(IOConsumer<O> getter) throws IOException
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
    
    public void exclusiveGet(Consumer<O> getter)
    {
        try
        {
            writeLock().lock();
            getter.accept(object);
        }
        finally
        {
            writeLock().unlock();
        }
    }
    
    public void exclusiveGetIO(IOConsumer<O> getter) throws IOException
    {
        try
        {
            writeLock().lock();
            getter.accept(object);
        }
        finally
        {
            writeLock().unlock();
        }
    }
    
    public void exclusiveSet(Function<O, O> setter)
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
    
    public void exclusiveSetIO(IOFunction<O, O> setter) throws IOException
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
    
    public interface IOConsumer<T>
    {
        void accept(T t) throws IOException;
    }
    
    public interface IOFunction<T, R>
    {
        R apply(T t) throws IOException;
    }
}
