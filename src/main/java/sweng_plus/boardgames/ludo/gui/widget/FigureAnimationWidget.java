package sweng_plus.boardgames.ludo.gui.widget;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class FigureAnimationWidget extends Widget
{
    public static final int ANIMATION_SPEED = 1;
    
    protected LudoFigure figure;
    protected Texture figureTexture;
    protected LudoNodeWidget startNode;
    protected LudoNodeWidget endNode;
    
    protected int timer;
    protected int animationLength;
    
    protected boolean accelerate;
    protected boolean decelerate;
    protected boolean rotate;
    
    public FigureAnimationWidget(IScreenHolder screenHolder, Dimensions dimensions, LudoFigure figure, Texture figureTexture,
                                 LudoNodeWidget startNode, LudoNodeWidget endNode, int animationLength,
                                 boolean accelerate, boolean decelerate, boolean rotate)
    {
        super(screenHolder, dimensions);
        this.figure = figure;
        this.figureTexture = figureTexture;
        this.startNode = startNode;
        this.endNode = endNode;
        this.animationLength = animationLength;
        this.accelerate = accelerate;
        this.decelerate = decelerate;
        this.rotate = rotate;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        /*Vector2d startPos = new Vector2d(startNode.getDimensions().x, startNode.getDimensions().y)
                .add((startNode.getDimensions().w - figureTexture.getWidth()) / 2D,
                        (startNode.getDimensions().h - figureTexture.getHeight()) / 2D);
        Vector2d endPos = new Vector2d(endNode.getDimensions().x, endNode.getDimensions().y)
                .add((endNode.getDimensions().w - figureTexture.getWidth()) / 2D,
                        (endNode.getDimensions().h - figureTexture.getHeight()) / 2D);*/
        Vector2d startPos = new Vector2d(startNode.getDimensions().x, startNode.getDimensions().y)
                .add(0, -LudoNodeWidget.FIGURE_TEXTURE_OFFSET);
        Vector2d endPos = new Vector2d(endNode.getDimensions().x, endNode.getDimensions().y)
                .add(0, -LudoNodeWidget.FIGURE_TEXTURE_OFFSET);
        
        double relativePos = function(accelerate, decelerate, timer + deltaTick, animationLength);
        Vector2d motion = new Vector2d(endPos).sub(startPos).mul(relativePos);
        
        Vector2d pos = new Vector2d(startPos).add(motion);
        Vector2i posI = pos.get(RoundingMode.HALF_UP, new Vector2i());
        
        figure.getColor().getColor().glColor4f();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(posI.x(), posI.y(), 0);
        GL11.glTranslatef((float) startNode.getDimensions().w / 2F,
                (float) startNode.getDimensions().h / 2F, 0);
        
        if(rotate)
        {
            GL11.glRotatef(360F * (float) relativePos, 0F, 0F, -1F);
        }
        
        GL11.glTranslatef((float) -figureTexture.getWidth() / 2F,
                (float) -figureTexture.getHeight() / 2F, 0);
        
        figureTexture.render(0, 0);
        GL11.glPopMatrix();
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        timer += ANIMATION_SPEED;
    }
    
    public boolean hasEnded()
    {
        return timer >= animationLength;
    }
    
    public static double function(boolean accelerate, boolean decelerate, double time, double maxTime)
    {
        if(accelerate)
        {
            if(decelerate)
            {
                // both
                return (Math.sin(Math.PI * (1.5D + time / maxTime)) + 1) * 0.5D;
            }
            else
            {
                // a
                return Math.sin(Math.PI * (1.5D + 0.5D * time / maxTime)) + 1;
            }
        }
        else
        {
            if(decelerate)
            {
                // d
                return Math.sin(Math.PI * 0.5D * time / maxTime);
            }
            else
            {
                // none
                return time / maxTime;
            }
        }
    }
}
