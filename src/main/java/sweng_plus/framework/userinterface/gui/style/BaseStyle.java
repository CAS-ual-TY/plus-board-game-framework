package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class BaseStyle implements IStyle
{
    protected Dimensions dimensions;
    
    public BaseStyle()
    {
    
    }
    
    @Override
    public void initStyle(Dimensions parentDimensions)
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
