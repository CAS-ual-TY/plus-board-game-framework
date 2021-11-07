package sweng_plus.boardgames.ludo;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.DebugScreen;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.font.FontHelper;
import sweng_plus.framework.userinterface.gui.font.FontInfo;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class Ludo implements IGame
{
    public static Ludo instance;
    
    private Screen screen;
    
    public FontRenderer fontRenderer;
    
    public Ludo()
    {
        instance = this;
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Ludo";
    }
    
    @Override
    public void postInit()
    {
        Font fontChicagoFLF = FontHelper.createFont(new File("src/main/resources/fonts/chicagoFLF.ttf"), 64F);
        FontInfo fontInfo = new FontInfo(fontChicagoFLF, StandardCharsets.UTF_8.name(), FontHelper.getAvailableChars((char)0xFF));
        fontRenderer = new FontRenderer(fontInfo);
        
        screen = new DebugScreen();
    }
    
    @Override
    public void update()
    {
    
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
