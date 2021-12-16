package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.LudoNodeType;
import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class LudoNodeWidget extends NodeWidget
{
    public static final int FIGURE_TEXTURE_OFFSET = 24;
    public static final int NODE_TEXTURE_RADIUS_SQUARED = 32 * 32;
    
    protected Texture nodeTexture;
    protected Texture figureTexture;
    
    protected TeamColor team;
    protected LudoNodeType type;
    
    public LudoNodeWidget(IScreenHolder screenHolder, Dimensions dimensions,
                          LudoNode node, Texture nodeTexture, Texture figureTexture)
    {
        super(screenHolder, dimensions, node);
        this.nodeTexture = nodeTexture;
        this.figureTexture = figureTexture;
        
        team = node.getColor();
        type = node.getNodeType();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        if(type == LudoNodeType.NEUTRAL || type == LudoNodeType.HOME_ENTRANCE)
        {
            Color4f.WHITE.glColor4f();
        }
        else
        {
            team.getColor().glColor3fStrength(1F);
        }
        
        renderNode();
        
        if(getNode().isOccupied() && getNode() instanceof LudoNode ludoNode &&
                ludoNode.getNodeFigures().get(0) instanceof LudoFigure ludoFigure)
        {
            ludoFigure.getColor().getColor().glColor4f();
            
            renderFigure();
        }
    }
    
    public void renderNode()
    {
        nodeTexture.render(
                dimensions.x + (dimensions.w - nodeTexture.getWidth()) / 2,
                dimensions.y + (dimensions.h - nodeTexture.getHeight()) / 2);
    }
    
    public void renderFigure()
    {
        figureTexture.render(
                dimensions.x + (dimensions.w - figureTexture.getWidth()) / 2,
                dimensions.y + (dimensions.h - figureTexture.getHeight()) / 2 - FIGURE_TEXTURE_OFFSET);
    }
}
