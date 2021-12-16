package sweng_plus.framework.boardgame.nodes_board;

import sweng_plus.framework.boardgame.nodes_board.interfaces.INode;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeBoard;
import sweng_plus.framework.boardgame.nodes_board.interfaces.INodeFigure;

import java.util.LinkedList;
import java.util.List;

public class NodeBoard<N extends INode<F, N>, F extends INodeFigure<N, F>> implements INodeBoard<N, F>
{
    protected final List<N> nodes;
    protected final List<F> figures;
    
    public NodeBoard(List<N> nodes, List<F> figures)
    {
        this.nodes = nodes;
        this.figures = figures;
    }
    
    public NodeBoard()
    {
        this(new LinkedList<>(), new LinkedList<>());
    }
    
    @Override
    public List<N> getNodes()
    {
        return nodes;
    }
    
    @Override
    public List<F> getFigures()
    {
        return figures;
    }
    
    @Override
    public void addNode(N node)
    {
        nodes.add(node);
    }
    
    @Override
    public void addFigure(F figure)
    {
        figures.add(figure);
    }
    
    @Override
    public boolean isNodeOnBoard(N node)
    {
        return nodes.contains(node);
    }
    
    @Override
    public boolean isFigureOnBoard(F figure)
    {
        return figures.contains(figure);
    }
}
