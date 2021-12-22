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
import sweng_plus.framework.userinterface.gui.style.*;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

public class ChatGame implements IGame
{
    private static ChatGame instance;
    
    private Screen screen;
    
    public FontRenderer fontRenderer64;
    public FontRenderer fontRenderer;
    public FontRenderer fontRenderer32;
    public FontRenderer fontRenderer24;
    public FontRenderer fontRenderer16;
    
    public IAdvancedMessageRegistry<Client> protocol;
    public ChatEventsListener listener;
    
    public IClientManager clientManager;
    public IHostManager<Client> hostManager;
    
    public ChatGame()
    {
        //noinspection ThisEscapedInObjectConstruction
        instance = this;
    }
    
    public static ChatGame instance()
    {
        return instance;
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Online Chat";
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
        
        try(InputStream in = EngineUtil.getResourceInputStream("/fonts/NotoSans-Regular.ttf"))
        {
            fontChicagoFLF = FontHelper.createFont(in);
        }
        catch(IOException e)
        {
            throw new RuntimeException("failed to load font.", e);
        }
        
        fontRenderer = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(48F), StandardCharsets.UTF_8.name(), chars));
        
        screen = new ChatMenuScreen(this);
        
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_SPACE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_BACKSPACE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_ESCAPE);
        Engine.instance().getInputHandler().registerKeyTracking(GLFW.GLFW_KEY_ENTER);
        
        listener = new ChatEventsListener();
        
        protocol = new AdvancedMessageRegistry<>(8, (byte) 0, (byte) 1, (byte) 2, () -> clientManager, () -> hostManager, listener, listener);
        protocol.registerMessage((byte) 3, ChatMessage.Handler::encodeMessage, ChatMessage.Handler::decodeMessage,
                ChatMessage.Handler::handleMessage, ChatMessage.class);
        
        GL11.glClearColor(0.5F, 0.5F, 1F, 1F);
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
        if(clientManager != null)
        {
            clientManager.update();
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
    
    public static BaseStyle activeStyle()
    {
        return new ColoredQuadStyle(Color4f.WHITE).stack(new ColoredBorderStyle(Color4f.GREY, 4));
    }
    
    public static BaseStyle activeStyle(Supplier<List<String>> text, AnchorPoint anchor)
    {
        return activeStyle().stack(new FunctionalTextStyle(instance().fontRenderer, text, anchor, Color4f.GREY));
    }
    
    public static BaseStyle activeStyle(Supplier<List<String>> text)
    {
        return activeStyle(text, AnchorPoint.M);
    }
    
    public static BaseStyle activeStyle(String text)
    {
        List<String> list = List.of(text);
        return activeStyle(() -> list);
    }
    
    public static BaseStyle inactiveStyle()
    {
        return new ColoredQuadStyle(Color4f.WHITE).stack(new ColoredBorderStyle(Color4f.BLACK, 4));
    }
    
    public static BaseStyle inactiveStyle(Supplier<List<String>> text, AnchorPoint anchor)
    {
        return inactiveStyle().stack(new FunctionalTextStyle(instance().fontRenderer, text, anchor, Color4f.BLACK));
    }
    
    public static BaseStyle inactiveStyle(Supplier<List<String>> text)
    {
        return inactiveStyle(text, AnchorPoint.M);
    }
    
    public static BaseStyle inactiveStyle(String text)
    {
        List<String> list = List.of(text);
        return inactiveStyle(() -> list);
    }
    
    public static BaseStyle hoverStyle(String text)
    {
        return new HoverStyle(inactiveStyle(text), activeStyle(text));
    }
    
    public static void main(String... args)
    {
        new Engine(new ChatGame()).run();
    }
}
