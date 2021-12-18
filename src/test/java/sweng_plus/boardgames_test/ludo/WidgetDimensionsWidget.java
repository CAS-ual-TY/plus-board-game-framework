package sweng_plus.boardgames_test.ludo;

import org.lwjgl.opengl.GL11;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.IWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class WidgetDimensionsWidget extends Widget
{
    public Screen screen;
    public Color4f baseColor;
    public Color4f hoverColor;
    public Color4f lineColor;
    
    public WidgetDimensionsWidget(IScreenHolder screenHolder, Screen screen, Color4f baseColor, Color4f hoverColor, Color4f lineColor)
    {
        super(screenHolder, new Dimensions(AnchorPoint.M));
        this.screen = screen;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.lineColor = lineColor;
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
    
                if(widget.updateMouseOver(deltaTick, mouseX, mouseY))
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
    
        GL11.glLineWidth(2F);
    
        for(IWidget w : screen.getWidgets())
        {
            if(w instanceof Widget widget)
            {
                int x1 = widget.getDimensions().headAnchor.widthToX(screen.screenW);
                int y1 = widget.getDimensions().headAnchor.heightToY(screen.screenH);
                
                int x2 = widget.getDimensions().x + widget.getDimensions().headAnchor.widthToX(widget.getDimensions().w);
                int y2 = widget.getDimensions().y + widget.getDimensions().headAnchor.heightToY(widget.getDimensions().h);
    
                lineColor.glColor4f();
            
                glBegin(GL_LINES);
                glVertex3f(x1, y1, 0);
                glVertex3f(x2, y2, 0);
                glEnd();
            }
        }
    }
}
