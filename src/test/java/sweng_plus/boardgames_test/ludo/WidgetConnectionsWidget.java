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

public class WidgetConnectionsWidget extends Widget
{
    public List<NodeWidget> widgets;
    
    public WidgetConnectionsWidget(IScreenHolder screenHolder, Dimensions dimensions, List<NodeWidget> widgets)
    {
        super(screenHolder, dimensions);
        this.widgets = widgets;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        GL11.glLineWidth(4F);
        
        for(NodeWidget w : widgets)
        {
            Vector2d start0 = new Vector2d(w.getDimensions().x, w.getDimensions().y).add(w.getDimensions().w / 2D, w.getDimensions().h / 2D);
            
            for(NodeWidget w2 : w.getForwardNodes())
            {
                Vector2d start = new Vector2d(start0);
                
                Vector2d end = new Vector2d(w2.getDimensions().x, w2.getDimensions().y)
                        .add(w2.getDimensions().w / 2D, w2.getDimensions().h / 2D);
                
                Vector2d dir = new Vector2d(end).sub(start).normalize().mul(LudoTextures.node.getWidth());
                start.add(new Vector2d(dir).mul(0.333));
                end.sub(new Vector2d(dir).mul(0.5));
                
                Color4f.BLACK.glColor4f();
                
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(start.x, start.y);
                GL11.glVertex2d(end.x, end.y);
                GL11.glEnd();
                
                GL11.glPointSize(10F);
                GL11.glBegin(GL11.GL_POINTS);
                GL11.glVertex2d(start.x, start.y);
                GL11.glEnd();
                
                Color4f.RED.glColor4f();
                
                GL11.glPointSize(6F);
                GL11.glBegin(GL11.GL_POINTS);
                GL11.glVertex2d(start.x, start.y);
                GL11.glEnd();
                
                Color4f.NEUTRAL.glColor4f();
            }
            
            for(NodeWidget w2 : w.getBackwardNodes())
            {
                Vector2d start = new Vector2d(start0);
                
                Vector2d end = new Vector2d(w2.getDimensions().x, w2.getDimensions().y)
                        .add(w2.getDimensions().w / 2D, w2.getDimensions().h / 2D);
                
                Vector2d dir = new Vector2d(end).sub(start).normalize().mul(LudoTextures.node.getWidth());
                start.add(new Vector2d(dir).mul(0.333));
                end.sub(new Vector2d(dir).mul(0.5));
                
                Color4f.BLACK.glColor4f();
                
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(start.x, start.y);
                GL11.glVertex2d(end.x, end.y);
                GL11.glEnd();
                
                GL11.glPointSize(10F);
                GL11.glBegin(GL11.GL_POINTS);
                GL11.glVertex2d(start.x, start.y);
                GL11.glEnd();
                
                Color4f.GREEN.glColor4f();
                
                GL11.glPointSize(6F);
                GL11.glBegin(GL11.GL_POINTS);
                GL11.glVertex2d(start.x, start.y);
                GL11.glEnd();
                
                Color4f.NEUTRAL.glColor4f();
            }
        }
    }
}
