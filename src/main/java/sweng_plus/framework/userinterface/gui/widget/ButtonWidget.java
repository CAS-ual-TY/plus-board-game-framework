package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.Texture;

public class ButtonWidget extends Widget
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
        
        Texture texture = isMouseOver ? active : inactive;
        GL11.glColor4f(1F,1F,1F,1F);
        
        int w1 = dimensions.w/2;
        int w2 = dimensions.w - w1;
        int h1 = dimensions.h/2;
        int h2 = dimensions.h - h1;
    
        texture.render(dimensions.x, dimensions.y, w1, h1, 0, 0, w1, h1);
        texture.render(dimensions.x + w1, dimensions.y, w2, h1, texture.getWidth() - w2, 0, w2, h1);
        texture.render(dimensions.x, dimensions.y + h1, w1, h2, 0, texture.getHeight() - h2, w1, h2);
        texture.render(dimensions.x + w1, dimensions.y + h1, w2, h2, texture.getWidth() - w2, texture.getHeight() - h2, w2, h2);
    
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
    
    //abstract machen
    protected void clicked(int mouseX, int mouseY, int mods)
    {
        System.out.println("Button clicked!");
    }
}
