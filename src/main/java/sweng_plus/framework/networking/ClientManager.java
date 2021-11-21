package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IClientManager;
import sweng_plus.framework.networking.interfaces.IMessageRegistry;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.function.Consumer;

public class ClientManager extends ConnectionInteractor implements IClientManager
{
    public Socket socket;
    
    public OutputStream out;
    public CircularBuffer writeBuffer;
    
    public Thread thread;
    
    public ClientManager(IMessageRegistry registry, String ip, int port) throws IOException
    {
        super(registry);
        socket = new Socket(ip, port);
        out = socket.getOutputStream();
        writeBuffer = new CircularBuffer();
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        thread = new Thread(new Connection((connection) -> socket, this));
        thread.start();
        
        super.run();
    }
    
    @Override
    public <M> void sendMessageToServer(M message) throws IOException // Main Thread
    {
        getMessageRegistry().encodeMessage(writeBuffer, message);
        writeBuffer.writeToOutputStream(out);
    }
    
    @Override
    public void runMessages(Consumer<Runnable> consumer) // Main Thread
    {
        super.runPackets(consumer);
    }
    
    @Override
    public void socketClosed() // Connection Thread
    {
        // TODO callback zur Engine?
    }
    
    @Override
    public void socketClosedWithException(Exception e) // Connection Thread
    {
        e.printStackTrace();
        socketClosed();
    }
    
    @Override
    public void close() // Main Thread
    {
        // close = true setzen
        
        super.close();
        
        // auf den connection thread warten
        // dieser sollte terminieren, sobald shouldClose == true ist
        
        while(true)
        {
            try
            {
                thread.join();
                break;
            }
            catch(InterruptedException ignored) {}
        }
        
        // socket schließen
        
        try
        {
            socket.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected Optional<IClient> getClientForConnThread(Thread thread)
    {
        return Optional.empty();
    }
}
