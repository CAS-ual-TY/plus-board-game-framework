package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Rectangle;

import static org.lwjgl.opengl.GL11.*;

public class ColoredBorderStyle extends BaseStyle
{
    protected Color4f color;
    protected int border;
    protected boolean inside;
    
    protected int x1o;
    protected int x2o;
    protected int y1o;
    protected int y2o;
    
    protected int x1i;
    protected int x2i;
    protected int y1i;
    protected int y2i;
    
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
    public void initStyle(Rectangle parentDimensions)
    {
        super.initStyle(parentDimensions);
        
        x1o = dimensions.x;
        x2o = dimensions.x + dimensions.w;
        y1o = dimensions.y;
        y2o = dimensions.y + dimensions.h;
        
        x1i = x1o;
        x2i = x2o;
        y1i = y1o;
        y2i = y2o;
        
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
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
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
