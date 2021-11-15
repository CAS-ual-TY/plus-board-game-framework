package sweng_plus.framework.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class HostManager extends ConnectionInteractor implements IHostManager
{
    private ServerSocket socket;
    
    public HostManager(MessageRegistry registry, int port) throws IOException
    {
        super(registry);
        socket = new ServerSocket(port);
    }
    
    @Override
    public void run() // Network Manager Thread
    {
        // TODO start thread
        
        super.run();
    }
    
    @Override
    public <M> void sendPacketToClient(IClient client, M m) // Main Thread
    {
    
    }
    
    @Override
    public <M> void sendPacketToAllClients(M m) // Main Thread
    {
    
    }
    
    @Override
    public <M> void sendPacketToServer(M m) // Main Thread
    {
    
    }
    
    @Override
    public void runPackets(Consumer<Runnable> consumer) // Main Thread
    {
        super.runPackets(consumer);
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
            //try
            //{
            // TODO Allen Threads joinen
            break;
            //}
            //catch(InterruptedException ignored) {}
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
