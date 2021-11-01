package sweng_plus.boardgames.ludo;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.DebugScreen;
import sweng_plus.framework.userinterface.gui.Screen;

public class Ludo implements IGame
{
    private Screen screen;
    
    public Ludo()
    {
        screen = new DebugScreen();
    }
    
    @Override
    public String getWindowTitle()
    {
        return "Ludo";
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
