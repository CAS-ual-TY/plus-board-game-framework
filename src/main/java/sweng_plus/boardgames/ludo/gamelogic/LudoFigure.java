package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.NodeFigure;
import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

public class LudoFigure extends NodeFigure
{
    private int index;
    
    public LudoFigure(INode currentNode, TeamColor color, int index)
    {
        super(currentNode, color);
        this.index = index;
    }
    
    public LudoFigure(TeamColor color, int index)
    {
        super(color);
    }
}
