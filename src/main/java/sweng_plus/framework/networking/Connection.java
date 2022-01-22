package sweng_plus.framework.networking;

import sweng_plus.framework.networking.interfaces.IClient;
import sweng_plus.framework.networking.interfaces.IConnectionInteractor;
import sweng_plus.framework.networking.util.CircularBuffer;
import sweng_plus.framework.networking.util.MessageTracker;
import sweng_plus.framework.networking.util.TrackedMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Comparator;

public class Connection<C extends IClient> implements Runnable
{
    public SocketSupplier<C> socketSupplier;
    public IConnectionInteractor<C> connectionInteractor;
    
    public ArrayList<TrackedMessage<?, C>> trackedMessages;
    public Comparator<TrackedMessage<?, C>> trackedMessageComparator;
    
    public CircularBuffer readBuffer;
    public CircularBuffer writeBuffer;
    
    public MessageTracker readTracker;
    public MessageTracker writeTracker;
    
    // kann entweder Client -> Server socket sein (einfach der Socket)
    // oder ein serverSocket.accept() call
    public Socket socket;
    public OutputStream out;
    
    public Connection(SocketSupplier<C> socketSupplier, IConnectionInteractor<C> connectionInteractor)
    {
        this.socketSupplier = socketSupplier;
        this.connectionInteractor = connectionInteractor;
        
        trackedMessages = new ArrayList<>(16);
        
        readBuffer = new CircularBuffer();
        writeBuffer = new CircularBuffer();
        
        readTracker = new MessageTracker();
        writeTracker = new MessageTracker();
        
        trackedMessageComparator = readTracker.makeComparator();
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
                                trackedMessages.add(new TrackedMessage<>(msg, handler, uMsgPosition))
                        );
                    }
                    
                    trackedMessages.sort(trackedMessageComparator);
                    
                    while(!trackedMessages.isEmpty())
                    {
                        if(handleReceivedMessage(trackedMessages.remove(0)))
                        {
                            readTracker.increment();
                        }
                        else
                        {
                            break;
                        }
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
    
    protected <M> boolean handleReceivedMessage(TrackedMessage<M, C> msg)
    {
        if(msg.getPosition() == readTracker.get())
        {
            connectionInteractor.receivedMessage(msg.msg(), msg.handler());
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public <M> void sendMessage(M msg) throws IOException
    {
        if(!socket.isClosed())
        {
            connectionInteractor.getMessageRegistry().encodeMessage(writeBuffer, msg, writeTracker.getByteThenIncrement());
            writeBuffer.writeToOutputStream(out);
        }
    }
    
    public interface SocketSupplier<C extends IClient>
    {
        Socket makeOrGetSocket(Connection<C> connection) throws IOException;
    }
}
