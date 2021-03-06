package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Rectangle;

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
    public void initStyle(Rectangle parentDimensions)
    {
        super.initStyle(parentDimensions);
        
        unhovered.initStyle(dimensions);
        hovered.initStyle(dimensions);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        if(dimensions.isMouseOver(mouseX, mouseY))
        {
            hovered.render(deltaTick, mouseX, mouseY);
        }
        else
        {
            unhovered.render(deltaTick, mouseX, mouseY);
        }
    }
}
