package sweng_plus.framework.networking;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientManager extends ConnectionInteractor implements IClientManager
{
    public Socket socket;
    
    public Thread thread;
    
    public ClientManager(MessageRegistry registry, String ip, int port) throws IOException
    {
        super(registry);
        socket = new Socket(ip, port);
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        thread = new Thread(new Connection(() -> socket, this));
        thread.start();
        
        super.run();
    }
    
    @Override
    public <M> void sendPacketToServer(M m) // Main Thread
    {
        //socket.getOutputStream().
    }
    
    @Override
    public void runPackets(Consumer<Runnable> consumer) // Main Thread
    {
        super.runPackets(consumer);
    }
    
    @Override
    public void socketClosed() // Connection Thread
    {
    
    }
    
    @Override
    public void socketClosedWithException(Exception e) // Connection Thread
    {
    
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
}
