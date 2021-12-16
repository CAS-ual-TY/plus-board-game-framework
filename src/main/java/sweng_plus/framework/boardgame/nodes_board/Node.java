package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

import java.util.LinkedList;
import java.util.List;

public class Node<F extends INodeFigure<N, F>, N extends INode<F, N>> implements INode<F, N>
{
    private final List<N> forwardNodes;
    private final List<N> backwardNodes;
    private final List<F> figures;
    
    public Node(List<N> forwardNodes, List<N> backwardNodes, List<F> figures)
    {
        this.forwardNodes = forwardNodes;
        this.backwardNodes = backwardNodes;
        this.figures = figures;
    }
    
    public Node()
    {
        this(new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
    }
    
    @Override
    public List<N> getForwardNodes()
    {
        return forwardNodes;
    }
    
    @Override
    public void addForwardNode(N node)
    {
        forwardNodes.add(node);
    }
    
    @Override
    public void removeForwardNode(N node)
    {
        forwardNodes.remove(node);
    }
    
    @Override
    public List<N> getBackwardNodes()
    {
        return backwardNodes;
    }
    
    @Override
    public void addBackwardNode(N node)
    {
        backwardNodes.add(node);
    }
    
    @Override
    public void removeBackwardNode(N node)
    {
        backwardNodes.remove(node);
    }
    
    @Override
    public List<F> getFigures()
    {
        return figures;
    }
    
    @Override
    public void addFigure(F figure)
    {
        figures.add(figure);
    }
    
    @Override
    public void removeFigure(F figure)
    {
        figures.remove(figure);
    }
}
