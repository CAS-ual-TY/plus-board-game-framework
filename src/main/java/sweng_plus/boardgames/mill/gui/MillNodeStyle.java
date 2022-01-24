package sweng_plus.boardgames.mill.gui;

import sweng_plus.boardgames.mill.gamelogic.MillNode;
import sweng_plus.boardgames.mill.gui.util.MillTextures;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.userinterface.gui.style.BaseStyle;
import sweng_plus.framework.userinterface.gui.util.Color4f;

public class MillNodeStyle extends BaseStyle
{
    protected MillNode node;
    
    public MillNodeStyle(MillNode node)
    {
        this.node = node;
    }
    
    @Override
    public void renderStyle(float deltaTick, int mouseX, int mouseY)
    {
        if(node.isOccupied())
        {
            Color4f.NEUTRAL.glColor4f();
            if(node.getFigures().get(0).getTeam() == TeamColor.BLACK)
            {
                MillTextures.figureBlack.render(dimensions.x, dimensions.y);
            }
            else if(node.getFigures().get(0).getTeam() == TeamColor.WHITE)
            {
                MillTextures.figureWhite.render(dimensions.x, dimensions.y);
            }
        }
        
    }
}
