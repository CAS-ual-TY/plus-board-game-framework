package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Board
{
    protected final List<INode> nodes;
    protected final List<NodeFigure> fieldFigures;
    
    public Board(List<INode> nodes, List<NodeFigure> fieldFigures)
    {
        this.nodes = nodes;
        this.fieldFigures = fieldFigures;
    }
    
    public Board()
    {
        this(new LinkedList<>(), new LinkedList<>());
    }
    
    public List<INode> getNodes()
    {
        return nodes;
    }
    
    public List<NodeFigure> getNodeFigures()
    {
        return fieldFigures;
    }
    
    protected void addNode(INode node)
    {
        nodes.add(node);
    }
    
    protected void addAllNodes(List<? extends INode> nodes)
    {
        this.nodes.addAll(nodes);
    }
    
    public List<INode> getForwardNodes(INode start, int distance, Predicate<INode> predicate)
    {
        if(!nodes.contains(start))
        {   // Node not on board
            return new LinkedList<>();
        }
        return start.getDistantForwardNodes(distance, predicate);
    }
    
    public List<INode> getForwardNodes(INode start)
    {
        if(!nodes.contains(start))
        {   // Node not on board
            return new LinkedList<>();
        }
        return start.getForwardNodes();
    }
    
    public List<INode> getForwardNodes(NodeFigure figure, int distance, Predicate<INode> predicate)
    {
        if(!fieldFigures.contains(figure))
        {   // Figure not on board
            return new LinkedList<>();
        }
        return figure.getCurrentNode().getDistantForwardNodes(distance, predicate);
    }
    
    public List<INode> getForwardNodes(NodeFigure figure)
    {
        if(!fieldFigures.contains(figure))
        {   // Figure not on board
            return new LinkedList<>();
        }
        return figure.getCurrentNode().getForwardNodes();
    }
    
    
    public List<INode> getBackwardNodes(INode start, int distance, Predicate<INode> predicate)
    {
        if(!nodes.contains(start))
        {   // Node not on board
            return new LinkedList<>();
        }
        return start.getDistantBackwardNodes(distance, predicate);
    }
    
    public List<INode> getBackwardNodes(INode start)
    {
        if(!nodes.contains(start))
        {   // Node not on board
            return new LinkedList<>();
        }
        return start.getBackwardNodes();
    }
    
    public List<INode> getBackwardNodes(NodeFigure figure, int distance, Predicate<INode> predicate)
    {
        if(!fieldFigures.contains(figure))
        {   // Figure not on board
            return new LinkedList<>();
        }
        return figure.getCurrentNode().getDistantBackwardNodes(distance, predicate);
    }
    
    public List<INode> getAllBackwardNodes(NodeFigure figure)
    {
        if(!fieldFigures.contains(figure))
        {   // Figure not on board
            return new LinkedList<>();
        }
        return figure.getCurrentNode().getBackwardNodes();
    }
    
    public void moveFigure(NodeFigure figure, INode target)
    {
        if(!(fieldFigures.contains(figure) || nodes.contains(target)))
        {   // Figure not on board
            return;
        }
        figure.move(target);
    }
}
