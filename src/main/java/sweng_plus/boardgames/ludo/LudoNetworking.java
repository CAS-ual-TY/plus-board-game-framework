package sweng_plus.boardgames.ludo;

import sweng_plus.boardgames.ludo.gamelogic.networking.*;
import sweng_plus.boardgames.ludo.gui.LobbyScreen;
import sweng_plus.framework.networking.AdvancedMessageRegistry;
import sweng_plus.framework.networking.NetworkHelper;
import sweng_plus.framework.networking.interfaces.*;

import java.io.IOException;

public class LudoNetworking implements IAdvancedClientEventsListener, IAdvancedHostEventsListener<LudoClient>
{
    public final Ludo ludo;
    
    public IAdvancedMessageRegistry<LudoClient> protocol;
    
    public IAdvancedClientManager clientManager;
    public IAdvancedHostManager<LudoClient> hostManager;
    
    public LudoNetworking(Ludo ludo)
    {
        this.ludo = ludo;
        initProtocol();
    }
    
    public IAdvancedClientManager getClientManager()
    {
        return clientManager;
    }
    
    public IAdvancedHostManager<LudoClient> getHostManager()
    {
        return hostManager;
    }
    
    protected void initProtocol()
    {
        byte messageID = 0;
        protocol = new AdvancedMessageRegistry<>(32, messageID++, messageID++, messageID++, messageID++,
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
        
        hostManager = null;
        ludo.names.clear();
        clientManager = NetworkHelper.advancedConnect(protocol, this, playerName, ip, port);
        
        ludo.setScreen(new LobbyScreen(ludo));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public void host(String playerName, int port) throws IOException
    {
        System.out.println("host");
        
        ludo.names.clear();
        hostManager = NetworkHelper.advancedHost(protocol, this, LudoClient::new, playerName, port);
        clientManager = hostManager;
        
        ludo.setScreen(new LobbyScreen(ludo));
        
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
    public void clientAuthSuccessful(LudoClient client)
    {
        client.setTeamIndex(hostManager.getAllClients().size() - 1);
    }
}
