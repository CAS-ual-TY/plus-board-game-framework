package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.util.LinkedList;
import java.util.List;

public class Node implements INode
{
    private final List<INode> forwardNodes;
    private final List<INode> backwardNodes;
    private final List<NodeFigure> fieldFigures;
    
    public Node()
    {
        forwardNodes = new LinkedList<>();
        backwardNodes = new LinkedList<>();
        fieldFigures = new LinkedList<>();
    }
    
    @Override
    public List<INode> getForwardNodes()
    {
        return forwardNodes;
    }
    
    @Override
    public void addForwardNode(INode forwardNode)
    {
        forwardNodes.add(forwardNode);
    }
    
    @Override
    public void removeForwardNode(INode forwardNode)
    {
        forwardNodes.remove(forwardNode);
    }
    
    @Override
    public List<INode> getBackwardNodes()
    {
        return backwardNodes;
    }
    
    @Override
    public void addBackwardNode(INode backwardNode)
    {
        backwardNodes.add(backwardNode);
    }
    
    @Override
    public void removeBackwardNode(INode backwardNode)
    {
        backwardNodes.remove(backwardNode);
    }
    
    @Override
    public List<NodeFigure> getNodeFigures()
    {
        return fieldFigures;
    }
    
    @Override
    public void addNodeFigure(NodeFigure fieldFigure)
    {
        fieldFigures.add(fieldFigure);
        fieldFigure.setCurrentNode(this);
    }
    
    @Override
    public void removeNodeFigure(NodeFigure fieldFigure)
    {
        fieldFigures.remove(fieldFigure);
        fieldFigure.setCurrentNode(null);
    }
}
