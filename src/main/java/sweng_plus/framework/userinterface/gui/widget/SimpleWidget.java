package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class SimpleWidget extends Widget
{
    protected IStyle style;
    
    public SimpleWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle style)
    {
        super(screenHolder, dimensions);
        this.style = style;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        style.renderWidget(this, deltaTick, mouseX, mouseY);
    }
}