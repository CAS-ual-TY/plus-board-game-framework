package sweng_plus.framework.userinterface.gui.widget;

public class Widget implements IWidget
{
    public final IWidgetParent parent;
    
    protected Dimensions dimensions;
    
    public boolean isMouseOver;
    
    public Widget(IWidgetParent parent, Dimensions dimensions)
    {
        this.parent = parent;
        this.dimensions = dimensions;
    }
    
    @Override
    public void init(int parentX, int parentY, int parentW, int parentH)
    {
        dimensions.init(parentX, parentY, parentW, parentH);
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
    public void keyRepeated(int key, int mods)
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
