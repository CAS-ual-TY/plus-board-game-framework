package sweng_plus.boardgames_test.ludo;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;
import sweng_plus.boardgames.ludo.gui.LudoTextures;
import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

import java.util.List;

public class NodeConnectionsWidget extends Widget
{
    public List<NodeWidget> widgets;
    
    public NodeConnectionsWidget(IScreenHolder screenHolder, Dimensions dimensions, List<NodeWidget> widgets)
    {
        super(screenHolder, dimensions);
        this.widgets = widgets;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        GL11.glLineWidth(2F);
        GL11.glPointSize(8F);
        
        for(NodeWidget w : widgets)
        {
            Vector2d start = new Vector2d(w.getDimensions().x, w.getDimensions().y).add(w.getDimensions().w / 2D, w.getDimensions().h / 2D);
            
            for(NodeWidget w2 : w.getForwardNodes())
            {
                Vector2d end = new Vector2d(w2.getDimensions().x, w2.getDimensions().y).add(w2.getDimensions().w / 2D, w2.getDimensions().h / 2D);
                
                Vector2d dir = new Vector2d(end).sub(start).normalize().mul(LudoTextures.node.getWidth()).mul(0.333);
                start.add(dir);
                end.sub(dir);
                
                Color4f.BLACK.glColor4f();
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(start.x, start.y);
                GL11.glVertex2d(end.x, end.y);
                GL11.glEnd();
                
                GL11.glBegin(GL11.GL_POINTS);
                
                Color4f.RED.glColor4f();
                GL11.glVertex2d(start.x, start.y);
                
                Color4f.GREEN.glColor4f();
                GL11.glVertex2d(end.x, end.y);
                
                GL11.glEnd();
                
                Color4f.NEUTRAL.glColor4f();
            }
        }
    }
}