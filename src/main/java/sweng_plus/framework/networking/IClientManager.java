package sweng_plus.framework.networking;

import java.io.Closeable;
import java.util.function.Consumer;

public interface IClientManager extends Closeable
{
    <M> void sendPacketToServer(M m); // Main Thread
    
    void runPackets(Consumer<Runnable> consumer); // Main Thread
    
    @Override
    void close(); // Main Thread
}
