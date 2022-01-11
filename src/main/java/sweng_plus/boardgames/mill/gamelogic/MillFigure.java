package sweng_plus.boardgames.mill.gamelogic;

import sweng_plus.framework.boardgame.nodes_board.TeamColor;
import sweng_plus.framework.boardgame.nodes_board.TeamNodeFigure;

public class MillFigure extends TeamNodeFigure<MillNode, MillFigure>
{
    private final int index;
    private boolean alreadyPlaced;
    private boolean inMill;
    
    
    public MillFigure(TeamColor team, int index)
    {
        super(team);
        this.index = index;
        
        alreadyPlaced = false;
        inMill = false;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public boolean isAlreadyPlaced()
    {
        return alreadyPlaced;
    }
    
    public void setAlreadyPlaced(boolean alreadyPlaced)
    {
        this.alreadyPlaced = alreadyPlaced;
    }
    
    public boolean isInMill()
    {
        return inMill;
    }
    
    public void setInMill(boolean inMill)
    {
        this.inMill = inMill;
    }
}
