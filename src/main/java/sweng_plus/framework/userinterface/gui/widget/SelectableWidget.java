package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
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
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        if(isSelected)
        {
            activeStyle.renderWidget(this, deltaTick, mouseX, mouseY);
        }
        else
        {
            inactiveStyle.renderWidget(this, deltaTick, mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            isSelected = updateMouseOver(Engine.FULL_DELTA_TICK, mouseX, mouseY);
        }
    }
}
