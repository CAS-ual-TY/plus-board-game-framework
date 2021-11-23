package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;

public abstract class ButtonWidget extends Widget
{
    public ButtonWidget(IScreenHolder screenHolder, Dimensions dimensions)
    {
        super(screenHolder, dimensions);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        if(updateMouseOver(Engine.FULL_DELTA_TICK, mouseX, mouseY) && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            clicked(mouseX, mouseY, mods);
        }
    }
    
    protected abstract void clicked(int mouseX, int mouseY, int mods);
}
