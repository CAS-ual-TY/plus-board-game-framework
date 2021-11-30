package sweng_plus.boardgames.ludo;

import org.lwjgl.glfw.GLFW;
import sweng_plus.boardgames.ludo.gamelogic.LudoGameLogic;
import sweng_plus.boardgames.ludo.gamelogic.networking.LudoClient;
import sweng_plus.boardgames.ludo.gamelogic.networking.SendNameMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.SendNamesMessage;
import sweng_plus.boardgames.ludo.gamelogic.networking.StartGameMessage;
import sweng_plus.boardgames.ludo.gui.LudoTextures;
import sweng_plus.boardgames.ludo.gui.MenuScreen;
import sweng_plus.boardgames.ludo.gui.NameScreen;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.networking.MessageRegistry;
import sweng_plus.framework.networking.NetworkManager;
import sweng_plus.framework.networking.interfaces.*;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontHelper;
import sweng_plus.framework.userinterface.gui.font.FontInfo;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Ludo implements IGame, IClientEventsListener, IHostEventsListener<LudoClient>
{
    private static Ludo instance;
    
    private Screen screen;
    
    public FontRenderer fontRenderer64;
    public FontRenderer fontRenderer48;
    public FontRenderer fontRenderer32;
    public FontRenderer fontRenderer24;
    public FontRenderer fontRenderer16;
    
    public LudoGameLogic gameLogic;
    public ArrayList<String> names;
    
    IMessageRegistry<LudoClient> protocol;
    
    public String name;
    
    public IClientManager<LudoClient> clientManager;
    public IHostManager<LudoClient> hostManager;
    
    public Ludo()
    {
        //noinspection ThisEscapedInObjectConstruction
        instance = this;
        
        gameLogic = null;
        names = new ArrayList<>();
    }
    
    public static Ludo instance()
    {
        return instance;
    }
    
    public LudoGameLogic getGameLogic()
    {
        return gameLogic;
    }
    
    public IClientManager<LudoClient> getClientManager()
    {
        return clientManager;
    }
    
    public IHostManager<LudoClient> getHostManager()
    {
        return hostManager;
    }
    
    protected void initProtocol()
    {
        protocol = new MessageRegistry<>(32);
        byte messageID = 0;
        
        protocol.registerMessage(messageID++, SendNameMessage.Handler::encodeMessage,
                SendNameMessage.Handler::decodeMessage, SendNameMessage.Handler::handleMessage,
                SendNameMessage.class);
        
        protocol.registerMessage(messageID++, SendNamesMessage.Handler::encodeMessage,
                SendNamesMessage.Handler::decodeMessage, SendNamesMessage.Handler::handleMessage,
                SendNamesMessage.class);
        
        protocol.registerSimpleMessage(messageID++, StartGameMessage::handleMessage, new StartGameMessage());
    }
    
    public void connect(String playerName, String ip, int port) throws IOException
    {
        System.out.println("connect");
        
        name = playerName;
        
        hostManager = null;
        names.clear();
        clientManager = NetworkManager.connect(protocol, this, ip, port);
        
        setScreen(new NameScreen(this));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public void host(String playerName, int port) throws IOException
    {
        System.out.println("host");
        
        name = playerName;
        
        names.clear();
        hostManager = NetworkManager.host(protocol, this, LudoClient::new, port);
        clientManager = hostManager;
        
        setScreen(new NameScreen(this));
        
        clientManager.sendMessageToServer(new SendNameMessage(playerName));
    }
    
    public boolean isHost()
    {
        return hostManager != null;
    }
    
    @Override
    public void disconnected()
    {
    
    }
    
    @Override
    public void disconnectedWithException(Exception e)
    {
        disconnected();
    }
    
    @Override
    public void clientConnected(LudoClient client)
    {
    }
    
    @Override
    public void clientDisconnected(LudoClient client)
    {
    
    }
    
    public void startGame()
    {
        if(isHost())
        {
            initGameLogic(names.size());
        }
        else
        {
        
        }
    }
    
    public void initGameLogic(int teams)
    {
        gameLogic = new LudoGameLogic(TeamColor.getTeams(teams), isHost());
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Ludo";
    }
    
    @Override
    public void init()
    {
        String chars = FontHelper.getAvailableChars((char) 0xFF);
        Font fontChicagoFLF = FontHelper.createFont(new File("src/main/resources/fonts/chicagoFLF.ttf"));
        
        fontRenderer64 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(64F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer48 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(48F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer32 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(32F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer24 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(24F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer16 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(16F), StandardCharsets.UTF_8.name(), chars));
        
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_SPACE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_BACKSPACE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_ESCAPE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_ENTER);
        
        try
        {
            LudoTextures.load();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        initProtocol();
        
        screen = new MenuScreen(this);
    }
    
    @Override
    public void cleanup()
    {
        if(clientManager != null)
        {
            clientManager.close();
        }
    }
    
    @Override
    public void update()
    {
        if(isHost() && names.size() >= 3) // TODO
        {
            startGame();
        }
        
        if(clientManager != null)
        {
            clientManager.runMessages(Runnable::run);
        }
    }
    
    @Override
    public void render(float deltaTick)
    {
    
    }
    
    @Override
    public Screen getScreen()
    {
        return screen;
    }
    
    @Override
    public void setScreen(Screen screen)
    {
        this.screen = screen;
    }
    
    public static void main(String... args)
    {
        new Engine(new Ludo()).run();
    }
}
