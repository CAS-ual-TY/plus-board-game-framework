package sweng_plus.boardgames.ludo;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.IGame;
import sweng_plus.framework.userinterface.gui.Screen;

import static org.lwjgl.opengl.GL11.glClearColor;

public class Ludo implements IGame
{
    private Screen screen;
    
    public Ludo()
    {
        screen = Screen.EMPTY_SCREEN;
    }
    
    @Override
    public void postInit()
    {
        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
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
