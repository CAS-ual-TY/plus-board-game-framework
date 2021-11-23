package sweng_plus.framework.userinterface.gui.widget;

import org.lwjgl.glfw.GLFW;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.font.FontRenderer;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import static org.lwjgl.opengl.GL11.*;

public class InputWidget extends SelectableWidget
{
    protected StringBuilder stringBuilder;
    protected FontRenderer fontRenderer;
    
    public InputWidget(IScreenHolder screenHolder, Dimensions dimensions, FontRenderer fontRenderer)
    {
        super(screenHolder, dimensions);
        this.fontRenderer = fontRenderer;
        
        stringBuilder = new StringBuilder();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        updateMouseOver(deltaTick, mouseX, mouseY);
        
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
        
        int margin = (dimensions.h - fontRenderer.getHeight()) / 2;
        int x = dimensions.x + margin; //Höhe wegen gleichem Abstand
        int y = dimensions.y + margin;
        
        Color4f.NEUTRAL.glColor4f();
        
        fontRenderer.render(x, y, stringBuilder.toString());
    }
    
    public boolean tryDelete()
    {
        if(!isSelected || stringBuilder.isEmpty())
        {
            return false;
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return true;
    }
    
    public String getText()
    {
        return stringBuilder.toString();
    }
    
    public void clearText()
    {
        stringBuilder.setLength(0);
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        if(key == GLFW.GLFW_KEY_BACKSPACE)
        {
            tryDelete();
        }
        //TODO
        if(key == GLFW.GLFW_KEY_ENTER)
        {
            stringBuilder.setLength(0);
        }
    }
    
    @Override
    public void keyRepeated(int key, int mods)
    {
        if(key == GLFW.GLFW_KEY_BACKSPACE)
        {
            tryDelete();
        }
    }
    
    @Override
    public void charTyped(char character)
    {
        if(isSelected)
        {
            stringBuilder.append(character);
        }
    }
}
