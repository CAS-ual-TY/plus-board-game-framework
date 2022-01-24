package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class ColoredQuadStyle extends BaseStyle
{
    protected Color4f color;
    
    public ColoredQuadStyle(Color4f color)
    {
        this.color = color;
    }
    
    @Override
    public void renderStyle(float deltaTick, int mouseX, int mouseY)
    {
        int x1 = dimensions.x;
        int x2 = dimensions.x + dimensions.w;
        int y1 = dimensions.y;
        int y2 = dimensions.y + dimensions.h;
        
        color.glColor4f();
        
        glBegin(GL_QUADS);
        glVertex3f(x1, y1, 0); // Oben Links
        glVertex3f(x1, y2, 0); // Unten Links
        glVertex3f(x2, y2, 0); // Unten Rechts
        glVertex3f(x2, y1, 0); // Oben Rechts
        glEnd();
    }
}
