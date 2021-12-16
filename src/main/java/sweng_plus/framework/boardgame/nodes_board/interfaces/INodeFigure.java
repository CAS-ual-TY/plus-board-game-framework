package sweng_plus.framework.boardgame.nodes_board.interfaces;

public interface INodeFigure<N extends INode<F, N>, F extends INodeFigure<N, F>>
{
    N getCurrentNode();
    
    void setCurrentNode(N node);
}
