package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNodeFigure;

public class MillFigure extends TeamNodeFigure<MillNode, MillFigure>
{
    private int index;
    
    public MillFigure(MillNode currentNode, TeamColor team)
    {
        super(currentNode, team);
    }
    
    public MillFigure(TeamColor team)
    {
        super(team);
    }
}
