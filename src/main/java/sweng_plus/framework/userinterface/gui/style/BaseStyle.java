package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public abstract class BaseStyle implements IStyle
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
    
    public BaseStyle stack(IStyle style)
    {
        return new StackedStyle(this, style);
    }
}
