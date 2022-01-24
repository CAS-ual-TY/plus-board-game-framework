package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class ColoredBorderStyle extends BaseStyle
{
    protected Color4f color;
    protected int border;
    protected boolean inside;
    
    public ColoredBorderStyle(Color4f color, int border, boolean inside)
    {
        this.color = color;
        this.border = border;
        this.inside = inside;
    }
    
    public ColoredBorderStyle(Color4f color, int border)
    {
        this(color, border, true);
    }
    
    @Override
    public void renderStyle(float deltaTick, int mouseX, int mouseY)
    {
        int x1o = dimensions.x;
        int x2o = dimensions.x + dimensions.w;
        int y1o = dimensions.y;
        int y2o = dimensions.y + dimensions.h;
        
        int x1i = x1o;
        int x2i = x2o;
        int y1i = y1o;
        int y2i = y2o;
        
        if(inside)
        {
            x1i += border;
            x2i -= border;
            y1i += border;
            y2i -= border;
        }
        else
        {
            x1o -= border;
            x2o += border;
            y1o -= border;
            y2o += border;
        }
        
        color.glColor4f();
        
        glBegin(GL_QUADS);
        
        glVertex3f(x1o, y1o, 0);
        glVertex3f(x1i, y1i, 0);
        glVertex3f(x2i, y1i, 0);
        glVertex3f(x2o, y1o, 0);
        
        glVertex3f(x2o, y1o, 0);
        glVertex3f(x2i, y1i, 0);
        glVertex3f(x2i, y2i, 0);
        glVertex3f(x2o, y2o, 0);
        
        glVertex3f(x2o, y2o, 0);
        glVertex3f(x2i, y2i, 0);
        glVertex3f(x1i, y2i, 0);
        glVertex3f(x1o, y2o, 0);
        
        glVertex3f(x1o, y2o, 0);
        glVertex3f(x1i, y2i, 0);
        glVertex3f(x1i, y1i, 0);
        glVertex3f(x1o, y1o, 0);
        
        glEnd();
    }
}
