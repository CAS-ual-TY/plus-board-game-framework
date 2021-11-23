package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;

public class SelectableWidget extends Widget
{
    protected boolean isSelected;
    
    public SelectableWidget(IScreenHolder screenHolder, Dimensions dimensions)
    {
        super(screenHolder, dimensions);
        isSelected = false;
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
