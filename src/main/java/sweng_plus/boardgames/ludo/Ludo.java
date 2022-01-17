package sweng_plus.boardgames.ludo;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sweng_plus.boardgames.ludo.gamelogic.LudoGameLogic;
import sweng_plus.boardgames.ludo.gui.LudoScreen;
import sweng_plus.boardgames.ludo.gui.MenuScreen;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.EngineUtil;
import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontHelper;
import sweng_plus.framework.userinterface.gui.font.FontInfo;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Ludo implements IGame
{
    private static Ludo instance;
    
    public static final String LOCALE_DE_DE = "de_de";
    public static final String LOCALE_EN_US = "en_us";
    
    private Screen screen;
    
    public FontRenderer fontRenderer64;
    public FontRenderer fontRenderer48;
    public FontRenderer fontRenderer32;
    public FontRenderer fontRenderer24;
    public FontRenderer fontRenderer16;
    
    private LudoNetworking networking;
    public LudoGameLogic gameLogic;
    public ArrayList<String> names;
    
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
    
    public LudoNetworking getNetworking()
    {
        return networking;
    }
    
    public void startGame(boolean isServer, int teamCount)
    {
        if(gameLogic == null)
        {
            gameLogic = new LudoGameLogic(TeamColor.getTeams(teamCount), networking.isHost());
        }
        
        if(isServer)
        {
            gameLogic.startGame();
        }
        else
        {
            setScreen(new LudoScreen(this, gameLogic));
        }
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Mensch ärgere Dich nicht!";
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
        
        try(InputStream in = EngineUtil.getResourceInputStream("/fonts/ChicagoFLF.ttf"))
        {
            Font fontChicagoFLF = FontHelper.createFont(in);
            
            fontRenderer64 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(64F), StandardCharsets.UTF_8.name(), chars));
            fontRenderer48 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(48F), StandardCharsets.UTF_8.name(), chars));
            fontRenderer32 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(32F), StandardCharsets.UTF_8.name(), chars));
            fontRenderer24 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(24F), StandardCharsets.UTF_8.name(), chars));
            fontRenderer16 = new FontRenderer(new FontInfo(fontChicagoFLF.deriveFont(16F), StandardCharsets.UTF_8.name(), chars));
        }
        catch(IOException e)
        {
            throw new RuntimeException("failed to load font.", e);
        }
        
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
        
        networking = new LudoNetworking(this);
        
        GL11.glClearColor(245 / 255f, 238 / 255f, 176 / 255f, 1f);
        screen = new MenuScreen(this);
    }
    
    @Override
    public void cleanup()
    {
        networking.cleanup();
    }
    
    @Override
    public void update()
    {
        networking.update();
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
    
    @Override
    public String getDefaultLocale()
    {
        return LOCALE_DE_DE;
    }
    
    public static void main(String... args)
    {
        new Engine(new Ludo()).run();
    }
}
