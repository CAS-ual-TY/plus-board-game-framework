package sweng_plus.boardgames.ludo.gui.widget;

import sweng_plus.boardgames.ludo.gamelogic.LudoFigure;
import sweng_plus.boardgames.ludo.gamelogic.LudoNode;
import sweng_plus.boardgames.ludo.gamelogic.LudoNodeType;
import sweng_plus.framework.boardgame.gui.widget.NodeWidget;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.texture.Texture;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class LudoFigureWidget extends NodeWidget
{
    private final Texture texture;
    public LudoFigureWidget(IScreenHolder screenHolder, Dimensions dimensions, INode node, Texture figureTexture)
    {
        super(screenHolder, dimensions, node);
        this.texture = figureTexture;
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        if(getNode().isOccupied() && getNode() instanceof LudoNode ludoNode && ludoNode.getNodeFigures().get(0) instanceof LudoFigure ludoFigure)
        {
            ludoFigure.getColor().getColor().glColor4f();
            // TODO Position anpassen
            texture.render(
                    dimensions.x + (dimensions.w - texture.getWidth()) / 2,
                    dimensions.y + (dimensions.h - texture.getHeight()) / 2 - dimensions.h/4);
        }
    }
}
