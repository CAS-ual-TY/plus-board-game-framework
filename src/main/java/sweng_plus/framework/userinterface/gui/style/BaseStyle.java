package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Rectangle;

public class BaseStyle implements IStyle
{
    protected Rectangle dimensions;
    
    public BaseStyle()
    {
    
    }
    
    @Override
    public void initStyle(Rectangle parentDimensions)
    {
        dimensions = parentDimensions;
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
    
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
    
    }
    
    public BaseStyle stack(IStyle style)
    {
        return new StackedStyle(this, style);
    }
}
