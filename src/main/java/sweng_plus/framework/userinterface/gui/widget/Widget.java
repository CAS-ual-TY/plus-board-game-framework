package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.Screen;

public class Widget implements IWidget
{
    public final Screen screen;
    
    protected Dimensions dimensions;
    
    private boolean isMouseOver;
    
    public Widget(Screen screen, Dimensions dimensions)
    {
        this.screen = screen;
        
        this.dimensions = dimensions;
        
        this.init(screen.screenW, screen.screenH);
    }
    
    @Override
    public void init(int screenW, int screenH)
    {
        this.dimensions.init(screenW, screenH);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        isMouseOver = dimensions.isMouseOver(mouseX, mouseY);
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
    
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
    
    }
    
    @Override
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
    
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
    
    }
    
    @Override
    public void keyReleased(int key, int mods)
    {
    
    }
    
    @Override
    public void charTyped(char character)
    {
    
    }
}
