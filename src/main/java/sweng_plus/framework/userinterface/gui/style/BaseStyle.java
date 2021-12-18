package sweng_plus.framework.userinterface.gui.style;

public abstract class BaseStyle implements IStyle
{
    public BaseStyle stack(IStyle style)
    {
        return new StackedStyle(this, style);
    }
}
