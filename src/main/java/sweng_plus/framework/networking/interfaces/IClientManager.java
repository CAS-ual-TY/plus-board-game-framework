package sweng_plus.framework.networking.interfaces;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface IClientManager extends Closeable
{
    <M> void sendPacketToServer(M m) throws IOException; // Main Thread
    
    void runPackets(Consumer<Runnable> consumer); // Main Thread
    
    @Override
    void close(); // Main Thread
}
