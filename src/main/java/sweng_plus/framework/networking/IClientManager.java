package sweng_plus.framework.networking;

import java.util.function.Consumer;

public interface IClientManager
{
    <M> void sendPacketToServer(M m); // Main Thread
    
    void runPackets(Consumer<Runnable> consumer); // Main Thread
    
    void close(); // Main Thread
}
