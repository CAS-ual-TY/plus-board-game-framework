package sweng_plus.boardgames.mill;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sweng_plus.boardgames.mill.gamelogic.MillGameLogic;
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

public class Mill implements IGame
{
    private static Mill instance;
    
    private Screen screen;
    
    public FontRenderer fontRenderer64;
    public FontRenderer fontRenderer48;
    public FontRenderer fontRenderer32;
    public FontRenderer fontRenderer24;
    public FontRenderer fontRenderer16;
    
    private MillNetworking networking;
    public MillGameLogic gameLogic;
    public ArrayList<String> names;
    
    public Mill()
    {
        //noinspection ThisEscapedInObjectConstruction
        instance = this;
        
        gameLogic = null;
        names = new ArrayList<>();
    }
    
    public static Mill instance()
    {
        return instance;
    }
    
    public MillGameLogic getGameLogic()
    {
        return gameLogic;
    }
    
    public MillNetworking getNetworking()
    {
        return networking;
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
        /*
        try
        {
            MillTextures.load();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
         */
        
        networking = new MillNetworking(this);
        
        GL11.glClearColor(245 / 255f, 238 / 255f, 176 / 255f, 1f);
        // screen = new MenuScreen(this);
    }
    
    public void startGame(boolean isServer, int teamCount)
    {
        if(gameLogic == null)
        {
            gameLogic = new MillGameLogic(TeamColor.getTeams(teamCount), networking.isHost());
        }
        
        if(isServer)
        {
            gameLogic.startGame();
        }
        else
        {
            //setScreen(new MillScreen(this, gameLogic));
        }
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Mill";
    }
    
    @Override
    public String getWindowIconResource()
    {
        return "/icon.png";
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
    
    public static void main(String... args)
    {
        new Engine(new Mill()).run();
    }
}
