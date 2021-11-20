package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class ColoredWidget extends Widget
{
    protected Color4f baseColor;
    protected Color4f hoverColor;
    
    public ColoredWidget(IWidgetParent parent, Dimensions dimensions, Color4f baseColor, Color4f hoverColor)
    {
        super(parent, dimensions);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Color4f color = updateMouseOver(deltaTick, mouseX, mouseY) ? hoverColor : baseColor;
        
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
