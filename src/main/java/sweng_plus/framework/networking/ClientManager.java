package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IClientEventsListener;
import sweng_plus.framework.networking.interfaces.IClientManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.function.Consumer;

public class ClientManager<C extends IClient> extends ConnectionInteractor<C> implements IClientManager
{
    public IClientEventsListener eventsListener;
    
    public Socket socket;
    
    public OutputStream out;
    public CircularBuffer writeBuffer;
    
    public Thread thread;
    
    public ClientManager(IMessageRegistry<C> registry, IClientEventsListener eventsListener, String ip, int port) throws IOException
    {
        super(registry);
        this.eventsListener = eventsListener;
        socket = new Socket(ip, port);
        out = socket.getOutputStream();
        writeBuffer = new CircularBuffer();
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        thread = new Thread(new Connection<>((connection) -> socket, this));
        thread.start();
        
        super.run();
    }
    
    @Override
    public <M> void sendMessageToServerUnsafe(M message) throws IOException // Main Thread
    {
        if(!socket.isClosed())
        {
            getMessageRegistry().encodeMessage(writeBuffer, message);
            writeBuffer.writeToOutputStream(out);
        }
    }
    
    @Override
    public void update() // Main Thread
    {
        if(!shouldClose())
        {
            super.runMessages();
        }
    }
    
    @Override
    public void connectionSocketClosed() // Connection Thread
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
        {
            mainThreadMessages1.clear();
            mainThreadMessages1.add(eventsListener::socketClosed);
        });
    }
    
    @Override
    public void connectionSocketClosedWithException(Exception e) // Connection Thread
    {
        mainThreadMessages.exclusiveGet(mainThreadMessages1 ->
        {
            mainThreadMessages1.clear();
            mainThreadMessages1.add(() -> eventsListener.socketClosedWithException(e));
        });
    }
    
    @Override
    public void close() // Main Thread
    {
        try
        {
            socket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void getClientForConnThread(Thread thread, Consumer<Optional<C>> consumer)
    {
        consumer.accept(Optional.empty());
    }
    
    @Override
    public boolean shouldClose()
    {
        return socket.isClosed();
    }
}
