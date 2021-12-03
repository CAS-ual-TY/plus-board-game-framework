package sweng_plus.framework.userinterface.gui.style;

import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import static org.lwjgl.opengl.GL11.*;

public class ColoredQuadStyle implements IStyle
{
    protected Color4f color;
    
    public ColoredQuadStyle(Color4f color)
    {
        this.color = color;
    }
    
    @Override
    public void renderWidget(Widget widget, float deltaTick, int mouseX, int mouseY)
    {
        int x1 = widget.getDimensions().x;
        int x2 = widget.getDimensions().x + widget.getDimensions().w;
        int y1 = widget.getDimensions().y;
        int y2 = widget.getDimensions().y + widget.getDimensions().h;
        
        color.glColor4f();
        
        glBegin(GL_QUADS);
        glVertex3f(x1, y1, 0); // Oben Links
        glVertex3f(x1, y2, 0); // Unten Links
        glVertex3f(x2, y2, 0); // Unten Rechts
        glVertex3f(x2, y1, 0); // Oben Rechts
        glEnd();
    }
}
