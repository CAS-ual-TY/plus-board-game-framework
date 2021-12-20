package sweng_plus.boardgames.ludo;

import sweng_plus.boardgames.ludo.gamelogic.networking.*;
import sweng_plus.boardgames.ludo.gui.NameScreen;
import sweng_plus.framework.networking.AdvancedMessageRegistry;
import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.networking.interfaces.*;

import java.io.IOException;

public class LudoNetworking implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<LudoClient>
{
    public final Ludo ludo;
    
    public IMessageRegistry<LudoClient> protocol;
    
    public String name;
    
    public IClientManager clientManager;
    public IHostManager<LudoClient> hostManager;
    
    public LudoNetworking(Ludo ludo)
    {
        this.ludo = ludo;
        initProtocol();
    }
    
    public IClientManager getClientManager()
    {
        return clientManager;
    }
    
    public IHostManager<LudoClient> getHostManager()
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
        
        protocol.registerMessage(messageID++, ChatMessage.Handler::encodeMessage,
                ChatMessage.Handler::decodeMessage, ChatMessage.Handler::handleMessage,
                ChatMessage.class);
        
        protocol.registerMessage(messageID++, StartGameMessage.Handler::encodeMessage,
                StartGameMessage.Handler::decodeMessage, StartGameMessage.Handler::handleMessage,
                StartGameMessage.class);
        
        protocol.registerMessage(messageID++, RollMessage.Handler::encodeMessage,
                RollMessage.Handler::decodeMessage, RollMessage.Handler::handleMessage,
                RollMessage.class);
        
        protocol.registerMessage(messageID++, RolledMessage.Handler::encodeMessage,
                RolledMessage.Handler::decodeMessage, RolledMessage.Handler::handleMessage,
                RolledMessage.class);
        
        protocol.registerMessage(messageID++, FigureSelectMessage.Handler::encodeMessage,
                FigureSelectMessage.Handler::decodeMessage, FigureSelectMessage.Handler::handleMessage,
                FigureSelectMessage.class);
        
        protocol.registerMessage(messageID++, FigureSelectedMessage.Handler::encodeMessage,
                FigureSelectedMessage.Handler::decodeMessage, FigureSelectedMessage.Handler::handleMessage,
                FigureSelectedMessage.class);
        
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
        ludo.names.clear();
        clientManager = NetworkHelper.connect(protocol, this, ip, port);
        
        ludo.setScreen(new NameScreen(ludo));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public void host(String playerName, int port) throws IOException
    {
        System.out.println("host");
        
        name = playerName;
        
        ludo.names.clear();
        hostManager = NetworkHelper.host(protocol, this, LudoClient::new, port);
        clientManager = hostManager;
        
        ludo.setScreen(new NameScreen(ludo));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public boolean isHost()
    {
        return hostManager != null;
    }
    
    @Override
    public void clientDisconnectedOrderly(LudoClient client)
    {
    
    }
    
    @Override
    public void clientConnected(LudoClient client)
    {
        client.setTeamIndex(hostManager.getAllClients().size() - 1);
        
        if(hostManager.getAllClients().size() >= 2) //TODO START
        {
            ludo.startGame(true, hostManager.getAllClients().size());
        }
    }
}
