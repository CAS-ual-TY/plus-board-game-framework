package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public abstract class ButtonWidget extends Widget
{
    protected IStyle style;
    
    public ButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle style)
    {
        super(screenHolder, dimensions);
        this.style = style;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        style.renderWidget(this, deltaTick, mouseX, mouseY);
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
