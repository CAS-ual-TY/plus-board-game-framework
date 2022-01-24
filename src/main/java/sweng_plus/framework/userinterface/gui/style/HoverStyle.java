package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

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
    public void initStyle(Dimensions parentDimensions)
    {
        super.initStyle(parentDimensions);
        
        unhovered.initStyle(dimensions);
        hovered.initStyle(dimensions);
    }
    
    @Override
    public void renderStyle(float deltaTick, int mouseX, int mouseY)
    {
        if(dimensions.isMouseOver(mouseX, mouseY))
        {
            hovered.renderStyle(deltaTick, mouseX, mouseY);
        }
        else
        {
            unhovered.renderStyle(deltaTick, mouseX, mouseY);
        }
    }
}
