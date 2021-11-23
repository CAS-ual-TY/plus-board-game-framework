package sweng_plus.framework.userinterface.gui.widget.base;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.IWidgetParent;

public class Widget implements IWidget
{
    public final IScreenHolder screenHolder;
    
    protected Dimensions dimensions;
    
    public boolean isMouseOver;
    
    public Widget(IScreenHolder screenHolder, Dimensions dimensions)
    {
        this.screenHolder = screenHolder;
        this.dimensions = dimensions;
    }
    
    public boolean updateMouseOver(float deltaTick, int mouseX, int mouseY)
    {
        return isMouseOver = dimensions.isMouseOver(mouseX, mouseY);
    }
    
    @Override
    public void initWidget(IWidgetParent parent)
    {
        dimensions.init(parent.getParentX(), parent.getParentY(), parent.getParentW(), parent.getParentH());
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
    
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
