package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.function.Consumer;

public class ClientManager<C extends IClient> extends ConnectionInteractor<C> implements IClientManager<C>
{
    protected IClientEventsListener eventsListener;
    
    protected Socket socket;
    
    protected OutputStream out;
    protected CircularBuffer writeBuffer;
    
    protected Connection<C> connection;
    protected Thread thread;
    
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
        thread = new Thread(connection = new Connection<>((connection) -> socket, this));
        thread.start();
        
        super.run();
    }
    
    @Override
    public <M> void sendMessageToServerUnsafe(M message) throws IOException // Main Thread
    {
        connection.sendMessage(message);
    }
    
    @Override
    public void update() // Main Thread
    {
        super.runMessages();
    }
    
    @Override
    public <M> void receivedMessage(M msg, IMessageHandler<M, C> handler)
    {
        connectionThreadMessages.exclusiveGet(connectionThreadMessages1 ->
                connectionThreadMessages1.add(() -> handler.handleMessage(Optional.empty(), msg)));
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
    protected void getClientForConnThread(Thread thread, Consumer<C> consumer)
    {
        consumer.accept(null);
    }
    
    @Override
    public boolean shouldClose()
    {
        return socket.isClosed();
    }
}
