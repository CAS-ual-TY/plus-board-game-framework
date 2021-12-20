package sweng_plus.demos.chat;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.EngineUtil;
import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.networking.AdvancedMessageRegistry;
import sweng_plus.framework.networking.Client;
import sweng_plus.framework.networking.interfaces.IAdvancedMessageRegistry;
import sweng_plus.framework.networking.interfaces.IClientManager;
import sweng_plus.framework.networking.interfaces.IHostManager;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontHelper;
import sweng_plus.framework.userinterface.gui.font.FontInfo;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class NetTestGame implements IGame
{
    private static NetTestGame instance;
    
    private Screen screen;
    
    public FontRenderer fontRenderer64;
    public FontRenderer fontRenderer48;
    public FontRenderer fontRenderer32;
    public FontRenderer fontRenderer24;
    public FontRenderer fontRenderer16;
    
    public IAdvancedMessageRegistry<Client> protocol;
    public NetTestEventsListener listener;
    
    public IClientManager clientManager;
    public IHostManager<Client> hostManager;
    
    public NetTestGame()
    {
        //noinspection ThisEscapedInObjectConstruction
        instance = this;
    }
    
    public static NetTestGame instance()
    {
        return instance;
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Ludo";
    }
    
    @Override
    public String getWindowIconResource()
    {
        return "/icon.png";
    }
    
    @Override
    public void init()
    {
        String chars = FontHelper.getAvailableChars((char) 0xFF);
        Font fontChicagoFLF;
    
        try(InputStream in = EngineUtil.getResourceInputStream("/fonts/ChicagoFLF.ttf"))
        {
            fontChicagoFLF = FontHelper.createFont(in);
        }
        catch(IOException e)
        {
            throw new RuntimeException("failed to load font.", e);
        }
        
        fontRenderer64 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(64F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer48 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(48F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer32 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(32F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer24 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(24F), StandardCharsets.UTF_8.name(), chars));
        fontRenderer16 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(16F), StandardCharsets.UTF_8.name(), chars));
        
        screen = new NetTestMenuScreen(this);
        
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_SPACE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_BACKSPACE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_ESCAPE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_ENTER);
        
        listener = new NetTestEventsListener();
        
        protocol = new AdvancedMessageRegistry<>(8, (byte) 0, (byte) 1, (byte) 2, () -> clientManager, () -> hostManager, listener, listener);
        protocol.registerMessage((byte) 3, NetTestMessage.Handler::encodeMessage, NetTestMessage.Handler::decodeMessage,
                NetTestMessage.Handler::handleMessage, NetTestMessage.class);
        
        GL11.glClearColor(0.5F, 0.5F, 1F, 1F);
    }
    
    @Override
    public void cleanup()
    {
        if(hostManager != null)
            hostManager.close();
        else if(clientManager != null)
            clientManager.close();
    }
    
    @Override
    public void update()
    {
        if(hostManager != null)
            hostManager.update();
        else if(clientManager != null)
            clientManager.update();
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
        new Engine(new NetTestGame()).run();
    }
}
