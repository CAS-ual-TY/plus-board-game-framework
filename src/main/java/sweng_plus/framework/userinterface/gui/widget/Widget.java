package sweng_plus.framework.userinterface.gui.widget;

public class Widget implements IWidget
{
    public final IWidgetParent screen;
    
    protected Dimensions dimensions;
    
    public boolean isMouseOver;
    
    public Widget(IWidgetParent screen, Dimensions dimensions)
    {
        this.screen = screen;
        this.dimensions = dimensions;
    }
    
    @Override
    public void init(int screenW, int screenH)
    {
        dimensions.init(screenW, screenH);
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
