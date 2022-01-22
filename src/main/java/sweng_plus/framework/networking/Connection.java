package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IConnectionInteractor;
import sweng_plus.framework.networking.util.CircularBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Connection<C extends IClient> implements Runnable
{
    public SocketSupplier<C> socketSupplier;
    public IConnectionInteractor<C> connectionInteractor;
    
    public CircularBuffer readBuffer;
    public CircularBuffer writeBuffer;
    
    // kann entweder Client -> Server socket sein (einfach der Socket)
    // oder ein serverSocket.accept() call
    public Socket socket;
    public OutputStream out;
    
    public Connection(SocketSupplier<C> socketSupplier, IConnectionInteractor<C> connectionInteractor)
    {
        this.socketSupplier = socketSupplier;
        this.connectionInteractor = connectionInteractor;
        
        readBuffer = new CircularBuffer();
        writeBuffer = new CircularBuffer();
    }
    
    @Override
    public void run() // Connection Thread
    {
        try
        {
            socket = socketSupplier.makeOrGetSocket(this);
            
            if(socket == null)
            {
                return;
            }
            
            connectionInteractor.connectionSocketCreated();
            
            socket.setSoTimeout(100);
            out = socket.getOutputStream();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        try
        {
            InputStream in = socket.getInputStream();
            
            while(!connectionInteractor.shouldClose() && !socket.isClosed())
            {
                try
                {
                    readBuffer.readFrominputStream(in);
                    
                    while(connectionInteractor.getMessageRegistry().canDecodeMessage(readBuffer))
                    {
                        connectionInteractor.getMessageRegistry().decodeMessage(readBuffer, (msg, uMsgPosition, handler) ->
                                connectionInteractor.receivedMessage(msg, uMsgPosition, handler)
                        );
                    }
                }
                catch(SocketTimeoutException ignored) {}
            }
            
            connectionInteractor.connectionSocketClosed();
        }
        catch(IOException e)
        {
            connectionInteractor.connectionSocketClosedWithException(e);
        }
    }
    
    public <M> void sendMessage(M msg) throws IOException
    {
        if(!socket.isClosed())
        {
            connectionInteractor.getMessageRegistry().encodeMessage(writeBuffer, msg, (byte) 0); // TODO uMsgPos
            writeBuffer.writeToOutputStream(out);
        }
    }
    
    public interface SocketSupplier<C extends IClient>
    {
        Socket makeOrGetSocket(Connection<C> connection) throws IOException;
    }
}
