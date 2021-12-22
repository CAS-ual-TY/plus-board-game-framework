package sweng_plus.boardgames.ludo.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNodeFigure;

public class LudoFigure extends TeamNodeFigure<LudoNode, LudoFigure>
{
    private int index;
    
    public LudoFigure(TeamColor color, int index)
    {
        super(color);
    }
    
    public int getIndex()
    {
        return index;
    }
}
