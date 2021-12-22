package sweng_plus.boardgames.mill.gamelogic;


import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNode;

public class MillNode extends TeamNode<MillFigure, MillNode>
{
    private final int index;
    
    public MillNode(TeamColor team, int index)
    {
        super(team);
        
        this.index = index;
    }
    
    public int getIndex()
    {
        return index;
    }
}
