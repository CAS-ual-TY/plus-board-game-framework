package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public abstract class ButtonWidget extends SimpleWidget
{
    public ButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle style)
    {
        super(screenHolder, dimensions, style);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        if(updateMouseOver(mouseX, mouseY) && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            clicked(mouseX, mouseY, mods);
        }
    }
    
    protected abstract void clicked(int mouseX, int mouseY, int mods);
}
