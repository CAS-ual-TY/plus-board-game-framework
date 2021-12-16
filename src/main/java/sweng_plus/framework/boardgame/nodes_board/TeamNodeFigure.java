package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

public class TeamNodeFigure<N extends INode<F, N>, F extends INodeFigure<N, F>> extends NodeFigure<N, F>
{
    private TeamColor team;
    
    public TeamNodeFigure(TeamColor team)
    {
        super();
        this.team = team;
    }
    
    public TeamNodeFigure(N currentNode, TeamColor team)
    {
        super(currentNode);
        this.team = team;
    }
    
    public TeamColor getTeam()
    {
        return team;
    }
}
