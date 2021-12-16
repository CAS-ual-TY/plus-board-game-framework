package sweng_plus.framework.boardgame.nodes_board.interfaces;

import java.util.List;
import java.util.function.Predicate;

public interface INodeBoard<N extends INode<F, N>, F extends INodeFigure<N, F>>
{
    List<N> getNodes();
    
    List<F> getNodeFigures();
    
    void addNode(N node);
    
    default void addNodes(List<N> nodes)
    {
        nodes.forEach(this::addNode);
    }
    
    void addNodeFigure(F figure);
    
    default void addNodeFigures(List<F> figures)
    {
        figures.forEach(this::addNodeFigure);
    }
    
    boolean isNodeOnBoard(N node);
    
    boolean isFigureOnBoard(F figure);
    
    default List<N> getForwardNodes(N start, int distance, Predicate<N> predicate)
    {
        if(!isNodeOnBoard(start))
        {
            return List.of();
        }
        return start.getDistantForwardNodes(distance, predicate);
    }
    
    default List<N> getForwardNodes(F figure, int distance, Predicate<N> predicate)
    {
        return getForwardNodes(figure.getCurrentNode(), distance, predicate);
    }
    
    default List<N> getBackwardNodes(N start, int distance, Predicate<N> predicate)
    {
        if(!isNodeOnBoard(start))
        {
            return List.of();
        }
        return start.getDistantBackwardNodes(distance, predicate);
    }
    
    default List<N> getBackwardNodes(F figure, int distance, Predicate<N> predicate)
    {
        return getBackwardNodes(figure.getCurrentNode(), distance, predicate);
    }
    
    default void moveFigure(F figure, N target)
    {
        if(!isFigureOnBoard(figure) || !isNodeOnBoard(target))
        {
            return;
        }
        
        figure.getCurrentNode().removeFigure(figure);
        figure.setCurrentNode(target);
        target.addFigure(figure);
    }
    
    default void linkNodes(N backwardNode, N forwardNode)
    {
        INode.linkNodes(backwardNode, forwardNode);
    }
}
