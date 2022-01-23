package sweng_plus.boardgames.mill;

import sweng_plus.boardgames.mill.gamelogic.networking.*;
import sweng_plus.boardgames.mill.gui.NameScreen;
import sweng_plus.framework.networking.AdvancedMessageRegistry;
import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.networking.interfaces.*;

import java.io.IOException;

public class MillNetworking implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<MillClient>
{
    public final Mill mill;
    
    public IAdvancedMessageRegistry<MillClient> protocol;
    
    public String name;
    
    public IAdvancedClientManager<MillClient> clientManager;
    public IAdvancedHostManager<MillClient> hostManager;
    
    public MillNetworking(Mill mill)
    {
        this.mill = mill;
        initProtocol();
    }
    
    public IAdvancedClientManager<MillClient> getClientManager()
    {
        return clientManager;
    }
    
    public IAdvancedHostManager<MillClient> getHostManager()
    {
        return hostManager;
    }
    
    protected void initProtocol()
    {
        byte messageID = 0;
        protocol = new AdvancedMessageRegistry<>(32, messageID++, messageID++, messageID++, messageID++,
                this::getClientManager, this::getHostManager,
                this, this);
        
        protocol.registerMessage(messageID++, SendNamesMessage.Handler::encodeMessage,
                SendNamesMessage.Handler::decodeMessage, SendNamesMessage.Handler::handleMessage,
                SendNamesMessage.class);
        
        protocol.registerMessage(messageID++, StartGameMessage.Handler::encodeMessage,
                StartGameMessage.Handler::decodeMessage, StartGameMessage.Handler::handleMessage,
                StartGameMessage.class);
        
        protocol.registerMessage(messageID++, TellServerFigureNodeSelectedMessage.Handler::encodeMessage,
                TellServerFigureNodeSelectedMessage.Handler::decodeMessage, TellServerFigureNodeSelectedMessage.Handler::handleMessage,
                TellServerFigureNodeSelectedMessage.class);
        
        protocol.registerMessage(messageID++, FigureNodeSelectedMessage.Handler::encodeMessage,
                FigureNodeSelectedMessage.Handler::decodeMessage, FigureNodeSelectedMessage.Handler::handleMessage,
                FigureNodeSelectedMessage.class);
        
        protocol.registerMessage(messageID++, TellServerFigureTakenMessage.Handler::encodeMessage,
                TellServerFigureTakenMessage.Handler::decodeMessage, TellServerFigureTakenMessage.Handler::handleMessage,
                TellServerFigureTakenMessage.class);
        
        protocol.registerMessage(messageID++, FigureTakenMessage.Handler::encodeMessage,
                FigureTakenMessage.Handler::decodeMessage, FigureTakenMessage.Handler::handleMessage,
                FigureTakenMessage.class);
        
        protocol.registerMessage(messageID++, WinMessage.Handler::encodeMessage,
                WinMessage.Handler::decodeMessage, WinMessage.Handler::handleMessage,
                WinMessage.class);
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
        clientManager = NetworkHelper.advancedConnect(protocol, this, name, ip, port);
        
        mill.setScreen(new NameScreen(mill));
    }
    
    public void host(String playerName, int port) throws IOException
    {
        System.out.println("host");
        
        name = playerName;
        
        mill.names.clear();
        hostManager = NetworkHelper.advancedHost(protocol, this, MillClient::new, name, port);
        clientManager = hostManager;
        
        mill.setScreen(new NameScreen(mill));
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
    public void clientAuthSuccessful(MillClient client)
    {
        client.setTeamIndex(hostManager.getAllClients().size() - 1);
        
        hostManager.sendMessageToAllClients(SendNamesMessage.makeNamesMessage());
        
        if(hostManager.getAllClients().size() >= 2) //TODO START
        {
            mill.startGame(true, hostManager.getAllClients().size());
        }
    }
    
}
