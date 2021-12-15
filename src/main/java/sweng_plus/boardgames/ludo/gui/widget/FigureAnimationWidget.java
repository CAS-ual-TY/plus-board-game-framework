package sweng_plus.boardgames.ludo.gui.widget;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2i;
import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.base.Widget;

public class FigureAnimationWidget extends Widget
{
    public static final int ANIMATION_SPEED = 2;
    
    protected LudoFigure figure;
    protected Texture figureTexture;
    protected LudoNodeWidget startNode;
    protected LudoNodeWidget endNode;
    
    protected int timer;
    protected int animationLength;
    
    public FigureAnimationWidget(IScreenHolder screenHolder, Dimensions dimensions, LudoFigure figure, Texture figureTexture,
                                 LudoNodeWidget startNode, LudoNodeWidget endNode, int animationLength)
    {
        super(screenHolder, dimensions);
        this.figure = figure;
        this.figureTexture = figureTexture;
        this.startNode = startNode;
        this.endNode = endNode;
        this.animationLength = animationLength;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Vector2d startPos = new Vector2d(startNode.getDimensions().x, startNode.getDimensions().y)
                .add((startNode.getDimensions().w - figureTexture.getWidth()) / 2D,
                        (startNode.getDimensions().h - figureTexture.getHeight()) / 2D);
        Vector2d endPos = new Vector2d(endNode.getDimensions().x, endNode.getDimensions().y)
                .add((endNode.getDimensions().w - figureTexture.getWidth()) / 2D,
                        (endNode.getDimensions().h - figureTexture.getHeight()) / 2D);
        
        double relativePos = Math.cos(Math.PI + Math.PI * (timer + deltaTick) / animationLength);
        relativePos = (relativePos + 1) * 0.5D;
        Vector2d motion = new Vector2d(endPos).sub(startPos).mul(relativePos);
        
        Vector2d pos = new Vector2d(startPos).add(motion);
        Vector2i posI = pos.get(RoundingMode.HALF_UP, new Vector2i());
        
        figure.getColor().getColor().glColor4f();
        
        figureTexture.render(posI.x, posI.y);
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
}
