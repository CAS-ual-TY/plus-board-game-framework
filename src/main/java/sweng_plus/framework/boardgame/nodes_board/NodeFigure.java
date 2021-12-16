package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

public class NodeFigure<N extends INode<F, N>, F extends INodeFigure<N, F>> implements INodeFigure<N, F>
{
    private N currentNode;
    
    public NodeFigure(N currentNode)
    {
        this.currentNode = currentNode;
    }
    
    public NodeFigure()
    {
        this(null);
    }
    
    @Override
    public N getCurrentNode()
    {
        return currentNode;
    }
    
    @Override
    public void setCurrentNode(N node)
    {
        currentNode = node;
    }
}
