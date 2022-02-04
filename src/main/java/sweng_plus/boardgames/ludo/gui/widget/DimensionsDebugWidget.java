package sweng_plus.boardgames.ludo.gui.widget;

import org.lwjgl.opengl.GL11;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.IWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import static org.lwjgl.opengl.GL11.*;

public class DimensionsDebugWidget extends Widget
{
    public Screen screen;
    public Color4f baseColor;
    public Color4f hoverColor;
    public Color4f lineColor;
    
    public DimensionsDebugWidget(IScreenHolder screenHolder, Screen screen, Color4f baseColor, Color4f hoverColor, Color4f lineColor)
    {
        super(screenHolder, new Dimensions(AnchorPoint.M));
        this.screen = screen;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.lineColor = lineColor;
    }
    
    public DimensionsDebugWidget(IScreenHolder screenHolder, Screen screen)
    {
        this(screenHolder, screen, new Color4f(1F, 0.5F, 0.5F, 0.2F), new Color4f(1F, 0F, 0F, 0.25F), Color4f.BLUE);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IWidget w : screen.getWidgets())
        {
            if(w instanceof Widget widget)
            {
                int x1 = widget.getDimensions().x;
                int x2 = widget.getDimensions().x + widget.getDimensions().w;
                int y1 = widget.getDimensions().y;
                int y2 = widget.getDimensions().y + widget.getDimensions().h;
                
                if(widget.updateMouseOver(mouseX, mouseY))
                {
                    hoverColor.glColor4f();
                }
                else
                {
                    baseColor.glColor4f();
                }
                
                glBegin(GL_QUADS);
                glVertex3f(x1, y1, 0); // Oben Links
                glVertex3f(x1, y2, 0); // Unten Links
                glVertex3f(x2, y2, 0); // Unten Rechts
                glVertex3f(x2, y1, 0); // Oben Rechts
                glEnd();
            }
        }
        
        GL11.glLineWidth(4F);
        
        for(IWidget w : screen.getWidgets())
        {
            if(w instanceof Widget widget)
            {
                int x1 = widget.getDimensions().headAnchor.widthToX(screen.screenW);
                int y1 = widget.getDimensions().headAnchor.heightToY(screen.screenH);
                
                int x2 = widget.getDimensions().x + widget.getDimensions().innerAnchor.widthToX(widget.getDimensions().w);
                int y2 = widget.getDimensions().y + widget.getDimensions().innerAnchor.heightToY(widget.getDimensions().h);
                
                lineColor.glColor4f();
                
                glBegin(GL_LINES);
                glVertex3f(x1, y1, 0);
                glVertex3f(x2, y2, 0);
                glEnd();
            }
        }
    }
}
