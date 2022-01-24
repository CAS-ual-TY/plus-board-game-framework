package sweng_plus.boardgames.mill.gui;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import static org.lwjgl.opengl.GL11.*;

public class MillBoardWidget extends Widget
{
    public static final int LINESIZE = 4;
    protected int size;
    
    public MillBoardWidget(IScreenHolder screenHolder, Dimensions dimensions, int size)
    {
        super(screenHolder, dimensions);
        this.size = size;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        renderHorizontal(dimensions.x - 3 * size, dimensions.x + 3 * size, dimensions.y - 3 * size);
        renderHorizontal(dimensions.x - 3 * size, dimensions.x + 3 * size, dimensions.y + 3 * size);
        renderVertical(dimensions.x - 3 * size, dimensions.y - 3 * size, dimensions.y + 3 * size);
        renderVertical(dimensions.x + 3 * size, dimensions.y - 3 * size, dimensions.y + 3 * size);
        
        renderHorizontal(dimensions.x - 2 * size, dimensions.x + 2 * size, dimensions.y - 2 * size);
        renderHorizontal(dimensions.x - 2 * size, dimensions.x + 2 * size, dimensions.y + 2 * size);
        renderVertical(dimensions.x - 2 * size, dimensions.y - 2 * size, dimensions.y + 2 * size);
        renderVertical(dimensions.x + 2 * size, dimensions.y - 2 * size, dimensions.y + 2 * size);
        
        renderHorizontal(dimensions.x - size, dimensions.x + size, dimensions.y - size);
        renderHorizontal(dimensions.x - size, dimensions.x + size, dimensions.y + size);
        renderVertical(dimensions.x - size, dimensions.y - size, dimensions.y + size);
        renderVertical(dimensions.x + size, dimensions.y - size, dimensions.y + size);
        
        renderHorizontal(dimensions.x - 3 * size, dimensions.x - size, dimensions.y);
        renderHorizontal(dimensions.x + size, dimensions.x + 3 * size, dimensions.y);
        renderVertical(dimensions.x, dimensions.y - 3 * size, dimensions.y - size);
        renderVertical(dimensions.x, dimensions.y + size, dimensions.y + 3 * size);
        
        super.render(deltaTick, mouseX, mouseY);
    }
    
    protected void renderHorizontal(int x1, int x2, int y)
    {
        int dx1 = x1 - LINESIZE;
        int dx2 = x2 + LINESIZE;
        int dy1 = y - LINESIZE;
        int dy2 = y + LINESIZE;
        
        Color4f.BLACK.glColor4f();
        
        glBegin(GL_QUADS);
        glVertex3f(dx1, dy1, 0); // Oben Links
        glVertex3f(dx1, dy2, 0); // Unten Links
        glVertex3f(dx2, dy2, 0); // Unten Rechts
        glVertex3f(dx2, dy1, 0); // Oben Rechts
        glEnd();
    }
    
    protected void renderVertical(int x, int y1, int y2)
    {
        int dx1 = x - LINESIZE;
        int dx2 = x + LINESIZE;
        int dy1 = y1 - LINESIZE;
        int dy2 = y2 + LINESIZE;
        
        Color4f.BLACK.glColor4f();
        
        glBegin(GL_QUADS);
        glVertex3f(dx1, dy1, 0); // Oben Links
        glVertex3f(dx1, dy2, 0); // Unten Links
        glVertex3f(dx2, dy2, 0); // Unten Rechts
        glVertex3f(dx2, dy1, 0); // Oben Rechts
        glEnd();
    }
}
