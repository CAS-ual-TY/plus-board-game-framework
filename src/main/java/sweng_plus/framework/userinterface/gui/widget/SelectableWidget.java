package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.IWidgetParent;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class SelectableWidget extends Widget
{
    protected IStyle activeStyle;
    protected IStyle inactiveStyle;
    
    protected boolean isSelected;
    
    public SelectableWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle activeStyle, IStyle inactiveStyle)
    {
        super(screenHolder, dimensions);
        this.activeStyle = activeStyle;
        this.inactiveStyle = inactiveStyle;
        isSelected = false;
    }
    
    @Override
    public void initWidget(IWidgetParent parent)
    {
        super.initWidget(parent);
        activeStyle.initStyle(dimensions);
        inactiveStyle.initStyle(dimensions);
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        activeStyle.update(mouseX, mouseY);
        inactiveStyle.update(mouseX, mouseY);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        if(isSelected)
        {
            activeStyle.render(deltaTick, mouseX, mouseY);
        }
        else
        {
            inactiveStyle.render(deltaTick, mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            isSelected = updateMouseOver(mouseX, mouseY);
        }
    }
}
