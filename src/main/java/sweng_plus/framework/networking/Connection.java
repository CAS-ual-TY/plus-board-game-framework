package sweng_plus.framework.networking;

import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.function.Supplier;

public class Connection implements Runnable
{
    public Supplier<Socket> socketSuppler;
    public IConnectionInteractor connectionInteractor;
    
    public CircularBuffer readBuffer;
    
    public Connection(Supplier<Socket> socketSuppler, IConnectionInteractor connectionInteractor)
    {
        this.socketSuppler = socketSuppler;
        this.connectionInteractor = connectionInteractor;
        
        readBuffer = new CircularBuffer();
    }
    
    @Override
    public void run() // Connection Thread
    {
        // kann entweder Client -> Server socket sein (einfach der Socket)
        // oder ein serverSocket.accept() call
        Socket socket = socketSuppler.get();
        
        try
        {
            InputStream in = socket.getInputStream();
            
            while(!connectionInteractor.shouldClose())
            {
                readBuffer.writeBytes(in.readAllBytes()); // TODO blockiert das wirklich?
                
                while(readBuffer.size() > Short.BYTES && readBuffer.size() >= readBuffer.peekShort())
                {
                    connectionInteractor.receivedMessage(connectionInteractor.getMessageRegistry().decodeMessage(readBuffer));
                }
            }
            
            connectionInteractor.socketClosed();
        }
        catch(IOException e)
        {
            connectionInteractor.socketClosedWithException(e);
        }
    }
}
