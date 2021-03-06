package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

import java.util.List;

public class TeamNode<F extends INodeFigure<N, F>, N extends INode<F, N>> extends Node<F, N>
{
    protected TeamColor team;
    
    public TeamNode(TeamColor team)
    {
        super();
        this.team = team;
    }
    
    public TeamNode(TeamColor team, List<N> forwardNodes, List<N> backwardNodes, List<F> figures)
    {
        super(forwardNodes, backwardNodes, figures);
        this.team = team;
    }
    
    public TeamColor getTeam()
    {
        return team;
    }
}
