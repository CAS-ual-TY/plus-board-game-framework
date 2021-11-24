package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

public class NodeFigure implements INodeFigure
{
    private INode currentNode;
    private TeamColor color;
    
    public NodeFigure(INode currentNode, TeamColor color)
    {
        this.currentNode = currentNode;
        this.color = color;
    }
    
    public NodeFigure(TeamColor color)
    {
        this(null, color);
    }
    
    public TeamColor getColor()
    {
        return color;
    }
    
    @Override
    public INode getCurrentNode()
    {
        return currentNode;
    }
    
    @Override
    public void setCurrentNode(INode node)
    {
        if(currentNode != null)
        {
            currentNode.removeNodeFigure(this);
        }
        if(node != null)
        {
            node.addNodeFigure(this);
        }
        currentNode = node;
    }
    
    @Override
    public void move(INode node)
    {
        setCurrentNode(node);
    }
}
