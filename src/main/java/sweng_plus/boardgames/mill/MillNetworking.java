package sweng_plus.boardgames.mill;

import sweng_plus.boardgames.mill.gamelogic.networking.MillClient;
import sweng_plus.boardgames.mill.gamelogic.networking.SendNameMessage;
import sweng_plus.boardgames.mill.gamelogic.networking.SendNamesMessage;
import sweng_plus.boardgames.mill.gamelogic.networking.StartGameMessage;
import sweng_plus.boardgames.mill.gui.NameScreen;
import sweng_plus.framework.networking.AdvancedMessageRegistry;
import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.networking.interfaces.*;

import java.io.IOException;

public class MillNetworking implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<MillClient>
{
    public final Mill mill;
    
    public IMessageRegistry<MillClient> protocol;
    
    public String name;
    
    public IClientManager clientManager;
    public IHostManager<MillClient> hostManager;
    
    public MillNetworking(Mill mill)
    {
        this.mill = mill;
        initProtocol();
    }
    
    public IClientManager getClientManager()
    {
        return clientManager;
    }
    
    public IHostManager<MillClient> getHostManager()
    {
        return hostManager;
    }
    
    protected void initProtocol()
    {
        byte messageID = 0;
        protocol = new AdvancedMessageRegistry<>(32, messageID++, messageID++, messageID++,
                this::getClientManager, this::getHostManager,
                this, this);
    
        protocol.registerMessage(messageID++, SendNameMessage.Handler::encodeMessage,
                SendNameMessage.Handler::decodeMessage, SendNameMessage.Handler::handleMessage,
                SendNameMessage.class);
    
        protocol.registerMessage(messageID++, SendNamesMessage.Handler::encodeMessage,
                SendNamesMessage.Handler::decodeMessage, SendNamesMessage.Handler::handleMessage,
                SendNamesMessage.class);
    
        protocol.registerMessage(messageID++, StartGameMessage.Handler::encodeMessage,
                StartGameMessage.Handler::decodeMessage, StartGameMessage.Handler::handleMessage,
                StartGameMessage.class);
    }
    
    public void cleanup()
    {
        if(clientManager != null)
        {
            clientManager.close();
        }
    }
    
    public void update()
    {
        if(clientManager != null)
        {
            clientManager.update();
        }
    }
    
    public void connect(String playerName, String ip, int port) throws IOException
    {
        System.out.println("connect");
        
        name = playerName;
        
        hostManager = null;
        mill.names.clear();
        clientManager = NetworkHelper.connect(protocol, this, ip, port);
        
        mill.setScreen(new NameScreen(mill));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public void host(String playerName, int port) throws IOException
    {
        System.out.println("host");
        
        name = playerName;
        
        mill.names.clear();
        hostManager = NetworkHelper.host(protocol, this, MillClient::new, port);
        clientManager = hostManager;
        
        mill.setScreen(new NameScreen(mill));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public boolean isHost()
    {
        return hostManager != null;
    }
    
    @Override
    public void clientDisconnectedOrderly(MillClient client)
    {
    
    }
    
    @Override
    public void clientConnected(MillClient client)
    {
        client.setTeamIndex(hostManager.getAllClients().size() - 1);
        
        if(hostManager.getAllClients().size() >= 2) //TODO START
        {
            mill.startGame(true, hostManager.getAllClients().size());
        }
    }
    
}
