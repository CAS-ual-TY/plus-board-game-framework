package sweng_plus.framework.networking;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.function.Supplier;

public class Connection implements Runnable
{
    public Supplier<Socket> socket;
    public IConnectionInteractor connectionInteractor;
    
    public CircularBuffer buffer;
    
    public Connection(Supplier<Socket> socket, IConnectionInteractor connectionInteractor)
    {
        this.socket = socket;
        this.connectionInteractor = connectionInteractor;
        
        buffer = new CircularBuffer();
    }
    
    @Override
    public void run() // Connection Thread
    {
        // kann entweder Client -> Server socket sein (einfach der Socket)
        // oder ein serverSocket.accept() call
        Socket socket = this.socket.get();
        
        try
        {
            InputStream in = socket.getInputStream();
            
            while(!connectionInteractor.shouldClose())
            {
                buffer.writeBytes(in.readAllBytes()); // TODO blockiert das wirklich?
                
                while(buffer.size() > Short.BYTES && buffer.size() >= buffer.peekShort())
                {
                    connectionInteractor.receivedMessage(connectionInteractor.getMessageRegistry().decodeMessage(buffer));
                }
            }
        }
        catch(IOException e)
        {
            // TODO callback an IConnectionInteractor ?
            e.printStackTrace();
        }
    }
}
