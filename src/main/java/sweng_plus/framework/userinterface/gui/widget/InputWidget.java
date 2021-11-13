package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class InputWidget extends Widget
{
    protected StringBuilder builder;
    protected FontRenderer renderer;
    
    public InputWidget(IWidgetParent parent, Dimensions dimensions, FontRenderer renderer)
    {
        super(parent, dimensions);
        this.renderer = renderer;
        
        builder = new StringBuilder();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
    
        //Quad Rendern als Hintergrund:
        int x1 = dimensions.x;
        int x2 = dimensions.x + dimensions.w;
        int y1 = dimensions.y;
        int y2 = dimensions.y + dimensions.h;
    
        Color4f.BLACK.glColor4f();
    
        glBegin(GL_QUADS);
        glVertex3f(x1, y1, 0); // Oben Links
        glVertex3f(x1, y2, 0); // Unten Links
        glVertex3f(x2, y2, 0); // Unten Rechts
        glVertex3f(x2, y1, 0); // Oben Rechts
        glEnd();
        
        int margin = (dimensions.h - renderer.getHeight()) / 2;
        int x = dimensions.x + margin; //Höhe wegen gleichem Abstand
        int y = dimensions.y + margin;
        
        Color4f.NEUTRAL.glColor4f();
        
        renderer.render(x, y, builder.toString());
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        if (key == GLFW.GLFW_KEY_BACKSPACE)
            builder.delete(builder.length()-1,builder.length());
        if (key == GLFW.GLFW_KEY_ENTER)
            builder.setLength(0);
    }
    
    @Override
    public void charTyped(char character)
    {
        builder.append(character);
    }
}
