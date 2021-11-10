package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Texture;

public abstract class ButtonWidget extends Widget
{
    protected Texture active;
    protected Texture inactive;
    
    public ButtonWidget(Screen screen, Dimensions dimensions, Texture active, Texture inactive)
    {
        super(screen, dimensions);
        this.active = active;
        this.inactive = inactive;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        Color4f.NEUTRAL.glColor4f();
        
        Texture texture = isMouseOver ? active : inactive;
        texture.renderCornered(dimensions.x, dimensions.y, dimensions.w, dimensions.h);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        super.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
        
        if(dimensions.isMouseOver(mouseX, mouseY) && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            clicked(mouseX, mouseY, mods);
        }
    }
    
    protected abstract void clicked(int mouseX, int mouseY, int mods);
}
