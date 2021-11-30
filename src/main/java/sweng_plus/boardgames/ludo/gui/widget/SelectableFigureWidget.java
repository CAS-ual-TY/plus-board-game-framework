package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

import static org.lwjgl.opengl.GL11.*;

public class SelectableFigureWidget extends FunctionalButtonWidget
{
    public LudoNodeWidget nodeWidget;
    public LudoFigure ludoFigure;
    
    public SelectableFigureWidget(IScreenHolder screenHolder, Dimensions dimensions, SimpleButtonAction buttonAction, LudoNodeWidget nodeWidget)
    {
        super(screenHolder, dimensions, buttonAction);
        
        this.nodeWidget = nodeWidget;
        ludoFigure = ((LudoFigure) nodeWidget.getNode().getNodeFigures().get(0));
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        nodeWidget.render(deltaTick, mouseX, mouseY);
        
        if(updateMouseOver(deltaTick, mouseX, mouseY))
        {
            renderQuad(deltaTick, mouseX, mouseY);
        }
    }
    
    public void renderQuad(float deltaTick, int mouseX, int mouseY)
    {
        int x1 = dimensions.x;
        int x2 = dimensions.x + dimensions.w;
        int y1 = dimensions.y;
        int y2 = dimensions.y + dimensions.h;
        
        Color4f.FOREGROUND.glColor4f();
        
        glBegin(GL_QUADS);
        glVertex3f(x1, y1, 0); // Oben Links
        glVertex3f(x1, y2, 0); // Unten Links
        glVertex3f(x2, y2, 0); // Unten Rechts
        glVertex3f(x2, y1, 0); // Oben Rechts
        glEnd();
    }
}
