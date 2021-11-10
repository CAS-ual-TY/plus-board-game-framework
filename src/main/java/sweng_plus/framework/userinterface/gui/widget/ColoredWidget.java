package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class ColoredWidget extends Widget
{
    public Color4f baseColor;
    public Color4f hoverColor;
    
    public ColoredWidget(Screen screen, Dimensions dimensions, Color4f baseColor, Color4f hoverColor)
    {
        super(screen, dimensions);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        Color4f color = isMouseOver ? hoverColor : baseColor;
        
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
