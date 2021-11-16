package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IConnectionInteractor;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable
{
    public SocketSupplier socketSuppler;
    public IConnectionInteractor connectionInteractor;
    
    public CircularBuffer readBuffer;
    
    // kann entweder Client -> Server socket sein (einfach der Socket)
    // oder ein serverSocket.accept() call
    public Socket socket;
    public OutputStream out;
    
    public Connection(SocketSupplier socketSuppler, IConnectionInteractor connectionInteractor)
    {
        this.socketSuppler = socketSuppler;
        this.connectionInteractor = connectionInteractor;
        
        readBuffer = new CircularBuffer();
    }
    
    @Override
    public void run() // Connection Thread
    {
        try
        {
            socket = socketSuppler.makeOrGetSocket(this);
            out = socket.getOutputStream();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
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
    
    public interface SocketSupplier
    {
        Socket makeOrGetSocket(Connection connection) throws IOException;
    }
}
