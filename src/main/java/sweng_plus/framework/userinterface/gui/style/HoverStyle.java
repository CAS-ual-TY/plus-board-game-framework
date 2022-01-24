package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class HoverStyle extends BaseStyle
{
    protected IStyle unhovered;
    protected IStyle hovered;
    
    public HoverStyle(IStyle unhovered, IStyle hovered)
    {
        this.unhovered = unhovered;
        this.hovered = hovered;
    }
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
        if(widget.updateMouseOver(mouseX, mouseY))
        {
            hovered.renderWidget(widget, deltaTick, mouseX, mouseY);
        }
        else
        {
            unhovered.renderWidget(widget, deltaTick, mouseX, mouseY);
        }
    }
}
